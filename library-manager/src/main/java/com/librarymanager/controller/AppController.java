package com.librarymanager.controller;

import java.sql.Connection;
import java.sql.SQLException;

import com.librarymanager.view.MainView;

import com.librarymanager.dao.BookDAO;
import com.librarymanager.dao.BookDAOImpl;
import com.librarymanager.dao.BookshelfDAO;
import com.librarymanager.dao.BookshelfDAOImpl;
import com.librarymanager.dao.GenreDAO;
import com.librarymanager.dao.GenreDAOImpl;
import com.librarymanager.dao.WriterDAO;
import com.librarymanager.dao.WriterDAOImpl;

import com.librarymanager.util.AlertUtils;
import com.librarymanager.util.DatabaseConnection;

import javafx.application.Platform;

public class AppController {

    private Connection conn;
    private final MainView mainView;

    private BookDAO bookDao;
    private BookshelfDAO bookshelfDao;
    private GenreDAO genreDao;
    private WriterDAO writerDao;

    public AppController(MainView mainView) {
        this.mainView = mainView;
    }

    public void start() {

        // Connects to the db
        conn = DatabaseConnection.getConnection();

        if (conn != null) {
            // Creates the remaining DAOs when their implementations are available.
            bookDao = new BookDAOImpl();
            bookshelfDao = new BookshelfDAOImpl();
            genreDao = new GenreDAOImpl();
            writerDao = new WriterDAOImpl();

            // Connect controllers to their view and DAOs
            new WriterController(mainView.getWriterView(), writerDao);
            new BookshelfController(mainView.getBookshelfView(), bookshelfDao);
            new GenreController(mainView.getGenreView(), genreDao);
            
            // Load writers, genres and bookshelves into book view
            mainView.getBookView().setReferenceData(
                writerDao.findAll(),
                genreDao.findAll(),
                bookshelfDao.findAll()
            );

            // Connect the book controller to view and DAO
            new BookController(mainView.getBookView(), bookDao);

            // Connect the DB check button
            mainView.getTestDbButton().setOnAction(e -> handleCheckDb());
        } 
        else {
            AlertUtils.showError("Could not connect to the database.");
            Platform.exit();
        }
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