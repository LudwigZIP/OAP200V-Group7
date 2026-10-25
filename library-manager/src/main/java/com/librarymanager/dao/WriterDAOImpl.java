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
        String sql = "INSERT INTO writer (name, nationality) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set the values for the SQL query
            statement.setString(1, writer.getWriterName());
            statement.setString(2, writer.getWriterNationality());

            // Insert the writer into the database
            statement.executeUpdate();

            // Get the generated writer ID
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    writer.setWriterId(keys.getInt(1));
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
        String sql = "UPDATE writer SET name = ?, nationality = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the updated values
            statement.setString(1, writer.getWriterName());
            statement.setString(2, writer.getWriterNationality());
            statement.setInt(3, writer.getWriterId());

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
            statement.setInt(1, writer.getWriterId());

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
        String sql = "SELECT id, name, nationality FROM writer";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            // Go through each row in the result
            while (resultSet.next()) {

                // Get the values from the database
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String nationality = resultSet.getString("nationality");

                // Create a writer object
                Writer writer = new Writer(id, name, nationality);

                // Add the writer to the list
                writers.add(writer);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve writers", e);
        }

        return writers;
    }
}