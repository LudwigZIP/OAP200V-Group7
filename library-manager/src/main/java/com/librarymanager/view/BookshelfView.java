package com.booklibrary.ui;

import com.booklibrary.model.Bookshelf;
import com.booklibrary.service.LibraryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Reference-data management screen for {@link Bookshelf} records: lists
 * all bookshelves and allows adding, editing, and deleting them.
 *
 * <p>Author: [Team member name] — responsible for bookshelf management UI.</p>
 */
// The view extends BorderPane, so it is itself a layout node that can be
// placed directly in a scene. Table goes in the center, form on the right.
public class BookshelfView extends BorderPane {

    // The service handles business logic and database access; the UI never talks to the DAO directly.
    private final LibraryService service;
    // Callback to let the rest of the app (e.g. other views) know that data has changed.
    private final Runnable onDataChanged;

    // The table shows the list; "data" is the observable list backing it.
    // When "data" changes, the table updates automatically.
    private final TableView<Bookshelf> table = new TableView<>();
    private final ObservableList<Bookshelf> data = FXCollections.observableArrayList();

    // Form input fields
    private final TextField nameField = new TextField();
    private final TextField roomField = new TextField();
    private final TextField shelfNumberField = new TextField();
    private final TextArea descriptionField = new TextArea();

    // The shelf currently selected in the table (null if none is selected).
    private Bookshelf selectedShelf;

    /**
     * @param service       shared library service
     * @param onDataChanged callback invoked after any add/edit/delete
     */
    public BookshelfView(LibraryService service, Runnable onDataChanged) {
        this.service = service;
        this.onDataChanged = onDataChanged;
        setPadding(new Insets(10));

        setCenter(buildTable());
        setRight(buildForm());
        refresh(); // load shelves from the database on startup
    }

    // ===== UI construction =====

    // Builds the table and its columns.
    private TableView<Bookshelf> buildTable() {
        // PropertyValueFactory finds the value by calling the getter with the
        // given name (e.g. "name" -> getName()) on each Bookshelf.
        TableColumn<Bookshelf, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(160);

        TableColumn<Bookshelf, String> roomCol = new TableColumn<>("Room");
        roomCol.setCellValueFactory(new PropertyValueFactory<>("room"));
        roomCol.setPrefWidth(120);

        TableColumn<Bookshelf, Integer> shelfNumCol = new TableColumn<>("Shelf #");
        shelfNumCol.setCellValueFactory(new PropertyValueFactory<>("shelfNumber"));
        shelfNumCol.setPrefWidth(80);

        TableColumn<Bookshelf, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.setPrefWidth(200);

        table.getColumns().addAll(nameCol, roomCol, shelfNumCol, descCol);
        table.setItems(data);
        // When the user selects a row, fill the form with that shelf's values.
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> populateForm(newV));
        return table;
    }

    // Builds the input form and the action buttons.
    private VBox buildForm() {
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setPadding(new Insets(0, 0, 0, 15));

        descriptionField.setPrefRowCount(3);
        descriptionField.setWrapText(true);

        // Labels in column 0, input fields in column 1. "*" marks a required field.
        grid.add(new Label("Name *:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Room:"), 0, 1);
        grid.add(roomField, 1, 1);
        grid.add(new Label("Shelf number:"), 0, 2);
        grid.add(shelfNumberField, 1, 2);
        grid.add(new Label("Description:"), 0, 3);
        grid.add(descriptionField, 1, 3);

        // Each button triggers one of the CRUD actions below.
        Button addBtn = new Button("Add new");
        addBtn.setOnAction(e -> addShelf());
        Button updateBtn = new Button("Save changes");
        updateBtn.setOnAction(e -> updateShelf());
        Button deleteBtn = new Button("Delete");
        deleteBtn.setOnAction(e -> deleteShelf());
        Button clearBtn = new Button("Clear");
        clearBtn.setOnAction(e -> clearForm());

        HBox buttons = new HBox(6, addBtn, updateBtn, deleteBtn, clearBtn);

        VBox box = new VBox(10, new Label("Bookshelf details"), grid, buttons);
        box.setPrefWidth(320);
        return box;
    }

    // ===== Form and table helpers =====

    // Reloads all shelves from the database into the table.
    private void refresh() {
        data.setAll(service.getAllBookshelves());
    }

    // Copies the selected shelf's values into the form fields.
    private void populateForm(Bookshelf s) {
        selectedShelf = s;
        if (s == null) {
            clearForm();
            return;
        }
        nameField.setText(s.getName());
        roomField.setText(s.getRoom());
        // shelfNumber may be null, so convert to text only if it has a value.
        shelfNumberField.setText(s.getShelfNumber() != null ? String.valueOf(s.getShelfNumber()) : "");
        descriptionField.setText(s.getDescription());
    }

    // Empties the form and removes the table selection (ready for a new entry).
    private void clearForm() {
        selectedShelf = null;
        nameField.clear();
        roomField.clear();
        shelfNumberField.clear();
        descriptionField.clear();
        table.getSelectionModel().clearSelection();
    }

    // Converts the shelf number text to an Integer.
    // Returns null if the field is empty or not a valid whole number.
    private Integer parseShelfNumber() {
        String text = shelfNumberField.getText() == null ? "" : shelfNumberField.getText().trim();
        if (text.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            showInfo("Shelf number must be a whole number. It will be left empty.");
            return null;
        }
    }

    // ===== CRUD actions =====

    // CREATE: builds a new Bookshelf from the form and saves it.
    private void addShelf() {
        try {
            Bookshelf shelf = new Bookshelf(nameField.getText().trim(), roomField.getText(),
                    parseShelfNumber(), descriptionField.getText());
            service.addBookshelf(shelf);
            refresh();
            clearForm();
            notifyChanged();
        } catch (Exception ex) {
            showError("Failed to add bookshelf", ex);
        }
    }

    // UPDATE: writes the form values into the selected shelf and saves it.
    private void updateShelf() {
        if (selectedShelf == null) {
            showInfo("Select a bookshelf in the table first.");
            return;
        }
        try {
            selectedShelf.setName(nameField.getText().trim());
            selectedShelf.setRoom(roomField.getText());
            selectedShelf.setShelfNumber(parseShelfNumber());
            selectedShelf.setDescription(descriptionField.getText());
            service.updateBookshelf(selectedShelf);
            refresh();
            notifyChanged();
        } catch (Exception ex) {
            showError("Failed to update bookshelf", ex);
        }
    }

    // DELETE: asks for confirmation, then removes the selected shelf.
    private void deleteShelf() {
        if (selectedShelf == null) {
            showInfo("Select a bookshelf in the table first.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete bookshelf \"" + selectedShelf.getName() + "\"?\n\n"
                        + "Note: this will fail if any book still references this shelf "
                        + "(the database requires every book to have a bookshelf).",
                ButtonType.YES, ButtonType.NO);
        // Only delete if the user clicks YES. Errors (e.g. foreign key
        // violation when books still use the shelf) are shown in an error dialog.
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    service.removeBookshelf(selectedShelf.getId());
                    refresh();
                    clearForm();
                    notifyChanged();
                } catch (Exception ex) {
                    showError("Failed to delete bookshelf", ex);
                }
            }
        });
    }

    // ===== Notifications and dialogs =====

    // Tells the rest of the app that data changed (if a callback was provided).
    private void notifyChanged() {
        if (onDataChanged != null) {
            onDataChanged.run();
        }
    }

    // Shows a simple information popup.
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }

    // Shows an error popup with a header and the exception message.
    private void showError(String header, Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(ex.getMessage());
        alert.showAndWait();
    }
}
