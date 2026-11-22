package com.librarymanager.dao;

import com.librarymanager.model.Writer;
import java.util.List;

/**
 * DAO interface for managing writer data in the Library Manager application.
 *
 * This interface defines methods for creating, updating, deleting
 * and retrieving writers, as well as checking for duplicate names.
 * The actual database operations are handled by the class
 * that implements this interface.
 *
 * @author Mia Bro Auganes
 */

public interface WriterDAO {

    /**
     * Creates a new writer in the database
     * 
     * @param writer the writer to create
     * @return the created writer
     */
    Writer create(Writer writer);

    /**
     * Updates an existing writer in the database
     * 
     * @param writer the writer to update
     */
    void updateWriter(Writer writer);

    /**
     * Deletes a writer in the database
     * 
     * @param writer the writer to delete
     */
    void deleteWriter(Writer writer);

    /**
     * Retrieves all the writers from the database
     * 
     * @return a list of all writers
     */
    List<Writer> findAll();

    /**
     * Checks if another writer with the same name already exists.
     *
     * @param firstName  the first name to check
     * @param lastName   the last name to check
     * @param excludedId the writer ID to ignore, or null when adding
     * @return true if another writer with the same name exists
     */
    boolean existsWriter(String firstName, String lastName, Integer excludedId);
}