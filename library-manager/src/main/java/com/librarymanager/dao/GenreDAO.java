package com.librarymanager.dao;

import com.librarymanager.model.Genre;
import java.util.List;

/**
 * Interface defining the Data Access Object (DAO) operations for the Genre entity.
 * Provides an abstraction layer for all database interactions related to genres,
 * ensuring that the rest of the application does not depend on specific
 * persistence logic or database technology.
 *
 * This interface specifies the core CRUD operations:
 *
 * - create(Genre genre):
 *      Stores a new Genre in the database and returns the saved object,
 *      including any auto‑generated ID.
 *
 * - findById(int id):
 *      Retrieves a single Genre by its unique identifier.
 *
 * - findAll():
 *      Reads and returns a list of all Genre entries stored in the database.
 *
 * - update(Genre genre):
 *      Updates an existing Genre with new information.
 *
 * - delete(int id):
 *      Removes a Genre from the database using its ID.
 *
 * Implementations of this interface encapsulate all database logic,
 * supporting clean separation between business logic and persistence.
 * 
 * @author: Erika Marie Gåsland
 */


public interface GenreDAO {

    // Creates a new genre in the database and returns the created genre with its generated id
    // @param genre the genre object to save
    // @return the saved genre with its generated ID
    Genre create(Genre genre);

    Genre findById(int id);

    //Reads all genres from the database and returns them as a list
    // Returns a list of all genres in the database
    // @return a list of all genres
    List<Genre> findAll();

    // Updates an existing genre in the database
    // @param genre the genre object with updated information
    void update(Genre genre);

    // Deletes a genre from the database by its ID
    // @param id the ID of the genre to delete
    void delete(int id);
}
