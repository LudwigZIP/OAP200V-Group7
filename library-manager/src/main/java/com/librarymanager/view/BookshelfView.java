package com.librarymanager.view;

import com.librarymanager.model.Bookshelf;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * View for displaying and editing bookshelves.
 *
 * <p>The controller connects the buttons and table selection to application
 * actions; this class is responsible for constructing the interface.</p>
 */
public class BookshelfView extends BorderPane {

    private final TableView<Bookshelf> bookshelfTable = new TableView<>();

    // Input fields for bookshelf details.
    private final TextField nameField = new TextField();
    private final TextField roomField = new TextField();
    private final TextField shelfNumberField = new TextField();
    private final TextArea descriptionField = new TextArea();

    // Buttons trigger actions that are assigned by the controller.
    private final Button addButton = new Button("Add");
    private final Button updateButton = new Button("Update");
    private final Button deleteButton = new Button("Delete");
    private final Button clearButton = new Button("Clear");

    /**
     * Creates the bookshelf view.
     */
    public BookshelfView() {
        setPadding(new Insets(10));
        buildTable();
        setCenter(bookshelfTable);
        setBottom(buildForm());
    }

    /**
     * Creates columns for the bookshelf properties shown in the table.
     */
    private void buildTable() {
        TableColumn<Bookshelf, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(180);

        TableColumn<Bookshelf, String> roomColumn = new TableColumn<>("Room");
        roomColumn.setCellValueFactory(new PropertyValueFactory<>("room"));
        roomColumn.setPrefWidth(140);

        TableColumn<Bookshelf, Integer> shelfNumberColumn =
                new TableColumn<>("Shelf number");
        shelfNumberColumn.setCellValueFactory(new PropertyValueFactory<>("shelfNumber"));
        shelfNumberColumn.setPrefWidth(120);

        TableColumn<Bookshelf, String> descriptionColumn =
                new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionColumn.setPrefWidth(260);

        bookshelfTable.getColumns().addAll(
                nameColumn, roomColumn, shelfNumberColumn, descriptionColumn);
        bookshelfTable.setPlaceholder(new Label("No bookshelves have been added yet."));
    }

    /**
     * Creates the input form and places its fields and buttons below the table.
     */
    private VBox buildForm() {
        GridPane fields = new GridPane();
        fields.setHgap(10);
        fields.setVgap(8);
        fields.setPadding(new Insets(12, 0, 0, 0));

        nameField.setPromptText("Required");
        roomField.setPromptText("Optional");
        shelfNumberField.setPromptText("Optional");
        descriptionField.setPromptText("Optional");
        descriptionField.setPrefRowCount(2);
        descriptionField.setWrapText(true);

        fields.add(new Label("Name:"), 0, 0);
        fields.add(nameField, 1, 0);
        fields.add(new Label("Room:"), 2, 0);
        fields.add(roomField, 3, 0);
        fields.add(new Label("Shelf number:"), 0, 1);
        fields.add(shelfNumberField, 1, 1);
        fields.add(new Label("Description:"), 2, 1);
        fields.add(descriptionField, 3, 1);

        HBox buttons = new HBox(8, addButton, updateButton, deleteButton, clearButton);
        VBox form = new VBox(10, new Label("Bookshelf details"), fields, buttons);
        form.setPadding(new Insets(10, 0, 0, 0));
        return form;
    }

    public TableView<Bookshelf> getBookshelfTable() {
        return bookshelfTable;
    }

    public TextField getNameField() {
        return nameField;
    }

    public TextField getRoomField() {
        return roomField;
    }

    public TextField getShelfNumberField() {
        return shelfNumberField;
    }

    public TextArea getDescriptionField() {
        return descriptionField;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getUpdateButton() {
        return updateButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getClearButton() {
        return clearButton;
    }
}
