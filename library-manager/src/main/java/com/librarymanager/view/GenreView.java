package com.librarymanager.view;

import com.librarymanager.model.Genre;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * JavaFX view responsible for displaying and managing the user interface
 * for working with Genre objects. This class builds the visual layout
 * where users can view existing genres, enter new genre names, and trigger
 * actions such as adding, updating, or deleting genres.
 *
 * The view is structured using a BorderPane:
 * - The center area contains a TableView that lists all genres.
 * - The bottom area contains a form with a text field and action buttons.
 *
 * Main UI components:
 * - TableView<Genre> table:
 *      Displays all genres with two columns: ID and Name.
 *      Columns are connected to Genre properties via PropertyValueFactory,
 *      which maps "id" to getId() and "name" to getName().
 *
 * - TextField nameField:
 *      Allows the user to type the name of a genre when creating or updating entries.
 *
 * - Buttons (Add, Update, Delete):
 *      These buttons do not contain logic themselves; they are exposed through
 *      getter methods so the controller can attach event handlers and define
 *      what happens when the user clicks them.
 *
 * Layout construction:
 * - buildTable():
 *      Creates and configures the table columns, then adds them to the table.
 *
 * - buildForm():
 *      Builds the input form using VBox and HBox containers.
 *      Includes a label + text field row, and a row of action buttons.
 *      The form is placed in the bottom region of the BorderPane.
 *
 * Controller interaction:
 * Getter methods provide access to the table, text field, and buttons.
 * This allows the controller to:
 *      - populate the table with data,
 *      - read user input,
 *      - respond to button clicks,
 *      - update the UI based on database operations.
 *
 * Overall, GenreView defines the complete user interface for genre management,
 * while delegating all logic and data handling to the controller and DAO layers.
 * 
 * @author: Erika Marie Gåsland
 */


// This view builds the screen for working with genres.
// The table shows genres, and the form lets the user enter a genre name.
// BorderPane provides areas where the table and form can be placed.
public class GenreView extends BorderPane {

    // Displays the genre records. The controller will supply the table's data.
    private final TableView<Genre> table = new TableView<>();

    // The text field where the user types a genre name.
    private final TextField nameField = new TextField();

    // These buttons let the user request genre actions.
    // The controller will decide what happens when each button is clicked.
    // addButton is used to add a new genre to the database.
    // updateButton is used to update an existing genre in the database.
    // deleteButton is used to delete a genre from the database.
    private final Button addButton = new Button("Add");
    private final Button updateButton = new Button("Update");
    private final Button deleteButton = new Button("Delete");

    // Set up the table and form, then place the table in the center of the screen.
    public GenreView() {
        buildTable();
        buildForm();
        setCenter(table);
    }

    // Create the table columns and connect them to Genre getters.
    private void buildTable() {
        // Displays each genre's ID by calling getId() on its Genre object.
        // The PropertyValueFactory uses the property name "id" to find the getId() method in the Genre class.
        TableColumn<Genre, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Displays each genre's name by calling getName() on its Genre object.
        // The PropertyValueFactory uses the property name "name" to find the getName() method in the Genre class.
        TableColumn<Genre, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Add both columns to the table so they appear on screen.
        table.getColumns().addAll(idColumn, nameColumn);
    }

    // Arrange the name field and action buttons below the table.
    // The form is placed in a VBox, which stacks its children vertically.
    // The name field and its label are placed in an HBox, which arranges them horizontally.
    private void buildForm() {
        // VBox places its children in a vertical column; 10 is the spacing.
        // Insets adds padding around the VBox so the form is not right against the table.
        VBox formBox = new VBox(10);
        formBox.setPadding(new Insets(10));

        // HBox places the label and text field next to each other.
        // The spacing of 10 pixels is the space between the label and the text field.
        HBox nameBox = new HBox(10);
        nameBox.getChildren().addAll(new Label("Genre name:"), nameField);

        // Place the three action buttons next to each other.
        // The spacing of 10 pixels is the space between each button.
        // The buttons are placed in an HBox so they appear in a row.
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton);

        // Put the name row and button row in the form, then place it below the table.
        // The formBox is placed in the bottom area of the BorderPane, which is below the table.
        formBox.getChildren().addAll(nameBox, buttonBox);
        setBottom(formBox);
    }

    // Give the controller access to the table to display and select genres.
    // @return the TableView that displays genres
    public TableView<Genre> getTable() {
        return table;
    }

    // Give the controller access to the text the user entered.
    // @return the TextField for entering the genre name
    public TextField getNameField() {
        return nameField;
    }

    // Give the controller access to the Add button to handle clicks.
    // @return the Button for adding a new genre
    public Button getAddButton() {
        return addButton;
    }

    // Give the controller access to the Update button to handle clicks.
    // @return the Button for updating an existing genre
    public Button getUpdateButton() {
        return updateButton;
    }

    // Give the controller access to the Delete button to handle clicks.
    // @return the Button for deleting a genre
    public Button getDeleteButton() {
        return deleteButton;
    }
}