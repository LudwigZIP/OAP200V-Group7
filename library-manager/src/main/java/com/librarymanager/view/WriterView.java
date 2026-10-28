
package com.librarymanager.view;

import com.librarymanager.model.Writer;

import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

/**
 * View class for displaying and managing writers
 * in the Library Manager application.
 *
 * @author Mia Bro Auganes
 */
public class WriterView extends BorderPane {

    // Table to show writer objects
    private final TableView<Writer> table = new TableView<>();

    // Input fields for receiving name and nationality for the Writer
    private final TextField firstNameField = new TextField();
    private final TextField lastNameField = new TextField();
    private final TextField nationalityField = new TextField();

    // Buttons for writer operations
    private final Button addButton = new Button("Add");
    private final Button updateButton = new Button("Update");
    private final Button deleteButton = new Button("Delete");

    /**
     * Creates the WriterView.
     */
    public WriterView() {
        buildTable();
        buildForm();
        setCenter(table);
    }

    /**
     * Creates the table for displaying writers.
     */
    private void buildTable() {
        TableColumn<Writer, String> firstNameColumn = new TableColumn<>("First Name");

        firstNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("firstName"));

        TableColumn<Writer, String> lastNameColumn = new TableColumn<>("Last Name");

        lastNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("lastName"));

        TableColumn<Writer, String> nationalityColumn = new TableColumn<>("Nationality");

        nationalityColumn.setCellValueFactory(
                new PropertyValueFactory<>("nationality"));

        table.getColumns().add(firstNameColumn);
        table.getColumns().add(lastNameColumn);
        table.getColumns().add(nationalityColumn);
    }

    /**
     * Creates the input fields and buttons for writers.
     */
    private void buildForm() {

        // Labels for the input fields
        Label firstNameLabel = new Label("First Name");
        Label lastNameLabel = new Label("Last Name");
        Label nationalityLabel = new Label("Nationality");

        // Places the buttons next to each other
        HBox buttons = new HBox(10, addButton, updateButton, deleteButton);

        // Places fields and buttons vertically
        VBox form = new VBox(10,
                firstNameLabel,
                firstNameField,
                lastNameLabel,
                lastNameField,
                nationalityLabel,
                nationalityField,
                buttons);

        form.setPadding(new Insets(15));

        // Places the form at the bottom of the window
        setBottom(form);
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
     * Returns the writer table.
     *
     * @return the writer table
     */
    public TableView<Writer> getWriterTable() {
        return table;
    }

    /**
     * Returns the first name input field.
     *
     * @return the first name field
     */
    public TextField getFirstNameField() {
        return firstNameField;
    }

    /**
     * Returns the last name input field.
     *
     * @return the last name field
     */
    public TextField getLastNameField() {
        return lastNameField;
    }

    /**
     * Returns the nationality input field.
     *
     * @return the nationality field
     */
    public TextField getNationalityField() {
        return nationalityField;
    }
}
