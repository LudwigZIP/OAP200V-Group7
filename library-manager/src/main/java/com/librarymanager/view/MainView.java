package com.librarymanager.view;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;

/**
 * Represents the main user interface of the Private Library Manager.
 *
 * This class organizes the application's different views using
 * a TabPane, allowing users to navigate between books, writers,
 * genres, bookshelves, and reports.
 * 
 * Also provieds a button to check the current DB connection.
 *
 * Contribution: Responsible for the main interface layout,
 * tab navigation, and integration of the application's views.
 *
 * @author Dilan Shwan
 */

public class MainView extends VBox {


    // Main navigation component containing the application tabs
    private final TabPane tabPane = new TabPane();
    
    // Views for displaying books, writers, genres, bookshelves and reports respectively
    private final BookView bookView = new BookView();
    private final WriterView writerView = new WriterView();
    private final GenreView genreView = new GenreView();
    private final BookshelfView bookshelfView = new BookshelfView();
    private final ReportView reportView = new ReportView();


    private final Button testDbButton = new Button("Check Database Connection");

    /**
    * Constructs the main application interface.
    *
    * Initializes the different views, creates their corresponding
    * non-closable tabs, and adds them to the main TabPane.
    * Also places the DB connection button above the tab bar
    */

    public MainView() {


        // Create tabs for the different application views

        Tab bookTab = new Tab("Books", bookView);
        bookTab.setClosable(false);

        Tab writerTab = new Tab("Writers", writerView);
        writerTab.setClosable(false);

        Tab genreTab = new Tab("Genres", genreView);
        genreTab.setClosable(false);

        Tab bookshelfTab = new Tab("Bookshelves", bookshelfView);
        bookshelfTab.setClosable(false);

        Tab reportTab = new Tab("Reports", reportView);
        reportTab.setClosable(false);

        // Add all tabs to the main navigation component
        tabPane.getTabs().addAll(
            bookTab,
            writerTab,
            genreTab,
            bookshelfTab,
            reportTab
        );

        getChildren().addAll(testDbButton, tabPane);
    }

    /**
     * Returns the button used to check the database connection.
     *
     * @return the database connection button
     */
    public Button getTestDbButton() {
        return testDbButton;
    }

    /**
     * Returns the view used to manage books.
     *
     * @return the BookView instance
     */
    public BookView getBookView() {
        return bookView;
    }

    /**
     * Returns the view used to manage writers.
     *
     * @return the WriterView instance
     */
    public WriterView getWriterView() {
        return writerView;
    }

    /**
     * Returns the view used to manage genres.
     *
     * @return the GenreView instance
     */
    public GenreView getGenreView() {
        return genreView;
    }

    /**
     * Returns the view used to manage bookshelves.
     *
     * @return the BookshelfView instance
     */
    public BookshelfView getBookshelfView() {
        return bookshelfView;
    }

    /**
     * Returns the view used to display and export library reports.
     *
     * @return the ReportView instance
     */
    public ReportView getReportView() {
        return reportView;
    }

    /**
     * Returns the main TabPane used for navigation.
     *
     * Allows other components to access the selected tab
     * and listen for navigation changes.
     *
     * @return the main TabPane
     */
    public TabPane getTabPane() {
        return tabPane;
    }
}