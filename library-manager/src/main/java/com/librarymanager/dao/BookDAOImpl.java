package com.librarymanager.dao;

// Imports the model classes that this DAO works with.
import com.librarymanager.model.Book;
import com.librarymanager.model.Bookshelf;
import com.librarymanager.model.Genre;
import com.librarymanager.model.Writer;
import com.librarymanager.util.DatabaseConnection;

// Imports JDBC classes used to communicate with the database.
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

// Imports helper classes for lists and null checks.
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * BookDAOImpl contains all the SQL for the book table.
 *
 * It implements the BookDAO interface in the same way as WriterDAOImpl
 * and BookshelfDAOImpl: every method opens its own connection with
 * DatabaseConnection, runs one SQL statement with a PreparedStatement,
 * and closes everything again with try-with-resources.
 *
 * A book is linked to its writer, genre and bookshelf with the three
 * foreign keys writer_id, genre_id and bookshelf_id. They are saved in
 * every INSERT and UPDATE, and every SELECT joins the three tables
 * on these keys, so each Book object comes back with its Writer,
 * Genre and Bookshelf.
 *
 * The ? placeholders keep the values the user types separate from the
 * SQL, which protects the database against SQL injection.
 *
 * @author Dior Cessay — responsible for BookDAO and BookDAOImpl: saving,
 *         finding, searching, updating and deleting books in the database.
 */
public class BookDAOImpl implements BookDAO {

    /**
     * The SELECT that all the "find" methods start with.
     * b.* gets every column of the book, including its three foreign keys.
     * The JOINs follow those foreign keys to the writer, genre and
     * bookshelf of the book, so everything comes back in one query.
     * Columns with the same name in two tables get a new name with AS.
     */
    private static final String SELECT_BOOKS =
            "SELECT b.*, "
            + "w.first_name, w.last_name, w.nationality, "
            + "g.name AS genre_name, "
            + "s.name AS shelf_name, s.room, s.shelf_number "
            + "FROM book b "
            + "JOIN writer w ON w.id = b.writer_id "
            + "JOIN genre g ON g.genre_id = b.genre_id "
            + "JOIN bookshelf s ON s.bookshelf_id = b.bookshelf_id ";

    /**
     * Creates the DAO. It needs no settings, because every method
     * gets its own connection from DatabaseConnection.
     */
    public BookDAOImpl() {
    }


    // ===== CREATE =====

    /**
     * Saves a new book in the database, together with the IDs of its
     * writer, genre and bookshelf. After saving, the Book object gets
     * the ID that the database generated.
     *
     * @param book the book to save (it must not have been saved before)
     * @throws NullPointerException     if book is null
     * @throws IllegalArgumentException if the book already has an ID
     * @throws RuntimeException         if the book could not be saved
     */
    @Override
    public void save(Book book) {

        // Stops right away with a clear message if no book was given.
        Objects.requireNonNull(book, "book cannot be null");

        // A book that already has an ID is already in the database.
        // Saving it again would create a duplicate row.
        if (book.getId() != 0) {
            throw new IllegalArgumentException(
                    "Book " + book.getId() + " is already saved. Use update().");
        }

        // SQL statement that inserts a new book, including the
        // three foreign keys. The question marks are placeholders.
        String sql = "INSERT INTO book (title, publication_year, reading_status, "
                + "writer_id, genre_id, bookshelf_id) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        // RETURN_GENERATED_KEYS allows us to retrieve the generated ID.
        // try-with-resources automatically closes the connection and statement.
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            // Fills in the six placeholders with the book's values.
            setBookValues(statement, book);

            // Executes the INSERT statement and checks that a row was added.
            if (statement.executeUpdate() == 0) {
                throw new SQLException("No book was inserted.");
            }

            // Retrieves the ID generated automatically by the database.
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new SQLException("The database did not return a book ID.");
                }

