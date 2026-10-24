package com.librarymanager.controller;

import java.sql.Connection;
import java.sql.SQLException;

import com.librarymanager.dao.BookDAO;
import com.librarymanager.dao.BookshelfDAO;
import com.librarymanager.dao.BookshelfDAOImpl;
import com.librarymanager.dao.GenreDAO;
import com.librarymanager.dao.WriterDAO;
import com.librarymanager.util.AlertUtils;
import com.librarymanager.util.DatabaseConnection;

import javafx.application.Platform;

public class AppController {

    private Connection conn;

    private BookDAO bookDao;
    private BookshelfDAO bookshelfDao;
    private GenreDAO genreDao;
    private WriterDAO writerDao;

    public void start() {
        // Connects to the db
        conn = DatabaseConnection.getConnection();
        if (conn != null) {
            // Creates the remaining DAOs when their implementations are available.
            // bookDao = new BookDAOImpl(conn);
            // genreDao = new GenreDAOImpl(conn);
            // writerDao = new WriterDAOImpl(conn);
        } else {
            AlertUtils.showError("Could not connect to the database.");
            Platform.exit();
        }
    }

    /**
     * Returns the DAO used by the bookshelf screen.
     *
     * @return the bookshelf DAO
     */
    public BookshelfDAO getBookshelfDao() {
        return bookshelfDao;
    }

    public void handleCheckDb() {
        try {
            if (conn != null && conn.isValid(2)) {
                AlertUtils.showInfo("Database Connection", "The database connection is active!");
            }
            else {
                AlertUtils.showError("The database connection is not active!");
            }
        }
        catch (SQLException error) {
            AlertUtils.showError("Failed to check database connection:\n" + error.getMessage());
        }
    }

    public void stop() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception ignored) {
            // Ignore shutdown exceptions.
        }
    }
}