package com.librarymanager.dao;

import com.librarymanager.model.Genre;
import com.librarymanager.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) implementation for the Genre entity.
 *
 * This class manages all database operations related to genres in the
 * Library Manager application. It provides implementations of the
 * standard CRUD operations (Create, Read, Update, Delete) defined
 * in the GenreDAO interface.
 *
 * The class communicates directly with the database using JDBC and
 * uses prepared statements to execute SQL queries safely and efficiently.
 * Database connections are obtained through the DatabaseConnection
 * utility class, and database records are mapped to Genre objects.
 *
 * Key responsibilities:
 * - Create new genres in the database
 * - Retrieve genres by ID or retrieve all genres
 * - Update existing genre information
 * - Delete genres from the database
 * - Convert database result sets into Genre objects
 * - Manage database connections and SQL exception handling
 *
 * The implementation follows the DAO pattern, separating data access
 * logic from the rest of the application to improve maintainability,
 * readability, and testability.
 * 
 * @author: Erika Marie Gåsland
 */

/**
 * Implements the GenreDAO interface.
 *
 * This class is responsible for communicating with the database.
 * It contains the SQL needed to create, read, update, and delete genres.
 */
public class GenreDAOImpl implements GenreDAO {

    /**
     * Creates a new genre in the database (CREATE).
     *
     * @param genre the genre to create
     * @return the created genre with its generated ID
     */
    @Override
    public Genre create(Genre genre) {
        // The database generates genre_id automatically, so only name is inserted.
        String sql = "INSERT INTO genre (name) VALUES (?)";

        // try-with-resources closes the connection and statement automatically.
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            // Replace the ? in the SQL query with the genre name.
            statement.setString(1, genre.getName());
            // Execute the INSERT statement.
            statement.executeUpdate();

            // Read the ID that the database generated for the new row.
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new SQLException("The database did not return a genre ID.");
                }
                // Store the generated ID in the Java object.
                genre.setId(keys.getInt(1));
            }

            // Return the same object, now including its database ID.
            return genre;
        } catch (SQLException e) {
            // Convert the database error into an unchecked application error.
            throw new RuntimeException("Failed to create genre", e);
        }
    }

    /**
     * Retrieves one genre by its ID (READ).
     *
     * @param id the ID of the genre to retrieve
     * @return the genre, or null if it is not found
     */
    @Override
    public Genre findById(int id) {
        // ? is used as a parameter instead of adding id directly to the SQL.
        String sql = "SELECT genre_id, name FROM genre WHERE genre_id = ?";

        // try-with-resources ensures that the connection and statement are closed automatically.
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the ID that should be searched for.
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                // A ResultSet points to rows returned by the query.
                // Return null when no genre has this ID.
                return resultSet.next() ? readGenre(resultSet) : null;
            }
            // The ResultSet is closed automatically by the try-with-resources block.
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve genre", e);
        }
    }

    /**
     * Retrieves all genres from the database (READ).
     *
     * @return a list of all genres
     */
    @Override
    public List<Genre> findAll() {
        // Sort the result alphabetically by genre name for predictable display.
        String sql = "SELECT genre_id, name FROM genre ORDER BY name";
        List<Genre> genres = new ArrayList<>();

        // try-with-resources ensures that the connection, statement, and result set are closed automatically.
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            // Read each database row and convert it into a Genre object.
            while (resultSet.next()) {
                genres.add(readGenre(resultSet));
            }
            // Return an empty list if the table has no rows.
            return genres;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve genres", e);
        }
    }

    /**
     * Updates an existing genre in the database (UPDATE).
     *
     * @param genre the genre to update
     */
    @Override
    public void update(Genre genre) {
        // Change only the name belonging to the specified genre_id.
        String sql = "UPDATE genre SET name = ? WHERE genre_id = ?";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the values for the two ? parameters in the correct order.
            statement.setString(1, genre.getName());
            statement.setInt(2, genre.getId());
            // Execute the UPDATE statement.
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update genre", e);
        }
    }

    /**
     * Deletes a genre from the database (DELETE).
     *
     * @param id the ID of the genre to delete
     */
    @Override
    public void delete(int id) {
        // Delete the row with the matching primary key.
        String sql = "DELETE FROM genre WHERE genre_id = ?";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the ID that identifies the genre to delete.
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete genre", e);
        }
    }

    /**
     * Opens a database connection through the shared connection helper.
     *
     * @return an open database connection
     * @throws SQLException if no connection can be established
     */
    private Connection openConnection() throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        if (connection == null) {
            // DatabaseConnection reports the original connection problem.
            // Throwing here prevents the DAO from continuing with a null connection.
            throw new SQLException("Could not establish a database connection.");
        }
        return connection;
    }

    /**
     * Converts the current row in a ResultSet into a Genre object.
     *
     * @param resultSet the result returned by a SELECT query
     * @return a Genre containing the row's ID and name
     * @throws SQLException if a column cannot be read
     */
    private Genre readGenre(ResultSet resultSet) throws SQLException {
        // The column names must match the names in the genre database table.
        return new Genre(
                resultSet.getInt("genre_id"),
                resultSet.getString("name"));
    }
}
