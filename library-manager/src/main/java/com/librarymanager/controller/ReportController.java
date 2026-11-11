package com.librarymanager.controller;

import com.librarymanager.dao.BookDAO;
import com.librarymanager.model.Book;
import com.librarymanager.view.ReportView;
import com.librarymanager.util.AlertUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;

import javafx.stage.FileChooser;

/**
 * Handles the generation and export of library summary reports.
 *
 * Retrieves books through BookDAO and organizes them by genre,
 * writer, and reading status. The generated report is displayed
 * in ReportView and can be exported as a UTF-8 text file.
 *
 * Contribution: Report generation, grouping of book information,
 * and exporting reports to text files.
 *
 * @author Dilan Shwan
 */

public class ReportController {
    
    private final ReportView view;
    private final BookDAO bookDAO;

    /**
     * Creates the report controller and initializes button actions.
     *
     * Connects the refresh and export buttons to their respective
     * methods.
     *
     * @param view the view used to display the report
     * @param bookDAO the DAO used to retrieve book information
     */
    public ReportController(ReportView view, BookDAO bookDAO) {
        this.view = view;
        this.bookDAO = bookDAO;

        // Refresh the report when the refresh button is clicked
        view.getRefreshButton().setOnAction( e -> refreshReport());

        // Export the displayed data when the export button is clciked
        view.getExportButton().setOnAction( e -> exportReport());
    }

    /**
     * Generates an updated summary of the book collection.
     *
     * Retrieves all registered books and groups their titles by
     * genre, writer, and reading status. Calculates the total number
     * of books and the number belonging to each category.
     *
     * The generated report is displayed in the view. If an error
     * occurs while retrieving or processing data, an error message
     * is displayed instead.
     */
    public void refreshReport() {
        try {
            List<Book> books = bookDAO.findAll();

            // Group book titles by genre and writer
            // TreeMap keeps the groups sorted alphabetically
            Map<String, List<String>> byGenre = new TreeMap<>();
            Map<String, List<String>> byWriter = new TreeMap<>();

            // Store titles according to their reading status
            List<String> read = new ArrayList<>();
            List<String> unread = new ArrayList<>();
            List<String> reading = new ArrayList<>();

            // Categorize each book using its stored information
            for (Book book: books) {

                String title = book.getTitle();
                String genre = book.getGenre().getName();
                String writer = book.getWriter().toString();

                // Create a list if the category is not already present
                // then add the book title to the corresponding group
                byGenre.computeIfAbsent(genre, key -> new ArrayList<>()).add(title);

                byWriter.computeIfAbsent(writer, key -> new ArrayList<>()).add(title);

                // Place the title in the appropriate reading status list
                switch(book.getReadingStatus()) {
                    case "Read":
                        read.add(title);
                        break;
                    case "Unread":
                        unread.add(title);
                        break;
                    case "Reading":
                        reading.add(title);
                        break;
                }
            }

            // Build the report by combining the calculated summaries
            StringBuilder report = new StringBuilder();

            report.append("LIBRARY SUMMARY\n\n");
            report.append("Total books: ").append(books.size()).append("\n");
            report.append("Read: ").append(read.size()).append("\n");
            report.append("Unread: ").append(unread.size()).append("\n");
            report.append("Reading: ").append(reading.size()).append("\n");

            // Add the sections containing books grouped by category
            appendGroups(report, "BOOKS BY GENRE", byGenre);
            appendGroups(report, "BOOKS BY WRITER", byWriter);

            // Add the sections containing books by reading status
            appendBookList(report, "READ BOOKS", read);
            appendBookList(report, "UNREAD BOOKS", unread);
            appendBookList(report, "CURRENTLY READING", reading);

            // Display the completed report
            view.setReportText(report.toString());

        }

        catch (RuntimeException e) {
            AlertUtils.showError("Failed to generate report:\n" + e.getMessage());
        }
    }

    /**
     * Exports the currently displayed report to a text file.
     *
     * Opens a file chooser where the user can select the filename
     * and destination. If a file is selected, the report is saved
     * using UTF-8 encoding.
     *
     * Cancelling the dialog does not create a file. An error message
     * is displayed if the file cannot be written.
     */
    private void exportReport() {

        // Create a prompt where the user chooses where to save the report
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Library Report");

        // Suggest a default filename and restrict format to .txt
        fileChooser.setInitialFileName("LibraryReport.txt");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt"));

        // Open the save prompt using the application's current window
        File file = fileChooser.showSaveDialog(view.getScene().getWindow());

        // Only saves if the user actually selected a file
        if (file != null) {
            try {

                // Writes the report text inside the selected file
                Files.writeString(file.toPath(),view.getReportText(),StandardCharsets.UTF_8);

                AlertUtils.showInfo("Export Successful", "Report saved Successfully!");
            }
            catch (IOException e) {
                AlertUtils.showError("Failed to export report: \n" + e.getMessage());
            }
        }
    }

    /**
     * Appends a section containing books grouped by category.
     *
     * Each category is displayed with its name, number of books,
     * and a list of the corresponding book titles. This method
     * is used for both genre and writer reports.
     *
     * @param report the StringBuilder containing the report
     * @param heading the heading for the report section
     * @param groups the book titles grouped by category name
     */
    private void appendGroups(StringBuilder report,String heading,Map<String, List<String>> groups) {

        report.append("\n").append(heading).append("\n\n");

        for (Map.Entry<String, List<String>> entry: groups.entrySet()) {

            // Append the category name and its total number of books
            report.append(entry.getKey()).append(" (").append(entry.getValue().size()).append(")\n");

            // Append each book title belonging to the category
            for (String title : entry.getValue()) {
                report.append("  - ").append(title).append("\n");
            }
        }
    }

    /**
     * Appends a section listing books with a specific reading status.
     *
     * Displays the section heading, the number of books, and their
     * individual titles. Used for read, unread, and currently
     * reading books.
     *
     * @param report the StringBuilder containing the report
     * @param heading the heading for the reading status section
     * @param titles the titles of books with the specified status
     */
    private void appendBookList(StringBuilder report, String heading, List<String> titles) {
        
        report.append("\n").append(heading).append(" (").append(titles.size()).append(")\n\n");

        for (String title: titles) {
            report.append("  - ").append(title).append("\n");
        }
    }

}
