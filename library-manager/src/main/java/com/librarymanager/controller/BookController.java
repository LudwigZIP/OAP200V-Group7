package com.librarymanager.controller;

import com.librarymanager.dao.BookDAO;
import com.librarymanager.model.Book;
import com.librarymanager.view.BookView;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Handles adding, viewing, updating, deleting and searching books.
 *
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
     * Connects the table, buttons, search field and row selection.
     */
    private void initialize() {

        // Displays the controller's book list in the table.
        view.getBookTable().setItems(bookList);

        // Connects the book action buttons.
        view.getAddButton().setOnAction(event -> addBook());
        view.getUpdateButton().setOnAction(event -> updateBook());
        view.getDeleteButton().setOnAction(event -> deleteBook());

        // Searches when the Search button is clicked.
        view.getSearchButton().setOnAction(event -> searchBooks());

        // Also searches when Enter is pressed in the search field.
        view.getSearchField().setOnAction(event -> searchBooks());

        // Displays the selected book in the input fields.
        view.getBookTable().getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldBook, selectedBook) -> {
                    if (selectedBook != null) {
                        populateForm(selectedBook);
                    }
                });

        // Loads registered books when the controller starts.
        loadBooks();
    }

    /**
     * Displays the selected book in the input fields.
     *
     * @param book the selected book
     */
    private void populateForm(Book book) {
        view.getTitleField().setText(book.getTitle());

        // Displays an empty field when the year is unknown.
        Integer year = book.getPublicationYear();
        view.getYearField().setText(
                year == null ? "" : year.toString());

        // Selects the current status and related objects.
        view.getStatusField().setValue(book.getReadingStatus());
        view.getWriterField().setValue(book.getWriter());
        view.getGenreField().setValue(book.getGenre());
        view.getBookshelfField().setValue(book.getBookshelf());
    }

    /**
     * Adds a new book using the input fields.
     */
    private void addBook() {

        // Creates a new book from the form values.
        Book book = new Book(
                view.getTitleField().getText(),
                readYear(),
                view.getStatusField().getValue(),
                view.getWriterField().getValue(),
                view.getGenreField().getValue(),
                view.getBookshelfField().getValue());

        // Saves the book and assigns its generated database ID.
        bookDAO.save(book);

        // Reloads the complete collection.
        loadBooks();
    }

    /**
     * Loads all books and resets the search and input form.
     */
    public void loadBooks() {
        List<Book> books = bookDAO.findAll();

        // Replaces the displayed list with all registered books.
        bookList.setAll(books);

        // Clears the search because the complete list is displayed.
        view.getSearchField().clear();

        clearForm();
    }

    /**
     * Searches for books whose titles contain the entered text.
     * An empty search displays all books.
     */
    private void searchBooks() {

        // Removes whitespace before and after the search text.
        String text = view.getSearchField().getText().trim();

        if (text.isEmpty()) {
            loadBooks();
            return;
        }

        // Retrieves matching books from the database.
        List<Book> results = bookDAO.findByTitle(text);

        // Displays only the matching books.
        bookList.setAll(results);

        // Removes the previous selection and its form values.
        clearForm();
    }

    /**
     * Updates the selected book using the input fields.
     */
    private void updateBook() {
        Book selectedBook = view.getBookTable()
                .getSelectionModel().getSelectedItem();

        if (selectedBook != null) {

            // Changes the selected book's values.
            selectedBook.setTitle(view.getTitleField().getText());
            selectedBook.setPublicationYear(readYear());
            selectedBook.setReadingStatus(
                    view.getStatusField().getValue());
            selectedBook.setWriter(view.getWriterField().getValue());
            selectedBook.setGenre(view.getGenreField().getValue());
            selectedBook.setBookshelf(
                    view.getBookshelfField().getValue());

            // Saves the changes and reloads the collection.
            bookDAO.update(selectedBook);
            loadBooks();
        }
    }

    /**
     * Deletes the selected book.
     */
    private void deleteBook() {
        Book selectedBook = view.getBookTable()
                .getSelectionModel().getSelectedItem();

        if (selectedBook != null) {

            // Deletes the database record using the book ID.
            bookDAO.delete(selectedBook.getId());

            // Reloads the remaining books.
            loadBooks();
        }
    }

    /**
     * Reads the optional publication year.
     *
     * @return the year, or null if the field is empty
     */
    private Integer readYear() {
        String text = view.getYearField().getText().trim();

        if (text.isEmpty()) {
            return null;
        }

        // Converts the entered text to an integer.
        return Integer.valueOf(text);
    }

    /**
     * Clears the table selection and input fields.
     */
    private void clearForm() {
        view.getBookTable().getSelectionModel().clearSelection();

        view.getTitleField().clear();
        view.getYearField().clear();

        // Uses the same status value as the dropdown list.
        view.getStatusField().setValue("Unread");

        view.getWriterField().setValue(null);
        view.getGenreField().setValue(null);
        view.getBookshelfField().setValue(null);
    }
}
