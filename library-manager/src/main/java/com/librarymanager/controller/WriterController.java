
package com.librarymanager.controller;

import javafx.scene.control.Alert;
import com.librarymanager.dao.WriterDAO;
import com.librarymanager.model.Writer;
import com.librarymanager.view.WriterView;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Controller class for managing writers in the Library Manager application
 * 
 */
public class WriterController {
    // DAO used to access writer data
    private final WriterDAO writerDAO;

    // Reference to the writer view
    private final WriterView view;

    // List of writers displayed in the table
    private final ObservableList<Writer> writerList;

    /**
     * Creates a controller with access to the view and DAO.
     *
     * @param view      the writer view
     * @param writerDAO the DAO used to manage writers
     */
    public WriterController(WriterView view, WriterDAO writerDAO) {
        this.view = view;
        this.writerDAO = writerDAO;
        this.writerList = FXCollections.observableArrayList();

        // initialize the controller
        initialize();
    }

    /**
     * Initializes the controller and connects the writer list to the table
     */
    private void initialize() {

        // Display the writer list in the table
        view.getWriterTable().setItems(writerList);

        // Handle clicks on the Add button
        view.getAddButton().setOnAction(e -> addWriter());

        // Handle clicks on the Update button
        view.getUpdateButton().setOnAction(e -> updateWriter());

        // Handle clicks on the Delete button
        view.getDeleteButton().setOnAction(e -> deleteWriter());

        // Load existing writers from the database
        loadWriters();

        // Display the selected writer in the input fields
        view.getWriterTable().getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldWriter, selectedWriter) -> {

                    // Check if a writer is selected
                    if (selectedWriter != null) {

                        // Display the writer´s first name in the fist name field
                        view.getFirstNameField().setText(selectedWriter.getFirstName());

                        // Display the writer´s last name in the last name field
                        view.getLastNameField().setText(selectedWriter.getLastName());

                        // Display the writer's nationality in the nationality field
                        view.getNationalityField().setText(selectedWriter.getNationality());
                    }
                });
    }

    /**
     * Adds a new writer using the input fields
     */
    private void addWriter() {

        // Get the input from the text fields
        String firstName = view.getFirstNameField().getText();
        String lastName = view.getLastNameField().getText();
        String nationality = view.getNationalityField().getText();

        // Check if the input fields are empty
        if (firstName.isEmpty()  || lastName.isEmpty() ||nationality.isEmpty()) {
            // Show an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Missing information");
            alert.setContentText("Please enter first name, last name and nationality.");
            alert.showAndWait();
            return;
        }

        // Create a new writer object
        Writer writer = new Writer(firstName, lastName, nationality);

        // Save the writer to the database
        Writer savedWriter = writerDAO.create(writer);

        // Add the saved writer to the table
        writerList.add(savedWriter);

        // Clear the input fields
        clearForm();
    }

    /**
     * Updates the selected writer in the database
     */
    private void updateWriter() {

        // Get the selected writer from the table
        Writer selectedWriter = view.getWriterTable()
                .getSelectionModel().getSelectedItem();

        // Check if a writer is selected
        if (selectedWriter != null) {

            // Get the updated input from the text fields
            String firstName = view.getFirstNameField().getText();
            String lastName = view.getLastNameField().getText();
            String nationality = view.getNationalityField().getText();

            // Update the writer object
            selectedWriter.setFirstName(firstName);
            selectedWriter.setLastName(lastName);
            selectedWriter.setNationality(nationality);

            // Save the changes to the database
            writerDAO.updateWriter(selectedWriter);

            // Reload the writers in the table
            loadWriters();

            // Clear the input fields
            clearForm();
        }

    }

    /**
     * Deletes the selected writer from the database.
     */
    private void deleteWriter() {

        // Get the selected writer from the table
        Writer selectedWriter = view.getWriterTable()
                .getSelectionModel().getSelectedItem();

        // Check if a writer is selected
        if (selectedWriter != null) {

            // Delete the writer from the database
            writerDAO.deleteWriter(selectedWriter);

            // Reload the writers in the table
            loadWriters();

            // Clear the input fields
            clearForm();
        }
    }

    /**
     * Loads all writers from the database into the table
     */
    private void loadWriters() {

        // Get all writers from the database
        List<Writer> writers = writerDAO.findAll();

        // Display the writers in the table
        writerList.setAll(writers);
    }

    /**
     * Clears the input fields and table selection
     */
    private void clearForm() {
        // Clear the first and last name field
        view.getFirstNameField().clear();
        view.getLastNameField().clear();

        // Clear the nationality field
        view.getNationalityField().clear();

        // Clear the selected writer in the table
        view.getWriterTable().getSelectionModel().clearSelection();
    }
}