package com.librarymanager.controller;

import com.librarymanager.dao.WriterDAO;
import com.librarymanager.model.Writer;

import java.util.List;

/**
 * Controller class for managing writers in the Library Manager application
 * 
 * WriterController
 */
public class WriterController{
    private final WriterDAO writerDAO;

    // Gives the controller access to the DAO
    public WriterController(WriterDAO writerDAO) {
        this.writerDAO = writerDAO;
    }

    /**
     * Adds a new writer to the database
     * 
     * @param writer the writer to add
     * @return the created writer
     */
    public Writer addWriter(Writer writer) {
        return writerDAO.create(writer);
    }

    /**
     * Updates an existing writer in the database
     * @param writer the writer to update
     */
    public void updateWriter(Writer writer) {
        writerDAO.updateWriter(writer);
    }
    
    /**
     * Deletes a writer from the database
     * 
     * @param writer the writer to delete
     */
    public void deleteWriter(Writer writer) {
        writerDAO.deleteWriter(writer);
    }

    /**
     * Gets all writers from the database
     * 
     * @return a list of all writers
     */
    public List<Writer> getAllWriters() {
        return writerDAO.findAll();
    }
}