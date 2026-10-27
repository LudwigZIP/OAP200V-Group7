package com.librarymanager.dao;

import com.librarymanager.model.Bookshelf;
import com.librarymanager.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implements database operations for bookshelves.
 *
 * @author Thomas Mølnvik
 */
public class BookshelfDAOImpl implements BookshelfDAO {

    /**
     * Creates a new bookshelf in the database.
     *
     * @param bookshelf the bookshelf to create
     * @return the created bookshelf with its generated ID
     */
    @Override
    public Bookshelf create(Bookshelf bookshelf) {
        String sql = "INSERT INTO bookshelf "
                + "(name, room, shelf_number) "
                + "VALUES (?, ?, ?)";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            setBookshelfValues(statement, bookshelf);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new SQLException("The database did not return a bookshelf ID.");
                }
                bookshelf.setId(keys.getInt(1));
            }

            return bookshelf;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create bookshelf", e);
        }
    }

    /**
     * Retrieves a bookshelf by its ID.
     *
     * @param id the ID of the bookshelf to retrieve
     * @return an optional containing the bookshelf, or empty if it is not found
     */
    @Override
    public Optional<Bookshelf> findById(int id) {
        String sql = "SELECT bookshelf_id, name, room, shelf_number "
                + "FROM bookshelf WHERE bookshelf_id = ?";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(readBookshelf(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve bookshelf", e);
        }
    }

    /**
     * Checks whether another bookshelf uses the given shelf number.
     *
     * @param shelfNumber the shelf number to check
     * @param excludedBookshelfId the bookshelf ID to ignore, or null
     * @return true if another bookshelf uses the shelf number
     */
    @Override
    public boolean isShelfNumberInUse(int shelfNumber, Integer excludedBookshelfId) {
        String sql = "SELECT 1 FROM bookshelf WHERE shelf_number = ?"
                + (excludedBookshelfId == null ? "" : " AND bookshelf_id <> ?")
                + " LIMIT 1";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, shelfNumber);
            if (excludedBookshelfId != null) {
                statement.setInt(2, excludedBookshelfId);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check shelf number", e);
        }
    }

    /**
     * Retrieves all bookshelves from the database.
     *
     * @return a list of all bookshelves
     */
    @Override
    public List<Bookshelf> findAll() {
        String sql = "SELECT bookshelf_id, name, room, shelf_number "
                + "FROM bookshelf ORDER BY name";
        List<Bookshelf> bookshelves = new ArrayList<>();

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                bookshelves.add(readBookshelf(resultSet));
            }
            return bookshelves;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve bookshelves", e);
        }
    }

    /**
     * Updates an existing bookshelf in the database.
     *
     * @param bookshelf the bookshelf to update
     */
    @Override
    public void update(Bookshelf bookshelf) {
        String sql = "UPDATE bookshelf SET name = ?, room = ?, "
                + "shelf_number = ? WHERE bookshelf_id = ?";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            setBookshelfValues(statement, bookshelf);
            statement.setInt(4, bookshelf.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update bookshelf", e);
        }
    }

    /**
     * Deletes a bookshelf from the database.
     *
     * @param id the ID of the bookshelf to delete
     */
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM bookshelf WHERE bookshelf_id = ?";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete bookshelf", e);
        }
    }

    /**
     * Opens a connection to the database.
     *
     * @return the database connection
     * @throws SQLException if a connection cannot be established
     */
    private Connection openConnection() throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        if (connection == null) {
            throw new SQLException("Could not establish a database connection.");
        }
        return connection;
    }

    /**
     * Sets the bookshelf values in a prepared statement.
     *
     * @param statement the prepared statement
     * @param bookshelf the bookshelf to use
     * @throws SQLException if a database error occurs
     */
    private void setBookshelfValues(PreparedStatement statement, Bookshelf bookshelf)
            throws SQLException {
        statement.setString(1, bookshelf.getName());
        statement.setString(2, bookshelf.getRoom());
        if (bookshelf.getShelfNumber() == null) {
            statement.setNull(3, Types.INTEGER);
        } else {
            statement.setInt(3, bookshelf.getShelfNumber());
        }
    }

    /**
     * Creates a bookshelf object from the current database row.
     *
     * @param resultSet the result set containing the bookshelf data
     * @return the bookshelf created from the row
     * @throws SQLException if a database error occurs
     */
    private Bookshelf readBookshelf(ResultSet resultSet) throws SQLException {
        int shelfNumberValue = resultSet.getInt("shelf_number");
        Integer shelfNumber = resultSet.wasNull() ? null : shelfNumberValue;

        return new Bookshelf(
                resultSet.getInt("bookshelf_id"),
                resultSet.getString("name"),
                resultSet.getString("room"),
                shelfNumber);
    }
}
