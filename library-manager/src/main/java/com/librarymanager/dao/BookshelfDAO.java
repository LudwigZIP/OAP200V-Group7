package com.booklibrary.dao;

import com.booklibrary.model.Bookshelf;

import java.util.List;
import java.util.Optional;

/**
 * Data-access contract for {@link Bookshelf} persistence operations.
 *
 * <p>Author: [Team member name] — responsible for bookshelf persistence.</p>
 */
// This is an interface: it only declares WHAT operations exist, not HOW they work.
// A separate class (e.g. a JDBC implementation) provides the actual SQL code.
// This keeps the rest of the application independent of the database details.
// The four core operations below are known as CRUD (Create, Read, Update, Delete).
public interface BookshelfDAO {

    /**
     * Persists a new bookshelf.
     *
     * @param bookshelf the bookshelf to insert (id is ignored/assigned by the DB)
     * @return the same bookshelf instance with its generated id set
     */
    // CREATE: inserts a row and returns the object with the new id filled in.
    Bookshelf create(Bookshelf bookshelf);

    /**
     * Finds a bookshelf by primary key.
     *
     * @param id the bookshelf id
     * @return an {@link Optional} containing the bookshelf, or empty if not found
     */
    // READ (one): returns Optional instead of null, so callers are forced
    // to handle the "not found" case explicitly.
    Optional<Bookshelf> findById(int id);

    /**
     * Returns all bookshelves, ordered by name.
     *
     * @return list of all bookshelves (never null, may be empty)
     */
    // READ (all): returns an empty list, never null, when there are no shelves.
    List<Bookshelf> findAll();

    /**
     * Updates an existing bookshelf's details.
     *
     * @param bookshelf the bookshelf to update, identified by its id
     */
    // UPDATE: the id decides which row is changed; the other fields are overwritten.
    void update(Bookshelf bookshelf);

    /**
     * Deletes a bookshelf by id. The database enforces
     * {@code ON DELETE RESTRICT} on {@code book.bookshelf_id}, so this
     * will fail if any book still references this bookshelf; callers
     * should catch {@link com.booklibrary.exception.DataAccessException}
     * and inform the user to reassign or delete those books first.
     *
     * @param id the bookshelf id to delete
     */
    // DELETE: fails if books still point to this shelf (foreign key restriction),
    // so the caller must handle the resulting exception.
    void delete(int id);
}
