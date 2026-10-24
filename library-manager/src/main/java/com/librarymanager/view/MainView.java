package com.librarymanager.view;

import com.librarymanager.controller.AppController;
import com.librarymanager.controller.BookshelfController;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;

/**
 * Represents the main user interface of the Private Library Manager.
 *
 * This class organizes the application's different views using
 * a TabPane, allowing users to navigate between books, writers,
 * genres, and bookshelves.
 *
 * Contribution: Responsible for the main interface layout,
 * tab navigation, and integration of the application's views.
 *
 * @author Dilan Shwan
 * @version 1.0
 */

public class MainView extends VBox {


    private final TabPane tabPane = new TabPane();
    
    private final BookView bookView = new BookView();
    private final WriterView writerView = new WriterView();
    private final GenreView genreView = new GenreView();
    private final BookshelfView bookshelfView = new BookshelfView();
    private final BookshelfController bookshelfController;

    /**
    * Constructs the main application view.
    *
    * Initializes the different views, creates their corresponding
    * non-closable tabs, and adds them to the main TabPane.
    */

    public MainView(AppController appController) {

        bookshelfController = new BookshelfController(
            bookshelfView, appController.getBookshelfDao());

        Button testDbButton = new Button("Check Database Connection");

        testDbButton.setOnAction(e -> appController.handleCheckDb());

        Tab bookTab = new Tab("Books", bookView);
        bookTab.setClosable(false);

        Tab writerTab = new Tab("Writers", writerView);
        writerTab.setClosable(false);

        Tab genreTab = new Tab("Genres", genreView);
        genreTab.setClosable(false);

        Tab bookshelfTab = new Tab("Bookshelves", bookshelfView);
        bookshelfTab.setClosable(false);

        tabPane.getTabs().addAll(
            bookTab,
            writerTab,
            genreTab,
            bookshelfTab
        );

        getChildren().addAll(testDbButton, tabPane);
    }
}