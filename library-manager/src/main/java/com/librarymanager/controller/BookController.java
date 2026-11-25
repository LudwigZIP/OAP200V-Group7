package com.librarymanager.controller;

import com.librarymanager.dao.BookDAO;
import com.librarymanager.model.Book;
import com.librarymanager.model.Writer;
import com.librarymanager.view.BookView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

/**
 * Handles book operations, search, filtering, input validation
 * and duplicate warnings.
 *
 * @author Ulrik Marker
 */
public class BookController {

    private final BookDAO bookDAO;
    private final BookView view;

    // Complete collection and the books currently displayed.
    private final List<Book> allBooks = new ArrayList<>();
    private final ObservableList<Book> bookList =
            FXCollections.observableArrayList();

    public BookController(BookView view, BookDAO bookDAO) {
        this.view = view;
        this.bookDAO = bookDAO;
        initialize();
    }

    /**
     * Connects the view controls to controller actions.
     */
    private void initialize() {
        view.getBookTable().setItems(bookList);

        view.getAddButton().setOnAction(event -> addBook());
        view.getUpdateButton().setOnAction(event -> updateBook());
        view.getDeleteButton().setOnAction(event -> deleteBook());
        view.getClearButton().setOnAction(event -> clearAll());

        // Search is performed by clicking Search or pressing Enter.
        view.getSearchButton().setOnAction(event -> searchBooks());
        view.getSearchField().setOnAction(event -> searchBooks());
        view.getFilterYearField().setOnAction(event -> searchBooks());

        view.getBookTable().getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldBook, selectedBook) -> {
                    if (selectedBook != null) {
                        populateForm(selectedBook);
                    }
                });

        loadBooks();
    }

    /**
     * Loads books without clearing the form or filter controls.
     * Previously displayed results remain if loading fails.
     */
    public void loadBooks() {
        try {
            List<Book> loadedBooks = bookDAO.findAll();

            allBooks.clear();
            allBooks.addAll(loadedBooks);
            bookList.setAll(allBooks);
        } catch (RuntimeException exception) {
            showMessage(
                    Alert.AlertType.ERROR,
                    "Loading failed",
                    "Books could not be loaded. Check the database connection.");
        }
    }

    /**
     * Displays a selected book in the input form.
     */
    private void populateForm(Book book) {
        view.getTitleField().setText(book.getTitle());

        Integer year = book.getPublicationYear();
        view.getYearField().setText(
                year == null ? "" : year.toString());

        view.getStatusField().setValue(book.getReadingStatus());
        view.getWriterField().setValue(book.getWriter());
        view.getGenreField().setValue(book.getGenre());
        view.getBookshelfField().setValue(book.getBookshelf());
    }

    /**
     * Validates the form and returns a new Book object.
     * Does not modify the selected book.
     */
    private Book readBookForm() {
        String title = view.getTitleField().getText().trim();

        if (title.isEmpty()) {
            throw new IllegalArgumentException(
                    "Title is required. Enter a book title.");
        }

        Integer year = parseYear(
                view.getYearField().getText(), "Publication year");

        String status = view.getStatusField().getValue();

        if (!"Unread".equals(status)
                && !"Reading".equals(status)
                && !"Read".equals(status)) {
            throw new IllegalArgumentException(
                    "Reading status is required. Select Unread, Reading or Read.");
        }

        if (view.getWriterField().getValue() == null) {
            throw new IllegalArgumentException(
                    "Writer is required. Select a writer.");
        }

        if (view.getGenreField().getValue() == null) {
            throw new IllegalArgumentException(
                    "Genre is required. Select a genre.");
        }

        if (view.getBookshelfField().getValue() == null) {
            throw new IllegalArgumentException(
                    "Bookshelf is required. Select a bookshelf.");
        }

        // The model also applies its own validation.
        return new Book(
                title,
                year,
                status,
                view.getWriterField().getValue(),
                view.getGenreField().getValue(),
                view.getBookshelfField().getValue());
    }

    /**
     * Reads an optional positive integer year.
     */
    private Integer parseYear(String input, String fieldName) {
        String text = input.trim();

        if (text.isEmpty()) {
            return null;
        }

        try {
            int year = Integer.parseInt(text);

            if (year <= 0) {
                throw new IllegalArgumentException(
                        fieldName + " must be greater than zero.");
            }

            return year;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    fieldName
                            + " must be a valid whole number, for example 1997.");
        }
    }

    /**
     * Adds a book after validation and duplicate confirmation.
     */
    private void addBook() {
        Book book;

        try {
            book = readBookForm();
        } catch (IllegalArgumentException exception) {
            showValidationError(exception.getMessage());
            return;
        }

        List<Book> existingBooks;

        try {
            // Checks fresh database data, including books hidden by filters.
            existingBooks = bookDAO.findAll();
        } catch (RuntimeException exception) {
            showMessage(
                    Alert.AlertType.ERROR,
                    "Duplicate check failed",
                    "Existing books could not be checked. "
                            + "Check the database connection and try again.");
            return;
        }

        for (Book existing : existingBooks) {

            // Ignores letter case and surrounding whitespace.
            boolean sameTitle = existing.getTitle().trim()
                    .equalsIgnoreCase(book.getTitle().trim());

            // Compares writer IDs rather than object references.
            boolean sameWriter = existing.getWriter() != null
                    && existing.getWriter().getId()
                    == book.getWriter().getId();

            if (sameTitle && sameWriter) {

                // Cancellation preserves the form and stops the save.
                if (!confirmDuplicate(book)) {
                    return;
                }

                // Asks only once, even if multiple copies already exist.
                break;
            }
        }

        try {
            bookDAO.save(book);
        } catch (RuntimeException exception) {
            showMessage(
                    Alert.AlertType.ERROR,
                    "Save failed",
                    "The save operation could not be completed. "
                            + "Check the database before trying again.");
            return;
        }

        clearAll();
    }

    /**
     * Asks whether to add a book with an existing title and writer.
     *
     * @param book the book being added
     * @return true if the user chooses Add anyway
     */
    private boolean confirmDuplicate(Book book) {
        ButtonType addAnywayButton = new ButtonType(
                "Add anyway", ButtonBar.ButtonData.OK_DONE);

        ButtonType cancelButton = new ButtonType(
                "Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert warning = new Alert(
                Alert.AlertType.WARNING,
                "A book with the title \"" + book.getTitle()
                        + "\" by " + book.getWriter()
                        + " is already registered.\n\n"
                        + "Do you still want to add this book?",
                addAnywayButton,
                cancelButton);

        warning.setTitle("Possible duplicate");
        warning.setHeaderText("This book may already be registered");
        setAlertOwner(warning);

        // Makes Cancel the default action when Enter is pressed.
        Button addButton = (Button) warning.getDialogPane()
                .lookupButton(addAnywayButton);
        addButton.setDefaultButton(false);

        Button cancel = (Button) warning.getDialogPane()
                .lookupButton(cancelButton);
        cancel.setDefaultButton(true);

        // Closing the dialog also cancels the operation.
        return warning.showAndWait().orElse(cancelButton)
                == addAnywayButton;
    }

    /**
     * Updates a selected book after validating all input.
     */
    private void updateBook() {
        Book selectedBook = view.getBookTable()
                .getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            showValidationError(
                    "Select a book in the table before updating.");
            return;
        }

        Book updatedBook;

        try {
            updatedBook = readBookForm();

            // Keeps the database ID without changing the original row.
            updatedBook.setId(selectedBook.getId());
        } catch (IllegalArgumentException exception) {
            showValidationError(exception.getMessage());
            return;
        }

        try {
            bookDAO.update(updatedBook);
        } catch (RuntimeException exception) {
            showMessage(
                    Alert.AlertType.ERROR,
                    "Update failed",
                    "The book could not be updated. "
                            + "Check the database connection and try again.");
            return;
        }

        clearAll();
    }

    /**
     * Deletes a selected book only after explicit confirmation.
     */
    private void deleteBook() {
        Book selectedBook = view.getBookTable()
                .getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            showValidationError(
                    "Select a book in the table before deleting.");
            return;
        }

        ButtonType deleteButton = new ButtonType(
                "Delete", ButtonBar.ButtonData.OK_DONE);

        ButtonType cancelButton = new ButtonType(
                "Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete \""
                        + selectedBook.getTitle() + "\"?",
                deleteButton,
                cancelButton);

        confirmation.setTitle("Delete book");
        confirmation.setHeaderText("Confirm deletion");
        setAlertOwner(confirmation);

        // Cancel and closing the dialog leave the book unchanged.
        if (confirmation.showAndWait().orElse(cancelButton)
                != deleteButton) {
            return;
        }

        try {
            bookDAO.delete(selectedBook.getId());
        } catch (RuntimeException exception) {
            showMessage(
                    Alert.AlertType.ERROR,
                    "Delete failed",
                    "The book could not be deleted. "
                            + "Check the database connection and try again.");
            return;
        }

        clearAll();
    }

    /**
     * Combines optional title, writer and exact-year criteria.
     * Filtering does not clear the book input form.
     */
    private void searchBooks() {
        Integer year;

        try {
            year = parseYear(
                    view.getFilterYearField().getText(),
                    "Search year");
        } catch (IllegalArgumentException exception) {
            showValidationError(exception.getMessage());
            return;
        }

        String title = view.getSearchField().getText()
                .trim().toLowerCase(Locale.ROOT);

        Writer writer = view.getWriterFilter().getValue();
        List<Book> results = new ArrayList<>();

        for (Book book : allBooks) {
            boolean matchesTitle = book.getTitle()
                    .toLowerCase(Locale.ROOT).contains(title);

            // Compares database IDs rather than object references.
            boolean matchesWriter = writer == null
                    || (book.getWriter() != null
                    && book.getWriter().getId() == writer.getId());

            boolean matchesYear = year == null
                    || year.equals(book.getPublicationYear());

            if (matchesTitle && matchesWriter && matchesYear) {
                results.add(book);
            }
        }

        // Clears the selection before replacing results.
        // Existing form input is kept.
        view.getBookTable().getSelectionModel().clearSelection();
        bookList.setAll(results);
    }

    /**
     * Resets search, filters and the form, then reloads all books.
     * Does not delete any database records.
     */
    private void clearAll() {
        view.getSearchField().clear();
        view.getFilterYearField().clear();
        view.getWriterFilter().setValue(null);

        clearForm();
        loadBooks();
    }

    /**
     * Clears the selected row and book input controls.
     */
    private void clearForm() {
        view.getBookTable().getSelectionModel().clearSelection();

        view.getTitleField().clear();
        view.getYearField().clear();
        view.getStatusField().setValue("Unread");
        view.getWriterField().setValue(null);
        view.getGenreField().setValue(null);
        view.getBookshelfField().setValue(null);
    }

    /**
     * Displays instructions for correcting invalid input.
     */
    private void showValidationError(String message) {
        showMessage(
                Alert.AlertType.WARNING, "Invalid input", message);
    }

    /**
     * Displays feedback in an application-owned dialog.
     */
    private void showMessage(
            Alert.AlertType type, String title, String message) {

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        setAlertOwner(alert);
        alert.showAndWait();
    }

    /**
     * Associates a dialog with the application window when available.
     */
    private void setAlertOwner(Alert alert) {
        if (view.getScene() != null
                && view.getScene().getWindow() != null) {
            alert.initOwner(view.getScene().getWindow());
        }
    }
}
