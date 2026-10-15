package com.librarymanager.dao;

// Imports the Book class, which represents a book in the application.
import com.librarymanager.model.Book;

// Imports JDBC classes used to communicate with the database.
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * BookDAO handles communication between Book objects
 * and the book table in the database.
 *
 * DAO stands for Data Access Object.
 * This class contains methods to save, retrieve, update,
 * and delete books in the database.
 */
public class BookDAO {

    // Stores the database connection used by all methods in this class.
    // final means this variable cannot be assigned to another Connection
    // after it has been initialized in the constructor.
    private final Connection connection;

    /**
     * Constructor that receives an existing database connection.
     *
     * @param connection the connection to the database
     * @throws IllegalArgumentException if the connection is null
     */
    public BookDAO(Connection connection) {

        // Checks that a database connection was provided.
        // Without a connection, database queries cannot be executed.
        if (connection == null) {
            throw new IllegalArgumentException(
                    "Database connection cannot be null.");
        }

        // Stores the connection so the other methods can use it.
        this.connection = connection;
    }

    // CREATE: Saves a new book in the database.
    public void save(Book book) throws SQLException {

        // SQL statement that inserts a new book.
        // The question marks are placeholders for the values.
        String sql = "INSERT INTO book "
                + "(title, publication_year, reading_status) "
                + "VALUES (?, ?, ?)";

        // PreparedStatement allows values to be inserted safely.
        // RETURN_GENERATED_KEYS allows us to retrieve the generated ID.
        // try-with-resources automatically closes the statement.
        try (PreparedStatement statement =
                     connection.prepareStatement(
                             sql, Statement.RETURN_GENERATED_KEYS)) {

            // Sets the book title in the first placeholder.
            statement.setString(1, book.getTitle());

            // Sets the publication year in the second placeholder.
            // setObject also allows the value to be null.
            statement.setObject(2, book.getPublicationYear());

            // Sets the reading status in the third placeholder.
            statement.setString(3, book.getReadingStatus());

            // Executes the INSERT statement.
            // Returns the number of rows affected by the operation.
            int rows = statement.executeUpdate();

            // Throws an exception if no row was inserted.
            if (rows == 0) {
                throw new SQLException("Creating book failed.");
            }

            // Retrieves the ID generated automatically by the database.
            try (ResultSet keys = statement.getGeneratedKeys()) {

                // Checks whether the database returned a generated ID.
                if (keys.next()) {

                    // Assigns the generated ID to the Book object.
                    // The object now has the same ID as the database row.
                    book.setId(keys.getInt(1));

                } else {

                    // Throws an exception if no ID was returned.
                    throw new SQLException(
                            "Creating book failed: no ID returned.");
                }
            }
        }
    }

    // READ: Finds a book in the database using its ID.
    public Book findById(int id) throws SQLException {

        // SQL statement that selects a book with a specific ID.
        String sql = "SELECT id, title, publication_year, reading_status "
                + "FROM book WHERE id = ?";

        // Creates a prepared SQL statement.
        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            // Inserts the requested ID into the WHERE condition.
            statement.setInt(1, id);

            // Executes the SELECT query and receives the result.
            try (ResultSet result = statement.executeQuery()) {

                // Moves to the first result.
                // Returns false if no matching book was found.
                if (result.next()) {

                    // Retrieves the publication year from the result.
                    int yearValue = result.getInt("publication_year");

                    // getInt() returns 0 if the SQL value is NULL.
                    // wasNull() checks whether the value was actually NULL.
                    // Integer is used because it can represent null.
                    Integer year = result.wasNull() ? null : yearValue;

                    // Creates and returns a new Book object using
                    // the information retrieved from the database.
                    // The last three arguments are null because this
                    // query does not retrieve Writer, Genre, or Bookshelf.
                    return new Book(
                            result.getInt("id"),
                            result.getString("title"),
                            year,
                            result.getString("reading_status"),
                            null,
                            null,
                            null
                    );
                }
            }
        }

        // Returns null if no book with the requested ID exists.
        return null;
    }

    // UPDATE: Updates an existing book in the database.
    public void update(Book book) throws SQLException {

        // Updates the title, publication year, and reading status
        // of the book that matches the specified ID.
        String sql = "UPDATE book "
                + "SET title = ?, publication_year = ?, reading_status = ? "
                + "WHERE id = ?";

        // Creates a prepared SQL statement.
        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            // Sets the updated title.
            statement.setString(1, book.getTitle());

            // Sets the updated publication year.
            statement.setObject(2, book.getPublicationYear());

            // Sets the updated reading status.
            statement.setString(3, book.getReadingStatus());

            // Sets the ID that identifies which book to update.
            statement.setInt(4, book.getId());

            // Executes the UPDATE statement.
            int rows = statement.executeUpdate();

            // Throws an exception if no row was updated.
            // This may mean that the ID does not exist,
            // or that no database row was changed.
            if (rows == 0) {
                throw new SQLException(
                        "Book not found. ID: " + book.getId());
            }
        }
    }

    // DELETE: Deletes a book from the database using its ID.
    public void delete(int id) throws SQLException {

        // SQL statement that deletes the row with the specified ID.
        String sql = "DELETE FROM book WHERE id = ?";

        // Creates a prepared SQL statement.
        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            // Inserts the ID into the WHERE condition.
            statement.setInt(1, id);

            // Executes the DELETE statement.
            int rows = statement.executeUpdate();

            // Throws an exception if no row was deleted.
            // This usually means that no book with this ID was found.
            if (rows == 0) {
                throw new SQLException(
                        "Book not found. ID: " + id);
            }
        }
    }
}