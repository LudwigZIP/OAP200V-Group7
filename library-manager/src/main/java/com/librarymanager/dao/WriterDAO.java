package com.librarymanager.dao;

import com.librarymanager.model.Writer;

import java.util.List;
import java.util.Optional;

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
     * Finds a writer by their ID
     * 
     * @param writerId the ID of the writer
     * @return containing the writer if found
     */
    Optional<Writer> findById(int writerId);

    /**
     * Retrieves all the writers from the database
     * 
     * @return a list of all writers
     */
    List<Writer> findAll();
}