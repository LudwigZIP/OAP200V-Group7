package com.librarymanager.controller;

import com.librarymanager.dao.BookDAO;
import com.librarymanager.model.Book;
import com.librarymanager.view.ReportView;
import com.librarymanager.util.AlertUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ReportController {
    
    private final ReportView view;
    private final BookDAO bookDAO;

    public ReportController(ReportView view, BookDAO bookDAO) {
        this.view = view;
        this.bookDAO = bookDAO;

        view.getRefreshButton().setOnAction( e -> refreshReport());
    }

    public void refreshReport() {
        try {
            List<Book> books = bookDAO.findAll();

            Map<String, List<String>> byGenre = new TreeMap<>();

            Map<String, List<String>> byWriter = new TreeMap<>();

            List<String> read = new ArrayList<>();
            List<String> unread = new ArrayList<>();
            List<String> reading = new ArrayList<>();

            for (Book book: books) {

                String title = book.getTitle();
                String genre = book.getGenre().getName();
                String writer = book.getWriter().toString();

                byGenre.computeIfAbsent(genre, key -> new ArrayList<>()).add(title);

                byWriter.computeIfAbsent(writer, key -> new ArrayList<>()).add(title);

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

            StringBuilder report = new StringBuilder();

            report.append("LIBRARY SUMMARY\n\n");
            report.append("Total books: ").append(books.size()).append("\n");
            report.append("read: ").append(read.size()).append("\n");
            report.append("Unread: ").append(unread.size()).append("\n");
            report.append("Reading: ").append(reading.size()).append("\n");

            appendGroups(report, "BOOKS BY GENRE", byGenre);
            appendGroups(report, "BOOKS BY WRITER", byWriter);

            appendBookList(report, "READ BOOKS", read);
            appendBookList(report, "UNREAD BOOKS", unread);
            appendBookList(report, "CURRENTLY READING", reading);

            view.setReportText(report.toString());

        }

        catch (RuntimeException e) {
            AlertUtils.showError("Failed to generate report:\n" + e.getMessage());
        }
    }

    private void appendGroups(StringBuilder report,String heading,Map<String, List<String>> groups) {

        report.append("\n").append(heading).append("\n\n");

        for (Map.Entry<String, List<String>> entry: groups.entrySet()) {

            report.append(entry.getKey()).append(" (").append(entry.getValue().size()).append(")\n");

            for (String title : entry.getValue()) {
                report.append("  - ").append(title).append("\n");
            }
        }
    }

    private void appendBookList(StringBuilder report, String heading, List<String> titles) {
        
        report.append("\n").append(heading).append(" (").append(titles.size()).append(")\n\n");

        for (String title: titles) {
            report.append("  - ").append(title).append("\n");
        }
    }

}
