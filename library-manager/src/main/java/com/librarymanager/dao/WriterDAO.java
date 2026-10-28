package com.librarymanager.dao;

import com.librarymanager.model.Writer;
import java.util.List;

/**
 * Data-access contract for writer persistence operations.
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