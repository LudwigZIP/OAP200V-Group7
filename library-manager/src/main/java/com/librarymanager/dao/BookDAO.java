package com.librarymanager.dao;

// Imports the Book class, which represents a book in the application.
import com.librarymanager.model.Book;

import java.util.List;

/**
 * BookDAO describes the database operations for books.
 *
 * DAO stands for Data Access Object. Like WriterDAO and BookshelfDAO,
 * this interface only says WHAT can be done with books. The SQL that
 * actually does it is in BookDAOImpl. The controller only knows this
 * interface, so it doesn't depend on how the books are stored.
 *
 * All methods throw a RuntimeException if something goes wrong
 * in the database, the same way as the other DAOs in the project.
 *
 * @author Dior Cessay — responsible for BookDAO and BookDAOImpl.
 */
public interface BookDAO {

    /**
     * Saves a new book in the database, together with its writer,
     * genre and bookshelf. After saving, the Book object gets the
     * ID that the database generated.
     *
     * @param book the book to save (it must not have been saved before)
     */
    void save(Book book);

    /**
     * Finds a book in the database using its ID.
     *
     * @param id the ID of the book
     * @return the book, or null if no book with this ID exists
     */
    Book findById(int id);

    /**
     * Gets all books in the library, sorted by title.
     *
     * @return all books, or an empty list if there are none
     */
    List<Book> findAll();

    /**
     * Searches for books where the title contains the given text.
     *
     * @param text the text to search for
     * @return the books that match, sorted by title
     */
    List<Book> findByTitle(String text);

    /**
     * Gets all books written by a specific writer.
     *
     * @param writerId the ID of the writer
     * @return the writer's books, sorted by title
     */
    List<Book> findByWriter(int writerId);

    /**
     * Gets all books that belong to a specific genre.
     *
     * @param genreId the ID of the genre
     * @return the books in that genre, sorted by title
     */
    List<Book> findByGenre(int genreId);

    /**
     * Gets all books placed on a specific bookshelf.
     *
     * @param bookshelfId the ID of the bookshelf
     * @return the books on that shelf, sorted by title
     */
    List<Book> findByBookshelf(int bookshelfId);

    /**
     * Gets all books with a specific reading status,
     * for example all unread books.
     *
     * @param readingStatus "Unread", "Reading" or "Read"
     * @return the books with that status, sorted by title
     */
    List<Book> findByReadingStatus(String readingStatus);

    /**
     * Updates an existing book in the database: title, year, reading
     * status, writer, genre and bookshelf.
     *
     * @param book the book to update (it must have been saved before)
     */
    void update(Book book);

    /**
     * Deletes a book from the database using its ID.
     *
     * @param id the ID of the book to delete
     */
    void delete(int id);
}