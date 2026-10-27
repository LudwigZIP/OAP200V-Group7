package com.librarymanager.controller;

import com.librarymanager.dao.BookshelfDAO;
import com.librarymanager.model.Bookshelf;
import com.librarymanager.view.BookshelfView;

import java.util.List;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Controller class for managing bookshelves in the Library Manager application.
 *
 * @author Thomas Mølnvik
 */
public class BookshelfController {

    private final BookshelfDAO bookshelfDAO;
    private final BookshelfView view;
    private final ObservableList<Bookshelf> bookshelfList;

    /**
     * Creates a controller with access to the view and DAO.
     *
     * @param view the bookshelf view
     * @param bookshelfDAO the DAO used to manage bookshelves
     */
    public BookshelfController(BookshelfView view, BookshelfDAO bookshelfDAO) {
        this.view = Objects.requireNonNull(view, "view");
        this.bookshelfDAO = Objects.requireNonNull(bookshelfDAO, "bookshelfDAO");
        this.bookshelfList = FXCollections.observableArrayList();
        initialize();
    }

    /**
     * Initializes the controller and connects the bookshelf list to the table.
     */
    private void initialize() {
        view.getBookshelfTable().setItems(bookshelfList);
        view.getAddButton().setOnAction(event -> addBookshelf());
        view.getUpdateButton().setOnAction(event -> updateBookshelf());
        view.getDeleteButton().setOnAction(event -> deleteBookshelf());
        view.getClearButton().setOnAction(event -> clearForm());

        view.getBookshelfTable().getSelectionModel().selectedItemProperty()
                .addListener((observable, oldBookshelf, selectedBookshelf) ->
                        populateForm(selectedBookshelf));
        loadBookshelves();
    }

    /**
     * Adds a new bookshelf using the input fields.
     */
    private void addBookshelf() {
        Bookshelf bookshelf = readForm();
        if (bookshelf == null) {
            return;
        }

        if (!confirmShelfNumberIfInUse(bookshelf.getShelfNumber(), null)) {
            return;
        }

        try {
            bookshelfDAO.create(bookshelf);
            loadBookshelves();
            clearForm();
        } catch (RuntimeException e) {
            showError("Failed to add bookshelf", e);
        }
    }

    /**
     * Updates the selected bookshelf in the database.
     */
    private void updateBookshelf() {
        Bookshelf selectedBookshelf = view.getBookshelfTable()
                .getSelectionModel().getSelectedItem();
        if (selectedBookshelf == null) {
            showInfo("Select a bookshelf in the table first.");
            return;
        }

        Bookshelf formValues = readForm();
        if (formValues == null) {
            return;
        }

        Bookshelf updatedBookshelf = new Bookshelf(
                selectedBookshelf.getId(),
                formValues.getName(),
                formValues.getRoom(),
                formValues.getShelfNumber());

        if (!confirmShelfNumberIfInUse(
                updatedBookshelf.getShelfNumber(), selectedBookshelf.getId())) {
            return;
        }

        try {
            bookshelfDAO.update(updatedBookshelf);
            loadBookshelves();
            clearForm();
        } catch (RuntimeException e) {
            showError("Failed to update bookshelf", e);
        }
    }

    /**
     * Deletes the selected bookshelf from the database.
     */
    private void deleteBookshelf() {
        Bookshelf selectedBookshelf = view.getBookshelfTable()
                .getSelectionModel().getSelectedItem();
        if (selectedBookshelf == null) {
            showInfo("Select a bookshelf in the table first.");
            return;
        }

        int selectedId = selectedBookshelf.getId();
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete bookshelf \"" + selectedBookshelf.getName() + "\"?\n\n"
                        + "Books that use this bookshelf must be reassigned first.",
                ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    bookshelfDAO.delete(selectedId);
                    loadBookshelves();
                    clearForm();
                } catch (RuntimeException e) {
                    showError("Failed to delete bookshelf", e);
                }
            }
        });
    }

    /**
     * Loads all bookshelves from the database into the table.
     */
    private void loadBookshelves() {
        try {
            List<Bookshelf> bookshelves = bookshelfDAO.findAll();
            bookshelfList.setAll(bookshelves);
        } catch (RuntimeException e) {
            showError("Failed to load bookshelves", e);
        }
    }

    /**
     * Displays the selected bookshelf in the input fields.
     *
     * @param bookshelf the selected bookshelf, or null when the selection is cleared
     */
    private void populateForm(Bookshelf bookshelf) {
        if (bookshelf == null) {
            clearFields();
            return;
        }

        view.getNameField().setText(bookshelf.getName());
        view.getRoomField().setText(bookshelf.getRoom());
        view.getShelfNumberField().setText(bookshelf.getShelfNumber() == null
                ? "" : String.valueOf(bookshelf.getShelfNumber()));
    }

    /**
     * Reads and validates the values entered in the form.
     *
     * @return a new bookshelf, or null if the input is invalid
     */
    private Bookshelf readForm() {
        String name = view.getNameField().getText();
        if (name == null || name.isBlank()) {
            showInfo("Bookshelf name is required.");
            return null;
        }

        Integer shelfNumber;
        String shelfNumberText = view.getShelfNumberField().getText();
        if (shelfNumberText == null || shelfNumberText.isBlank()) {
            shelfNumber = null;
        } else {
            try {
                shelfNumber = Integer.valueOf(shelfNumberText.trim());
            } catch (NumberFormatException e) {
                showInfo("Shelf number must be a whole number.");
                return null;
            }
        }

        return new Bookshelf(name, view.getRoomField().getText(), shelfNumber);
    }

    /**
     * Checks whether another bookshelf uses the shelf number and asks whether
     * the user wants to continue if it is already assigned.
     *
     * @param shelfNumber the shelf number to check, or null if none was entered
     * @param excludedBookshelfId the bookshelf ID to ignore, or null when adding
     * @return true if the number is available or the user confirms reuse
     */
    private boolean confirmShelfNumberIfInUse(
            Integer shelfNumber, Integer excludedBookshelfId) {
        if (shelfNumber == null) {
            return true;
        }

        try {
            if (!bookshelfDAO.isShelfNumberInUse(shelfNumber, excludedBookshelfId)) {
                return true;
            }
        } catch (RuntimeException e) {
            showError("Could not check shelf number", e);
            return false;
        }

        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Shelf number " + shelfNumber
                        + " is already assigned to another bookshelf.\n\n"
                        + "Do you want to use this shelf number anyway?",
                ButtonType.YES,
                ButtonType.NO);
        confirmation.setHeaderText("Shelf number already in use");
        confirmation.getDialogPane().lookupButton(ButtonType.NO).requestFocus();

        return confirmation.showAndWait()
                .filter(response -> response == ButtonType.YES)
                .isPresent();
    }

    /**
     * Clears the input fields and table selection.
     */
    private void clearForm() {
        view.getBookshelfTable().getSelectionModel().clearSelection();
        clearFields();
    }

    /**
     * Clears all input fields.
     */
    private void clearFields() {
        view.getNameField().clear();
        view.getRoomField().clear();
        view.getShelfNumberField().clear();
    }

    /**
     * Displays an information message.
     *
     * @param message the message to display
     */
    private void showInfo(String message) {
        new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK).showAndWait();
    }

    /**
     * Displays an error message.
     *
     * @param header the error message header
     * @param exception the exception that caused the error
     */
    private void showError(String header, Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        String message = exception.getMessage();
        if (exception.getCause() != null && exception.getCause().getMessage() != null) {
            message += "\n" + exception.getCause().getMessage();
        }
        alert.setContentText(message);
        alert.showAndWait();
    }
}
