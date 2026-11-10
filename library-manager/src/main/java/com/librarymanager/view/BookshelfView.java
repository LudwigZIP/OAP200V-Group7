package com.librarymanager.view;

import com.librarymanager.model.Bookshelf;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * View for displaying and editing bookshelves.
 * @author Thomas Molnvik
 * <p>The controller connects the buttons and table selection to application
 * actions; this class is responsible for constructing the interface.</p>
 */
public class BookshelfView extends BorderPane {

    private final TableView<Bookshelf> bookshelfTable = new TableView<>();

    // Input fields for bookshelf details.
    private final TextField nameField = new TextField();
    private final TextField roomField = new TextField();
    private final TextField shelfNumberField = new TextField();

    // Buttons trigger actions that are assigned by the controller.
    private final Button addButton = new Button("Add");
    private final Button updateButton = new Button("Update");
    private final Button deleteButton = new Button("Delete");
    private final Button clearButton = new Button("Clear");

    /**
     * Creates the BookshelfView.
     */
    public BookshelfView() {
        setPadding(new Insets(10));
        buildTable();
        buildForm();
        setCenter(bookshelfTable);
    }

    /**
     * Creates the table for displaying bookshelves.
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

        bookshelfTable.getColumns().addAll(
                nameColumn, roomColumn, shelfNumberColumn);
        bookshelfTable.setPlaceholder(new Label("No bookshelves have been added yet."));
    }

    /**
     * Creates the input fields and buttons for bookshelves.
     */
    private void buildForm() {

        // Sets placeholder text for input fields
        nameField.setPromptText("Required");
        roomField.setPromptText("Optional");
        shelfNumberField.setPromptText("Optional");

        // Places buttons next to one another
        HBox buttons = new HBox(8, addButton, updateButton, deleteButton, clearButton);

        //
        VBox form = new VBox(10, 
            new Label("Bookshelf details"),
            new Label("Name"), nameField,
            new Label("Room"), roomField,
            new Label("Shelf number"), shelfNumberField, buttons);

        form.setPadding(new Insets(10));

        // Makes the form scrollable if necessary
        ScrollPane scrollPane = new ScrollPane(form);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefWidth(300);

        // Places the form to the right of the table
        setRight(scrollPane);
    }

    /**
     * Returns the bookshelf table.
     *
     * @return the bookshelf table
     */
    public TableView<Bookshelf> getBookshelfTable() {
        return bookshelfTable;
    }

    /**
     * Returns the name input field.
     *
     * @return the name input field
     */
    public TextField getNameField() {
        return nameField;
    }

    /**
     * Returns the room input field.
     *
     * @return the room input field
     */
    public TextField getRoomField() {
        return roomField;
    }

    /**
     * Returns the shelf number input field.
     *
     * @return the shelf-number input field
     */
    public TextField getShelfNumberField() {
        return shelfNumberField;
    }

    /**
     * Returns the Add button.
     *
     * @return the Add button
     */
    public Button getAddButton() {
        return addButton;
    }

    /**
     * Returns the Update button.
     *
     * @return the Update button
     */
    public Button getUpdateButton() {
        return updateButton;
    }

    /**
     * Returns the Delete button.
     *
     * @return the Delete button
     */
    public Button getDeleteButton() {
        return deleteButton;
    }

    /**
     * Returns the Clear button.
     *
     * @return the Clear button
     */
    public Button getClearButton() {
        return clearButton;
    }
}
