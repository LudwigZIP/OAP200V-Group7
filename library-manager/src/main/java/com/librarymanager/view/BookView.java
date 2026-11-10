package com.librarymanager.view;

import com.librarymanager.model.Book;
import com.librarymanager.model.Writer;
import com.librarymanager.model.Genre;
import com.librarymanager.model.Bookshelf;

import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
/**
 * Displays the book table, title search and book input form.
 *
 * @author Ulrik Marker
 */
public class BookView extends BorderPane {

    // Table displaying registered books.
    private final TableView<Book> table = new TableView<>();

    // Input fields for book information.
    private final TextField titleField = new TextField();
    private final TextField yearField = new TextField();
    private final ComboBox<String> statusField = new ComboBox<>();
    private final ComboBox<Writer> writerField = new ComboBox<>();
    private final ComboBox<Genre> genreField = new ComboBox<>();
    private final ComboBox<Bookshelf> bookshelfField = new ComboBox<>();

    // Buttons connected to actions by the controller.
    private final Button addButton = new Button("Add");
    private final Button updateButton = new Button("Update");
    private final Button deleteButton = new Button("Delete");

    // Controls used to search for books by title.
    private final TextField searchField = new TextField();
    private final Button searchButton = new Button("Search");

    /**
     * Creates the table, search bar and input form.
     */
    public BookView() {
        buildTable();
        buildForm();

        // Groups the search bar and table on the left.
        BorderPane bookListPane = new BorderPane();
        bookListPane.setTop(buildSearchBar());
        bookListPane.setCenter(table);

        // Lets the table area fill the space beside the form.
        setCenter(bookListPane);
    }

    /**
     * Creates the columns displaying book information.
     */
    private void buildTable() {

        // "title" tells JavaFX to use Book.getTitle().
        TableColumn<Book, String> titleColumn =
                new TableColumn<>("Title");
        titleColumn.setCellValueFactory(
                new PropertyValueFactory<>("title"));

        TableColumn<Book, Integer> yearColumn =
                new TableColumn<>("Year");
        yearColumn.setCellValueFactory(
                new PropertyValueFactory<>("publicationYear"));

        TableColumn<Book, String> statusColumn =
                new TableColumn<>("Status");
        statusColumn.setCellValueFactory(
                new PropertyValueFactory<>("readingStatus"));

        // Related objects are displayed using their toString() methods.
        TableColumn<Book, Writer> writerColumn =
                new TableColumn<>("Writer");
        writerColumn.setCellValueFactory(
                new PropertyValueFactory<>("writer"));

        TableColumn<Book, Genre> genreColumn =
                new TableColumn<>("Genre");
        genreColumn.setCellValueFactory(
                new PropertyValueFactory<>("genre"));

        TableColumn<Book, Bookshelf> bookshelfColumn =
                new TableColumn<>("Bookshelf");
        bookshelfColumn.setCellValueFactory(
                new PropertyValueFactory<>("bookshelf"));

        // Sets initial column widths.
        titleColumn.setPrefWidth(230);
        yearColumn.setPrefWidth(60);
        statusColumn.setPrefWidth(80);
        writerColumn.setPrefWidth(150);
        genreColumn.setPrefWidth(150);
        bookshelfColumn.setPrefWidth(220);

        // Adds the columns in display order.
        table.getColumns().add(titleColumn);
        table.getColumns().add(yearColumn);
        table.getColumns().add(statusColumn);
        table.getColumns().add(writerColumn);
        table.getColumns().add(genreColumn);
        table.getColumns().add(bookshelfColumn);

        // Displays a message when the list is empty.
        table.setPlaceholder(new Label("No books to display."));
    }

    /**
     * Creates the title search controls.
     *
     * @return the search bar
     */
    private HBox buildSearchBar() {
        searchField.setPromptText("Search by book title...");

        HBox searchBar = new HBox(
                10, searchField, searchButton);
        searchBar.setPadding(new Insets(10));

        // Lets the search field fill the available horizontal space.
        HBox.setHgrow(searchField, Priority.ALWAYS);

        return searchBar;
    }

    /**
     * Creates the input form on the right side of the table.
     */
    private void buildForm() {

        // Uses the reading statuses agreed for the book data.
        statusField.getItems().addAll(
                "Unread", "Reading", "Read");
        statusField.setValue("Unread");

        HBox buttons = new HBox(
                10, addButton, updateButton, deleteButton);

        VBox form = new VBox(8,
                new Label("Title"), titleField,
                new Label("Publication year"), yearField,
                new Label("Reading status"), statusField,
                new Label("Writer"), writerField,
                new Label("Genre"), genreField,
                new Label("Bookshelf"), bookshelfField,
                buttons);

        form.setPadding(new Insets(15));

        // Allow the form to scroll if necessary
        ScrollPane scrollPane = new ScrollPane(form);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefWidth(300);

        // Places the form at the right side of the window
        setRight(scrollPane);
    }

    /**
     * Fills the dropdown lists with existing reference data.
     *
     * @param writers available writers
     * @param genres available genres
     * @param bookshelves available bookshelves
     */
    public void setReferenceData(
            List<Writer> writers,
            List<Genre> genres,
            List<Bookshelf> bookshelves) {

        writerField.getItems().setAll(writers);
        genreField.getItems().setAll(genres);
        bookshelfField.getItems().setAll(bookshelves);
    }

    /** @return the book table */
    public TableView<Book> getBookTable() {
        return table;
    }

    /** @return the title input */
    public TextField getTitleField() {
        return titleField;
    }

    /** @return the publication year input */
    public TextField getYearField() {
        return yearField;
    }

    /** @return the reading status dropdown */
    public ComboBox<String> getStatusField() {
        return statusField;
    }

    /** @return the writer dropdown */
    public ComboBox<Writer> getWriterField() {
        return writerField;
    }

    /** @return the genre dropdown */
    public ComboBox<Genre> getGenreField() {
        return genreField;
    }

    /** @return the bookshelf dropdown */
    public ComboBox<Bookshelf> getBookshelfField() {
        return bookshelfField;
    }

    /** @return the Add button */
    public Button getAddButton() {
        return addButton;
    }

    /** @return the Update button */
    public Button getUpdateButton() {
        return updateButton;
    }

    /** @return the Delete button */
    public Button getDeleteButton() {
        return deleteButton;
    }

    /** @return the title search field */
    public TextField getSearchField() {
        return searchField;
    }

    /** @return the Search button */
    public Button getSearchButton() {
        return searchButton;
    }
}
