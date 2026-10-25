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
 * JDBC implementation of the bookshelf data-access contract.
 */
public class BookshelfDAOImpl implements BookshelfDAO {

    /**
     * Inserts a bookshelf and assigns the generated database ID to it.
     *
     * @param bookshelf the bookshelf to save
     * @return the saved bookshelf
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
     * Finds a bookshelf by its primary key.
     *
     * @param id the bookshelf ID
     * @return the matching bookshelf, or an empty optional when it does not exist
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
     * Retrieves every bookshelf, ordered by name.
     *
     * @return all saved bookshelves
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
     * Updates the stored details for an existing bookshelf.
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
     * Deletes a bookshelf by its ID.
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
     * Opens a database connection and reports a useful failure if unavailable.
     */
    private Connection openConnection() throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        if (connection == null) {
            throw new SQLException("Could not establish a database connection.");
        }
        return connection;
    }

    /**
     * Binds the bookshelf's name, room, and optional number to an SQL statement.
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
     * Creates a Bookshelf from the current row in the result set.
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
