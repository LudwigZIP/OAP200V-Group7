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

/**
 * Coordinates the initialization and shutdown of the application.
 *
 * Establishes the initial database connection, creates the DAOs,
 * and connects the different views to their controllers.
 * Also handles database connection checks and report refreshing
 * when the user selects the Reports tab.
 *
 * Contribution: Application initialization, controller setup,
 * database connection checks, and application shutdown.
 *
 * @author Dilan Shwan
 */

public class AppController {

    // Connection used for checking database availability 
    private Connection conn;
    private final MainView mainView;

    // DAOs used to retrieve and modify data in the database.
    private BookDAO bookDao;
    private BookshelfDAO bookshelfDao;
    private GenreDAO genreDao;
    private WriterDAO writerDao;

    /**
     * Creates an application controller for the provided main view.
     *
     * @param mainView the main application interface
     */
    public AppController(MainView mainView) {
        this.mainView = mainView;
    }

    /**
     * Initializes the database connection and application controllers.
     *
     * Creates the DAOs, connects controllers to their views, and
     * loads reference data for the book form. Also assigns actions
     * to the database connection button and report tab.
     *
     * If the initial database connection fails, an error message
     * is displayed and the application is closed.
     */
    public void start() {

        // Connects to the database
        conn = DatabaseConnection.getConnection();

        if (conn != null) {
            // Creates the DAOs used by the application controllers.
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

            // Connect the report view to its controller using the existing BookDAO
            ReportController reportController = new ReportController(mainView.getReportView(), bookDao);

            /* Refreshes the report when the user opens the report tab
            in case the user makes a change to the database after opening the report tab */
            mainView.getTabPane().getSelectionModel().selectedItemProperty()
            .addListener((observable, oldTab, newTab) -> {if(newTab.getContent() == mainView.getReportView()) {reportController.refreshReport();}});

            // Connect the DB check method to its button
            mainView.getTestDbButton().setOnAction(e -> handleCheckDb());
        } 
        else {
            AlertUtils.showError("Could not connect to the database.");
            Platform.exit();
        }
    }

    /**
     * Checks whether the initial database connection is still valid.
     *
     * Displays a confirmation if the connection is active, or an
     * error message if the connection is invalid or the check fails.
     */
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

    /**
     * Closes the initial database connection when the application stops.
     *
     * Checks that the connection exists and is still open before
     * attempting to close it.
     */
    public void stop() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception ignored) {
            // Ignore exceptions when application is shutting down
        }
    }
}