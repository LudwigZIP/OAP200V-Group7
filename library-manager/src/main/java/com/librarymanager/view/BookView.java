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
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Displays the book table, search controls and validated input form.
 *
 * @author Ulrik Marker
 */
public class BookView extends BorderPane {

    private final TableView<Book> table = new TableView<>();

    // Book input controls.
    private final TextField titleField = new TextField();
    private final TextField yearField = new TextField();
    private final ComboBox<String> statusField = new ComboBox<>();
    private final ComboBox<Writer> writerField = new ComboBox<>();
    private final ComboBox<Genre> genreField = new ComboBox<>();
    private final ComboBox<Bookshelf> bookshelfField = new ComboBox<>();

    // Book action buttons.
    private final Button addButton = new Button("Add");
    private final Button updateButton = new Button("Update");
    private final Button deleteButton = new Button("Delete");
    private final Button clearButton = new Button("Clear");

    // Optional search and filter controls.
    private final TextField searchField = new TextField();
    private final ComboBox<Writer> writerFilter = new ComboBox<>();
    private final TextField filterYearField = new TextField();
    private final Button searchButton = new Button("Search");

    /**
     * Creates the table area and the scrollable form on the right.
     */
    public BookView() {
        buildTable();
        buildForm();

        BorderPane bookListPane = new BorderPane();
        bookListPane.setTop(buildSearchBar());
        bookListPane.setCenter(table);

        setCenter(bookListPane);
    }

    /**
     * Creates the book table columns.
     */
    private void buildTable() {
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

        // Related objects use their toString() methods for display.
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

        titleColumn.setPrefWidth(230);
        yearColumn.setPrefWidth(60);
        statusColumn.setPrefWidth(80);
        writerColumn.setPrefWidth(150);
        genreColumn.setPrefWidth(150);
        bookshelfColumn.setPrefWidth(220);

        table.getColumns().add(titleColumn);
        table.getColumns().add(yearColumn);
        table.getColumns().add(statusColumn);
        table.getColumns().add(writerColumn);
        table.getColumns().add(genreColumn);
        table.getColumns().add(bookshelfColumn);

        table.setPlaceholder(
                new Label("No books match the current criteria."));
    }

    /**
     * Creates optional title, writer and year search controls.
     */
    private FlowPane buildSearchBar() {
        searchField.setPromptText("Full or partial title");
        searchField.setPrefWidth(200);

        filterYearField.setPromptText("For example 1997");
        filterYearField.setPrefWidth(150);

        writerFilter.setPromptText("All writers");
        writerFilter.setPrefWidth(180);

        // Displays a readable label for the unrestricted writer choice.
        writerFilter.setCellFactory(list -> createWriterFilterCell());
        writerFilter.setButtonCell(createWriterFilterCell());

        // Wraps controls onto another row in a narrower window.
        FlowPane searchBar = new FlowPane(10, 10);
        searchBar.setPadding(new Insets(10));

        searchBar.getChildren().addAll(
                new VBox(5,
                        new Label("Title (optional)"), searchField),
                new VBox(5,
                        new Label("Writer (optional)"), writerFilter),
                new VBox(5,
                        new Label("Publication year (optional)"),
                        filterYearField),
                new VBox(5,
                        new Label("Apply criteria"), searchButton));

        return searchBar;
    }

    /**
     * Creates writer-filter cells, including the "All writers" choice.
     */
    private ListCell<Writer> createWriterFilterCell() {
        return new ListCell<Writer>() {
            @Override
            protected void updateItem(Writer writer, boolean empty) {
                super.updateItem(writer, empty);

                if (empty) {
                    setText(null);
                } else {
                    setText(writer == null
                            ? "All writers"
                            : writer.toString());
                }
            }
        };
    }

    /**
     * Creates the required and optional book input fields.
     */
    private void buildForm() {
        statusField.getItems().addAll("Unread", "Reading", "Read");
        statusField.setValue("Unread");

        titleField.setPromptText("Enter a book title");
        yearField.setPromptText("Leave empty if unknown");
        writerField.setPromptText("Select a writer");
        genreField.setPromptText("Select a genre");
        bookshelfField.setPromptText("Select a bookshelf");

        statusField.setMaxWidth(Double.MAX_VALUE);
        writerField.setMaxWidth(Double.MAX_VALUE);
        genreField.setMaxWidth(Double.MAX_VALUE);
        bookshelfField.setMaxWidth(Double.MAX_VALUE);

        HBox buttons = new HBox(
                8, addButton, updateButton, deleteButton, clearButton);

        VBox form = new VBox(8,
                new Label("Title (required)"), titleField,
                new Label("Publication year (optional)"), yearField,
                new Label("Reading status (required)"), statusField,
                new Label("Writer (required)"), writerField,
                new Label("Genre (required)"), genreField,
                new Label("Bookshelf (required)"), bookshelfField,
                buttons);

        form.setPadding(new Insets(15));

        // Keeps all form controls accessible in a shorter window.
        ScrollPane scrollPane = new ScrollPane(form);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefWidth(330);
        scrollPane.setMinWidth(330);

        setRight(scrollPane);
    }

    /**
     * Supplies reference data to the form and writer filter.
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

        // A null selection means no writer restriction.
        writerFilter.getItems().clear();
        writerFilter.getItems().add(null);
        writerFilter.getItems().addAll(writers);
        writerFilter.setValue(null);
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

    /** @return the reading status input */
    public ComboBox<String> getStatusField() {
        return statusField;
    }

    /** @return the writer input */
    public ComboBox<Writer> getWriterField() {
        return writerField;
    }

    /** @return the genre input */
    public ComboBox<Genre> getGenreField() {
        return genreField;
    }

    /** @return the bookshelf input */
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

    /** @return the Clear button */
    public Button getClearButton() {
        return clearButton;
    }

    /** @return the title search input */
    public TextField getSearchField() {
        return searchField;
    }

    /** @return the writer filter */
    public ComboBox<Writer> getWriterFilter() {
        return writerFilter;
    }

    /** @return the publication year filter */
    public TextField getFilterYearField() {
        return filterYearField;
    }

    /** @return the Search button */
    public Button getSearchButton() {
        return searchButton;
    }
}
