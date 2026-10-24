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
 * Connects bookshelf UI actions to bookshelf persistence operations.
 */
public class BookshelfController {

    private final BookshelfDAO bookshelfDAO;
    private final BookshelfView view;
    private final ObservableList<Bookshelf> bookshelfList;

    /**
     * Creates the controller and connects the bookshelf table and form.
     *
     * @param view the bookshelf view
     * @param bookshelfDAO the DAO used to manage bookshelf records
     */
    public BookshelfController(BookshelfView view, BookshelfDAO bookshelfDAO) {
        this.view = Objects.requireNonNull(view, "view");
        this.bookshelfDAO = Objects.requireNonNull(bookshelfDAO, "bookshelfDAO");
        this.bookshelfList = FXCollections.observableArrayList();
        initialize();
    }

    /**
     * Wires up the view and loads the current bookshelf records.
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
     * Adds a bookshelf using the values entered in the form.
     */
    private void addBookshelf() {
        Bookshelf bookshelf = readForm();
        if (bookshelf == null) {
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
     * Updates the selected bookshelf with the current form values.
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

        try {
            bookshelfDAO.update(updatedBookshelf);
            loadBookshelves();
            clearForm();
        } catch (RuntimeException e) {
            showError("Failed to update bookshelf", e);
        }
    }

    /**
     * Deletes the selected bookshelf after confirming with the user.
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
     * Loads all saved bookshelves into the table.
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
     * Copies the selected bookshelf into the form.
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
     * Reads and validates the form values.
     *
     * @return a new bookshelf, or null after displaying a validation message
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
     * Clears the form and removes the current table selection.
     */
    private void clearForm() {
        view.getBookshelfTable().getSelectionModel().clearSelection();
        clearFields();
    }

    private void clearFields() {
        view.getNameField().clear();
        view.getRoomField().clear();
        view.getShelfNumberField().clear();
    }

    private void showInfo(String message) {
        new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK).showAndWait();
    }

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
