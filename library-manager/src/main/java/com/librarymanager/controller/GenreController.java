package com.librarymanager.controller;

import java.util.List;
import java.util.Objects;

import com.librarymanager.dao.GenreDAO;
import com.librarymanager.model.Genre;
import com.librarymanager.view.GenreView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
/**
 * Controller class responsible for managing genre-related interactions
 * in the Library Manager application.
 *
 * This class acts as the intermediary between the GenreView and the
 * GenreDAO. It handles user actions from the graphical interface,
 * validates input, performs CRUD operations through the DAO layer,
 * and updates the user interface accordingly.
 *
 * The controller is responsible for:
 * - Loading genres from the database and displaying them in the table
 * - Adding new genres
 * - Updating existing genres
 * - Deleting selected genres
 * - Synchronizing table selections with the input form
 * - Validating user input before database operations
 * - Displaying informational and error messages to the user
 *
 * The class follows the MVC (Model-View-Controller) architecture,
 * where it coordinates communication between the user interface
 * (View), the business data (Model), and the database access layer (DAO).
 *
 * @author Erika Marie Gåsland
 */
/**
 * Controller class for managing genres in the Library Manager application.
 */

public class GenreController {

    // DAO handles communication with the database.
    private final GenreDAO genreDAO;

    // View contains the table, text field and buttons shown to the user.
    private final GenreView view;

    // This list supplies the genre data displayed in the table.
    private final ObservableList<Genre> genreList;

    /**
     * Creates a controller with access to the genre view and DAO.
     *
     * @param view the genre view
     * @param genreDAO the DAO used to manage genres
     */
    public GenreController(GenreView view, GenreDAO genreDAO) {
        // Ensure that the view and DAO are not null.
        this.view = Objects.requireNonNull(view, "view");
        // Ensure that the DAO is not null.
        this.genreDAO = Objects.requireNonNull(genreDAO, "genreDAO");
        // Create an observable list to hold the genres displayed in the table.
        this.genreList = FXCollections.observableArrayList();

        // Connect the view components to the controller.
        initialize();
    }

