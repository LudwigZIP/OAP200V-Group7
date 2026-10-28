package com.librarymanager.dao;

import com.librarymanager.model.Writer;
import com.librarymanager.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements database operations for writers.
 * 
 * @author Mia Bro Auganes
 */
public class WriterDAOImpl implements WriterDAO {

    /**
     * Creates a new writer in the database.
     *
     * @param writer the writer to create
     * @return the created writer with its generated ID
     */
    @Override
    public Writer create(Writer writer) {

        // SQL query to insert a new writer
        String sql = "INSERT INTO writer (first_name, last_name, nationality) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set the values for the SQL query
            statement.setString(1, writer.getFirstName());
            statement.setString(2, writer.getLastName());
            statement.setString(3, writer.getNationality());

            // Insert the writer into the database
            statement.executeUpdate();

            // Get the generated writer ID
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    writer.setId(keys.getInt(1));
                }
            }

            return writer;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create writer", e);
        }
    }

    /**
     * Updates an existing writer in the database.
     *
     * @param writer the writer to update
     */
    @Override
    public void updateWriter(Writer writer) {

        // SQL query to update a writer
        String sql = "UPDATE writer SET first_name = ?, last_name = ?, nationality = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the updated values
            statement.setString(1, writer.getFirstName());
            statement.setString(2, writer.getLastName());
            statement.setString(3, writer.getNationality());
            statement.setInt(4, writer.getId());

            // Update the writer in the database
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update writer", e);
        }
    }

    /**
     * Deletes a writer from the database.
     *
     * @param writer the writer to delete
     */
    @Override
    public void deleteWriter(Writer writer) {

        // SQL query to delete a writer
        String sql = "DELETE FROM writer WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the ID of the writer to delete
            statement.setInt(1, writer.getId());

            // Delete the writer from the database
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete writer", e);
        }
    }

    /**
     * Retrieves all writers from the database.
     *
     * @return a list of all writers
     */
    @Override
    public List<Writer> findAll() {

        // Create a list to store the writers
        List<Writer> writers = new ArrayList<>();

        // SQL query to retrieve all writers
        String sql = "SELECT id, first_name, last_name, nationality FROM writer";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            // Go through each row in the result
            while (resultSet.next()) {

                // Get the values from the database
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String nationality = resultSet.getString("nationality");

                // Create a writer object
                Writer writer = new Writer(id, firstName, lastName, nationality);

                // Add the writer to the list
                writers.add(writer);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve writers", e);
        }

        return writers;
    }

    /**
     * Checks if a writer with the same name already exists.
     */
    @Override
    public boolean existsWriter(String firstName, String lastName, Integer excludedId) {

        // SQL query to check if a writer already exists
        String sql = "SELECT 1 FROM writer WHERE first_name = ? AND last_name = ?"
                + (excludedId == null ? "" : " AND id <> ?")
                + " LIMIT 1";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the first and last name
            statement.setString(1, firstName);
            statement.setString(2, lastName);

            // Exclude the selected writer when updating
            if (excludedId != null) {
                statement.setInt(3, excludedId);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to check if writer exists", e);
        }
    }
}