                // The object now has the same ID as the database row.
                book.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save book", e);
        }
    }


    // ===== READ =====

    /**
     * Finds a book in the database using its ID.
     *
     * @param id the ID of the book
     * @return the book with its writer, genre and bookshelf,
     *         or null if no book with this ID exists
     */
    @Override
    public Book findById(int id) {
        List<Book> books = query(SELECT_BOOKS + "WHERE b.book_id = ?", id);

        // Returns null if no book with the requested ID exists.
        return books.isEmpty() ? null : books.get(0);
    }

    /**
     * Gets all books in the library, sorted by title.
     * BookController uses this to fill the book table.
     *
     * @return all books, or an empty list if there are none
     */
    @Override
    public List<Book> findAll() {
        return query(SELECT_BOOKS + "ORDER BY b.title");
    }

    /**
     * Searches for books where the title contains the given text.
     * For example, "harry" finds both Harry Potter books.
     *
     * @param text the text to search for
     * @return the books that match, sorted by title
     */
    @Override
    public List<Book> findByTitle(String text) {
        Objects.requireNonNull(text, "search text cannot be null");

        // The % signs mean "any text before or after", so LIKE
        // finds the search text anywhere in the title.
        return query(SELECT_BOOKS + "WHERE b.title LIKE ? ORDER BY b.title",
                "%" + text.trim() + "%");
    }

    /**
     * Gets all books written by a specific writer.
     *
     * @param writerId the ID of the writer
     * @return the writer's books, sorted by title
     */
    @Override
    public List<Book> findByWriter(int writerId) {
        return query(SELECT_BOOKS + "WHERE b.writer_id = ? ORDER BY b.title", writerId);
    }

    /**
     * Gets all books that belong to a specific genre.
     *
     * @param genreId the ID of the genre
     * @return the books in that genre, sorted by title
     */
    @Override
    public List<Book> findByGenre(int genreId) {
        return query(SELECT_BOOKS + "WHERE b.genre_id = ? ORDER BY b.title", genreId);
    }

    /**
     * Gets all books placed on a specific bookshelf.
     *
     * @param bookshelfId the ID of the bookshelf
     * @return the books on that shelf, sorted by title
     */
    @Override
    public List<Book> findByBookshelf(int bookshelfId) {
        return query(SELECT_BOOKS + "WHERE b.bookshelf_id = ? ORDER BY b.title", bookshelfId);
    }

    /**
     * Gets all books with a specific reading status.
     *
     * @param readingStatus "Unread", "Reading" or "Read"
     * @return the books with that status, sorted by title
     */
    @Override
    public List<Book> findByReadingStatus(String readingStatus) {
        return query(SELECT_BOOKS + "WHERE b.reading_status = ? ORDER BY b.title", readingStatus);
    }


    // ===== UPDATE =====

    /**
     * Updates an existing book in the database, including the
     * foreign keys to its writer, genre and bookshelf.
     *
     * @param book the book to update (it must have been saved before)
     * @throws NullPointerException     if book is null
     * @throws IllegalArgumentException if the book has never been saved
     * @throws RuntimeException         if no book has this ID, or the update fails
     */
    @Override
    public void update(Book book) {

        // Stops right away with a clear message if no book was given.
        Objects.requireNonNull(book, "book cannot be null");

        // A book without an ID has never been saved, so there is no row to update.
        if (book.getId() <= 0) {
            throw new IllegalArgumentException("Book must be saved before it can be updated.");
        }

        // Updates all the columns of the book with this ID,
        // including the three foreign keys.
        String sql = "UPDATE book SET title = ?, publication_year = ?, reading_status = ?, "
                + "writer_id = ?, genre_id = ?, bookshelf_id = ? "
                + "WHERE book_id = ?";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Sets the new values in placeholders 1 to 6,
            // and the ID of the book to update in placeholder 7.
            setBookValues(statement, book);
            statement.setInt(7, book.getId());

            // MySQL counts the rows that match the WHERE condition, so saving
            // a book without changes still returns 1. 0 means the ID doesn't exist.
            if (statement.executeUpdate() == 0) {
                throw new SQLException("Book not found. ID: " + book.getId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update book", e);
        }
    }


    // ===== DELETE =====

    /**
     * Deletes a book from the database using its ID.
     *
     * @param id the ID of the book to delete
     * @throws RuntimeException if no book has this ID, or the delete fails
     */
    @Override
    public void delete(int id) {

        // SQL statement that deletes the row with the specified ID.
        String sql = "DELETE FROM book WHERE book_id = ?";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Inserts the ID into the WHERE condition.
            statement.setInt(1, id);

            // 0 rows deleted usually means that no book with this ID was found.
            if (statement.executeUpdate() == 0) {
                throw new SQLException("Book not found. ID: " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete book", e);
        }
    }


    // ===== Helper methods =====

    /**
     * Opens a database connection, and gives a useful error if
     * DatabaseConnection could not connect.
     *
     * @return an open connection
     * @throws SQLException if there is no connection
     */
    private Connection openConnection() throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        if (connection == null) {
            throw new SQLException("Could not establish a database connection.");
        }
        return connection;
    }

    /**
     * Puts the book's values into placeholders 1 to 6. Both save() and
     * update() use this method, so they always write the same columns.
     *
     * @param statement the INSERT or UPDATE statement
     * @param book      the book to take the values from
     * @throws SQLException if a value cannot be set
     */
    private void setBookValues(PreparedStatement statement, Book book) throws SQLException {

        statement.setString(1, book.getTitle());

        // Types.INTEGER tells JDBC the column type, so null is saved as NULL.
        statement.setObject(2, book.getPublicationYear(), Types.INTEGER);
        statement.setString(3, book.getReadingStatus());

        // The three foreign keys. Book makes sure these objects are never null.
        statement.setInt(4, book.getWriter().getId());
        statement.setInt(5, book.getGenre().getId());
        statement.setInt(6, book.getBookshelf().getId());
    }

    /**
     * Runs a SELECT and turns every row into a Book object.
     * All the "find" methods use this method.
     *
     * @param sql        the SELECT statement with ? placeholders
     * @param parameters the values for the placeholders, in order
     * @return the books that were found, or an empty list
     * @throws RuntimeException if the query fails
     */
    private List<Book> query(String sql, Object... parameters) {
        List<Book> books = new ArrayList<>();

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Inserts the values into the placeholders, one by one.
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }

            // Executes the SELECT and turns every row into a Book.
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    books.add(readBook(result));
                }
            }

        } catch (SQLException | IllegalArgumentException e) {
            throw new RuntimeException("Failed to retrieve books", e);
        }

        return books;
    }

    /**
     * Creates a Book from the current row, together with its writer,
     * genre and bookshelf from the joined tables.
     *
     * @param result the result of a query that used SELECT_BOOKS
     * @return the book in the current row
     * @throws SQLException if a column cannot be read
     */
    private Book readBook(ResultSet result) throws SQLException {

        // Reads the writer from the joined writer table.
        // First and last name are stored separately, as in the database.
        Writer writer = new Writer(
                result.getInt("writer_id"),
                result.getString("first_name"),
                result.getString("last_name"),
                result.getString("nationality"));

        Genre genre = new Genre(
                result.getInt("genre_id"),
                result.getString("genre_name"));

        Bookshelf bookshelf = new Bookshelf(
                result.getInt("bookshelf_id"),
                result.getString("shelf_name"),
                result.getString("room"),
                getNullableInt(result, "shelf_number"));

        // Creates the Book. Its constructor checks all the values.
        Book book = new Book(
                result.getInt("book_id"),
                result.getString("title"),
                getNullableInt(result, "publication_year"),
                result.getString("reading_status"),
                writer,
                genre,
                bookshelf);


        return book;
    }

    /**
     * Reads a number column that can be NULL.
     * getInt() returns 0 for NULL, so wasNull() checks whether the
     * value was actually NULL.
     *
     * @param result the result, positioned on a row
     * @param column the name of the column
     * @return the value, or null if the column is NULL
     * @throws SQLException if the column cannot be read
     */
    private static Integer getNullableInt(ResultSet result, String column) throws SQLException {
        int value = result.getInt(column);
        return result.wasNull() ? null : value;
    }
}