    /**
     * Initializes the controller and connects the genre list, buttons and
     * table selection to the view.
     */
    private void initialize() {
        // Display the genre list in the table.
        view.getGenreTable().setItems(genreList);

        // Connect each button to the method that handles its action.
        // The controller handles the logic when the user clicks a button.
        // The add button is connected to the addGenre method.
        view.getAddButton().setOnAction(event -> addGenre());
        // The update button is connected to the updateGenre method.
        view.getUpdateButton().setOnAction(event -> updateGenre());
        // The delete button is connected to the deleteGenre method.
        view.getDeleteButton().setOnAction(event -> deleteGenre());

        // Display the selected genre in the input field.
        // When the user selects a row in the table, the populateForm method is called.
        view.getGenreTable().getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldGenre, selectedGenre) ->
                        populateForm(selectedGenre));

        // Load existing genres when the controller starts.
        loadGenres();
    }

    /**
     * Adds a new genre using the value entered in the form.
     */
    private void addGenre() {
        // Read the name entered by the user.
        String name = view.getNameField().getText();

        // Do not save an empty genre name.
        if (name == null || name.isBlank()) {
            showInfo("Genre name is required.");
            return;
        }

        try {
            // Create and save the new genre through the DAO.
            genreDAO.create(new Genre(name.trim()));

            // Refresh the table and clear the form.
            // After adding a new genre, reload the list of genres from the database.
            // This ensures that the table shows the most up-to-date data, including the newly added genre.
            loadGenres();
            // Clear the input field and table selection to prepare for the next entry.
            clearForm();
            // Inform the user that the genre was added successfully.
            showInfo("Genre added successfully.");
        } catch (RuntimeException e) {
            // Inform the user if saving fails.
            showError("Failed to add genre.", e);
        }
    }

    /**
     * Updates the selected genre using the value entered in the form.
     */
    private void updateGenre() {
        // Get the genre currently selected in the table.
        Genre selectedGenre = view.getGenreTable()
                .getSelectionModel()
                .getSelectedItem();

        // An update requires a selected genre.
        // If no genre is selected, inform the user and exit the method.
        if (selectedGenre == null) {
            showInfo("Select a genre in the table first.");
            return;
        }

        // Read the updated name from the input field.
        String name = view.getNameField().getText();

        // Do not allow the name to be empty.
        if (name == null || name.isBlank()) {
            showInfo("Genre name is required.");
            return;
        }

        try {
            // Update the selected object and save it in the database.
            // The selectedGenre object is modified with the new name.
            selectedGenre.setName(name.trim());
            genreDAO.update(selectedGenre);

            // Refresh the table and clear the form.
            // After updating a genre, reload the list of genres from the database.
            // This ensures that the table shows the most up-to-date data, including the updated genre
            loadGenres();
            // Clear the input field and table selection to prepare for the next action.
            clearForm();
            // Inform the user that the genre was updated successfully.
            showInfo("Genre updated successfully.");
        } catch (RuntimeException e) {
            // Inform the user if updating fails.
            showError("Failed to update genre.", e);
        }
    }

    /**
     * Deletes the selected genre from the database.
     */
    private void deleteGenre() {
        // Get the genre currently selected in the table.
        // The selected genre is retrieved from the table's selection model.
        Genre selectedGenre = view.getGenreTable()
                .getSelectionModel()
                .getSelectedItem();

        // A deletion requires a selected genre.
        if (selectedGenre == null) {
            showInfo("Select a genre in the table first.");
            return;
        }

        try {
            // Delete the selected genre using its database ID.
            // The genreDAO.delete method is called with the ID of the selected genre.
            genreDAO.delete(selectedGenre.getId());

            // Refresh the table and clear the form.
            // After deleting a genre, reload the list of genres from the database.
            // This ensures that the table shows the most up-to-date data, excluding the deleted genre.
            loadGenres();
            // Clear the input field and table selection to prepare for the next action.
            clearForm();
            // Inform the user that the genre was deleted successfully.
            showInfo("Genre deleted successfully.");
        } catch (RuntimeException e) {
            // Inform the user if deletion fails.
            showError("Failed to delete genre.", e);
        }
    }

    /**
     * Loads all genres from the database into the table.
     * This method retrieves the list of genres from the DAO and updates the observable list
     * that backs the table view. It ensures that the table displays the most current data.
     */
    private void loadGenres() {
        try {
            // Retrieve all genres from the database.
            // The genreDAO.findAll() method returns a list of all genres stored in the database.
            List<Genre> genres = genreDAO.findAll();

            // Replace the table contents with the database results.
            // The genreList observable list is updated with the new data.
            genreList.setAll(genres);
        } catch (RuntimeException e) {
            // Inform the user if loading fails.
            showError("Failed to load genres.", e);
        }
    }

    /**
     * Displays the selected genre in the input field.
     * When a user selects a genre in the table, this method populates the input field with the genre's name.
     *
     * @param genre the selected genre, or null when the selection is cleared
     */
    private void populateForm(Genre genre) {
        // Clear the field when the table selection is removed.
        // If the genre parameter is null, it means no genre is selected in the table.
        if (genre == null) {
            clearFields();
            return;
        }

        // Show the selected genre's name in the input field.
        view.getNameField().setText(genre.getName());
    }

    /**
     * Clears the input field and table selection.
     * This method is called after adding, updating, or deleting a genre to reset the form for the next action.
     * It ensures that the user starts with a clean state for entering new data.
     */
    private void clearForm() {
        // Remove the selected row from the table.
        view.getGenreTable().getSelectionModel().clearSelection();

        // Clear the input field.
        // The clearFields method is called to remove any text from the genre name input field.
        clearFields();
    }

    /**
     * Clears the genre input field without changing the table selection.
     * This method is used to reset the input field after an action is performed, 
     * allowing the user to enter new data without affecting the current table selection.
     */
    private void clearFields() {
        // Remove the text currently entered in the genre field.
        // The getNameField().clear() method is called to clear the text from the input field.
        view.getNameField().clear();
    }

    /**
     * Displays an informational message to the user.
     *
     * @param message the message to display
     */
    private void showInfo(String message) {
        // Display an information dialog to the user.
        // The Alert class is used to create a pop-up dialog that shows the message.
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        // Set the title and content of the alert dialog.
        alert.setTitle("Genre");
        // The header text is set to null to remove the default header from the dialog.
        alert.setHeaderText(null);
        // The content text is set to the message passed to the method.
        alert.setContentText(message);
        // Show the alert dialog and wait for the user to close it.
        alert.showAndWait();
    }

    /**
     * Displays an error message to the user.
     *
     * @param message the error description
     * @param exception the exception that caused the error
     */
    // The showError method is used to display an error dialog when an operation fails.
    // It takes a message describing the error and the exception that caused it.
    private void showError(String message, RuntimeException exception) {
        // Display an error dialog when an operation fails.
        Alert alert = new Alert(Alert.AlertType.ERROR);
        // Set the title, header, and content of the error dialog.
        // The title is set to "Genre Error" to indicate the context of the error.
        alert.setTitle("Genre Error");
        // The header text is set to the message describing the error.
        alert.setHeaderText(message);
        // The content text is set to the message from the exception, providing details about the failure.
        alert.setContentText(exception.getMessage());
        // Show the error dialog and wait for the user to close it.
        alert.showAndWait();
    }
}
