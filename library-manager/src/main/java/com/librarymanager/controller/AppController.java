package com.librarymanager.controller;

import java.sql.Connection;

import com.librarymanager.dao.BookDAO;
import com.librarymanager.dao.BookshelfDAO;
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
            // Creates DAOs
            bookDao = new BookDaoImpl(conn);
            bookshelfDao = new BookshelfDaoImpl(conn);
            genreDao = new GenreDaoImpl(conn);
            writerDao = new WriterDaoImpl(conn);
        } else {
            AlertUtils.showError("Could not connect to the database.");
            Platform.exit();
        }
    }

    private void handleCheckDb() {
        try (Connection test = DatabaseConnection.getConnection()) {
            if (test != null && !test.isClosed()) {
                AlertUtils.showInfo("Database Connection", "Successfully connected to the database.");
            } else {
                AlertUtils.showError("Could not establish a database connection.");
            }
        } catch (Exception ex) {
            AlertUtils.showError("Failed to connect to the database:\n" + ex.getMessage());
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