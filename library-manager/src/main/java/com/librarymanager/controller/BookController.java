package com.librarymanager.controller;

import com.librarymanager.dao.BookDAO;
import com.librarymanager.model.Book;
import com.librarymanager.view.BookView;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Handles adding, viewing, updating and deleting books.
 * @author Ulrik Marker
 */
public class BookController {

    // DAO used to access book data.
    private final BookDAO bookDAO;

    // Reference to the book view.
    private final BookView view;

    // List of books displayed in the table.
    private final ObservableList<Book> bookList;

    /**
     * Creates a controller with access to the view and DAO.
     *
     * @param view the book view
     * @param bookDAO the DAO used to manage books
     */
    public BookController(BookView view, BookDAO bookDAO) {
        this.view = view;
        this.bookDAO = bookDAO;
        this.bookList = FXCollections.observableArrayList();
        initialize();
    }
    /**
     * Connects the book list, buttons and table selection.
     */
    private void initialize() {

        // Displays the controller's book list in the table.
        view.getBookTable().setItems(bookList);

        // Calls addBook() when the Add button is clicked.
        view.getAddButton().setOnAction(event -> addBook());

        // Calls updateBook() when the Update button is clicked.
        view.getUpdateButton().setOnAction(event -> updateBook());

        // Calls deleteBook() when the Delete button is clicked.
        view.getDeleteButton().setOnAction(event -> deleteBook());

        // Copies the selected book's values into the form.
        view.getBookTable().getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldBook, selectedBook) -> {

                    // Only fills the form when a book is selected.
                    if (selectedBook != null) {
                        populateForm(selectedBook);
                    }
                });

        // Loads registered books from the database.
        loadBooks();
    }

    /**
     * Displays the selected book in the input fields.
     *
     * @param book the selected book
     */
    private void populateForm(Book book) {

        // Displays the book title.
        view.getTitleField().setText(book.getTitle());

        // Reads the optional publication year.
        Integer year = book.getPublicationYear();

        // Displays an empty field when the year is unknown.
        view.getYearField().setText(
                year == null ? "" : year.toString());

        // Selects the book's current reading status.
        view.getStatusField().setValue(book.getReadingStatus());

        // Selects the related writer, genre and bookshelf.
        view.getWriterField().setValue(book.getWriter());
        view.getGenreField().setValue(book.getGenre());
        view.getBookshelfField().setValue(book.getBookshelf());
    }

    /**
     *Adds a new book using the input fields.
     */
    private void addBook() {

        // Reads the input fields and creates a new book.
        Book book = new Book(
                view.getTitleField().getText(),
                readYear(),
                view.getStatusField().getValue(),
                view.getWriterField().getValue(),
                view.getGenreField().getValue(),
                view.getBookshelfField().getValue());

        // Saves the book and lets the DAO assign its database ID.
        // bookDAO.save(book);

        // Adds the saved book to the displayed list.
        bookList.add(book);

        // Clears the form after adding the book.
        clearForm();
    }

    /**
     *Loads registered books into the table.
     */
    public void loadBooks() {

        // Retrieves the complete book list from the DAO.
        // List<Book> books = bookDAO.findAll();

        // Replaces the contents of the observable list.
        // bookList.setAll(books);
    }

    /**
     *Updates the selected book using the input fields.
     */
    private void updateBook() {

        // Retrieves the currently selected book.
        Book selectedBook = view.getBookTable()
                .getSelectionModel().getSelectedItem();

        // Continues only if a book is selected.
        if (selectedBook != null) {

            // Updates the selected book's title and publication year.
            selectedBook.setTitle(view.getTitleField().getText());
            selectedBook.setPublicationYear(readYear());

            // Updates the reading status.
            selectedBook.setReadingStatus(
                    view.getStatusField().getValue());

            // Updates the related writer, genre and bookshelf.
            selectedBook.setWriter(view.getWriterField().getValue());
            selectedBook.setGenre(view.getGenreField().getValue());
            selectedBook.setBookshelf(
                    view.getBookshelfField().getValue());

            // Saves the changes to the database.
            // bookDAO.update(selectedBook);

            // Reloads the books in the table.
            loadBooks();

            // Clears the form and selection.
            clearForm();
        }
    }

    /**
     *Deletes the selected book.
     */
    private void deleteBook() {

        // Retrieves the currently selected book.
        Book selectedBook = view.getBookTable()
                .getSelectionModel().getSelectedItem();

        // Continues only if a book is selected.
        if (selectedBook != null) {

            // Deletes the database record using the book's ID.
            // bookDAO.delete(selectedBook.getId());

            // Reloads the remaining books.
            loadBooks();

            // Clears the form and selection.
            clearForm();
        }
    }

    /**
     * Reads the optional publication year.
     *
     * @return the year, or null if the field is empty
     */
    private Integer readYear() {

        // Reads the text and removes surrounding whitespace.
        String text = view.getYearField().getText().trim();

        // An empty field represents an unknown publication year.
        if (text.isEmpty()) {
            return null;
        }

        // Converts the text to an integer.
        // Non-numeric input causes a runtime error.
        return Integer.valueOf(text);
    }

    /**
     * Clears the form after adding, updating or deleting a book.
     */
    private void clearForm() {

        // Removes the current table selection.
        view.getBookTable().getSelectionModel().clearSelection();

        // Clears the text fields.
        view.getTitleField().clear();
        view.getYearField().clear();

        // Restores the default reading status.
        view.getStatusField().setValue("unread");

        // Clears the related object selections.
        view.getWriterField().setValue(null);
        view.getGenreField().setValue(null);
        view.getBookshelfField().setValue(null);
    }
}
