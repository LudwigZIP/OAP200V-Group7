package com.librarymanager.dao;

import com.librarymanager.model.Bookshelf;

import java.util.List;
import java.util.Optional;

/**
 * Data-access contract for bookshelf persistence operations.
 *
 * @author Thomas Mølnvik
 */
public interface BookshelfDAO {

    /**
     * Creates a new bookshelf in the database.
     *
     * @param bookshelf the bookshelf to create
     * @return the created bookshelf with its generated ID
     */
    Bookshelf create(Bookshelf bookshelf);

    /**
     * Retrieves a bookshelf by its ID.
     *
     * @param id the ID of the bookshelf to retrieve
     * @return an optional containing the bookshelf, or empty if it is not found
     */
    Optional<Bookshelf> findById(int id);

    /**
     * Checks whether another bookshelf uses the given shelf number.
     *
     * @param shelfNumber the shelf number to check
     * @param excludedBookshelfId the bookshelf ID to ignore, or null
     * @return true if another bookshelf uses the shelf number
     */
    boolean isShelfNumberInUse(int shelfNumber, Integer excludedBookshelfId);

    /**
     * Retrieves all bookshelves from the database.
     *
     * @return a list of all bookshelves
     */
    List<Bookshelf> findAll();

    /**
     * Updates an existing bookshelf in the database.
     *
     * @param bookshelf the bookshelf to update
     */
    void update(Bookshelf bookshelf);

    /**
     * Deletes a bookshelf from the database.
     *
     * @param id the ID of the bookshelf to delete
     */
    void delete(int id);
}
