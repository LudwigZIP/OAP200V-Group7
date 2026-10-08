package com.librarymanager.controller;

import com.librarymanager.dao.BookshelfDAO;
import com.librarymanager.model.Bookshelf;

import java.util.List;
import java.util.Objects;

/**
 * Application-facing operations for managing bookshelves.
 */
public class BookshelfController {

    private final BookshelfDAO bookshelfDAO;

    public BookshelfController(BookshelfDAO bookshelfDAO) {
        this.bookshelfDAO = Objects.requireNonNull(bookshelfDAO, "bookshelfDAO");
    }

    public List<Bookshelf> getAllBookshelves() {
        return bookshelfDAO.findAll();
    }

    public Bookshelf addBookshelf(Bookshelf bookshelf) {
        return bookshelfDAO.create(Objects.requireNonNull(bookshelf, "bookshelf"));
    }

    public void updateBookshelf(Bookshelf bookshelf) {
        bookshelfDAO.update(Objects.requireNonNull(bookshelf, "bookshelf"));
    }

    public void removeBookshelf(int id) {
        bookshelfDAO.delete(id);
    }
}
