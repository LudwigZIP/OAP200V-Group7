
package com.librarymanager.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import com.librarymanager.dao.WriterDAO;
import com.librarymanager.model.Writer;
import com.librarymanager.view.WriterView;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Controller class for managing writers.
 * This class acts as a link between the WriterView and WriterDAO.
 * It handles user actions from the interface, performs CRUD
 * operations through the DAO layer, and updates the user interface.
 *
 *   The controller is responsible for:
 * - Loading writers from the database and displaying them in the table
 * - Adding new writers
 * - Updating existing writer information
 * - Deleting selected writers
 * - Checking for duplicate writer names
 * - Synchronizing table selections with the input fields
 * - Displaying error and confirmation messages to the user
 * 
 * @author Mia Bro Auganes
 */
public class WriterController {
    // DAO used to access writer data in the database
    private final WriterDAO writerDAO;

    // Reference to the writer view, containing the writer table, input fields and buttons.
    private final WriterView view;

    // List of writers displayed in the table
    private final ObservableList<Writer> writerList;

    /**
     * Creates a controller with access to the view and DAO.
     * The constructor creates the writer list and initializes
     * the connection between the controller and the view. 
     *
     * @param view the writer view
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
     * Initializes the controller and connects the writer list to the table.
     * This method also loads existing writers from the database when the controller starts.
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
     * Adds a new writer using the information entered in the input fields
     */
    private void addWriter() {

        // Get the input from the text fields
        String firstName = view.getFirstNameField().getText();
        String lastName = view.getLastNameField().getText();
        String nationality = view.getNationalityField().getText();

        // Check if the input fields are empty
        if (firstName.isEmpty() || lastName.isEmpty() || nationality.isBlank()) {
            // Show an error message and inform the user that all fields must be completed
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Missing information");
            alert.setContentText("Please enter first name, last name and nationality.");
            alert.showAndWait();
            return;
        }

        // Check if a writer with the same name already exists
        if (!confirmWriterIfExists(firstName, lastName, null)) {
            return;
        }
        // Create a new writer object using the entered information
        Writer writer = new Writer(firstName, lastName, nationality);

        // Save the writer to the database
        Writer savedWriter = writerDAO.create(writer);

        // Add the saved writer to the list displayed in the table
        writerList.add(savedWriter);

        // Clear the input fields
        clearForm();
    }

    /**
     * Checks if a writer with the same name already exists
     * and asks the user whether to continue.
     *
     * @return true if the writer does not exist or the user confirms
     */
    private boolean confirmWriterIfExists(String firstName, String lastName, Integer excludedId) {

        // Check if the writer already exists in the database, if not the operation continue
        if (!writerDAO.existsWriter(firstName, lastName, excludedId)) {
            return true;
        }

        // Show a confirmation dialog if a duplicate exists. The user choose to continue or cancel
        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "A writer named " + firstName + " " + lastName
                        + " already exists in the database.\n\n"
                        + "Do you want to continue anyway?",
                ButtonType.YES,
                ButtonType.NO);

        confirmation.setHeaderText("Writer already exists");

        // Return true only if the user selects Yes
        return confirmation.showAndWait()
                .filter(response -> response == ButtonType.YES)
                .isPresent();
    }

    /**
     * Updates the selected writer in the database
     */
    private void updateWriter() {

        // Get the selected writer from the table
        Writer selectedWriter = view.getWriterTable()
                .getSelectionModel().getSelectedItem();

        // Check if a writer has been selected
        if (selectedWriter != null) {

            // Get the updated input from the text fields
            String firstName = view.getFirstNameField().getText();
            String lastName = view.getLastNameField().getText();
            String nationality = view.getNationalityField().getText();

            // Check if another writer with the same name already exists
            if (!confirmWriterIfExists(firstName, lastName, selectedWriter.getId())) {
                return;
            }
            // Update the selected writer object with new information
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

        try {
            // Delete the writer from the database, the deletion may failed of books are linked to the writer
            writerDAO.deleteWriter(selectedWriter);

            // Reload the writers in the table
            loadWriters();

            // Clear the input fields
            clearForm();

        } catch (RuntimeException e) {

            // Show an error message if deletion fails, and explain that the writer may have books linked to them
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Delete Failed");
            alert.setHeaderText("Could not delete writer");
            alert.setContentText(
                    "The writer could not be deleted. " +
                    "The writer may have books linked to them.");
            alert.showAndWait();
        }
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
     * Clears the input fields.
     * This method is used after adding, updating or deleting a writer. 
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