package com.librarymanager.model;

/**
 * Represents a book in the private library.
 *
 * Stores the book's title, publication year, reading status,
 * and references to its writer, genre, and bookshelf.
 *
 * Author: Dior Cessay — responsible for Book model and DAO.
 */
public class Book {

    // ===== Fields =====

    // Unique identifier for the book, usually assigned by the database.
    private int id;

    // The title of the book.
    private String title;

    // The year the book was published.
    // Integer is used instead of int because the year can be null.
    private Integer publicationYear;

    // The reading status: "read", "unread", or "currently reading".
    private String readingStatus;

    // Reference to the writer associated with this book.
    private Writer writer;

    // Reference to the genre associated with this book.
    private Genre genre;

    // Reference to the bookshelf where the book is stored.
    private Bookshelf bookshelf;


    // ===== Constructors =====

    /**
     * Creates a new book that has not yet been saved to the database.
     */
    public Book(String title, Integer publicationYear,
                String readingStatus, Writer writer,
                Genre genre, Bookshelf bookshelf) {

        // Validates the title before storing it.
        this.title = requireTitle(title);

        // Stores the publication year.
        this.publicationYear = publicationYear;

        // Validates the reading status before storing it.
        this.readingStatus = requireReadingStatus(readingStatus);

        // Stores the related writer, genre, and bookshelf objects.
        this.writer = writer;
        this.genre = genre;
        this.bookshelf = bookshelf;
    }

    /**
     * Reconstructs a book that already exists in the database.
     */
    public Book(int id, String title, Integer publicationYear,
                String readingStatus, Writer writer,
                Genre genre, Bookshelf bookshelf) {

        // Ensures that the book ID is not negative.
        if (id < 0) {
            throw new IllegalArgumentException(
                    "Book id cannot be negative.");
        }

        // Stores the existing database ID.
        this.id = id;

        // Validates and stores the title.
        this.title = requireTitle(title);

        // Stores the publication year.
        this.publicationYear = publicationYear;

        // Validates and stores the reading status.
        this.readingStatus = requireReadingStatus(readingStatus);

        // Stores the related objects.
        this.writer = writer;
        this.genre = genre;
        this.bookshelf = bookshelf;
    }


    // ===== Getters and setters =====

    // A getter returns the current value of a field.
    // A setter changes the value of a field.

    public int getId() {
        return id;
    }

    public void setId(int id) {

        // A persisted book must have a positive ID.
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Persisted book id must be positive.");
        }

        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {

        // Reuses the validation method to ensure the title is valid.
        this.title = requireTitle(title);
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getReadingStatus() {
        return readingStatus;
    }

    public void setReadingStatus(String readingStatus) {

        // Only accepted reading statuses can be assigned.
        this.readingStatus = requireReadingStatus(readingStatus);
    }

    public Writer getWriter() {
        return writer;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Bookshelf getBookshelf() {
        return bookshelf;
    }

    public void setBookshelf(Bookshelf bookshelf) {
        this.bookshelf = bookshelf;
    }


    // ===== Validation =====

    // Checks that the title is not null or empty.
    // trim() removes whitespace at the beginning and end.
    // The method returns the cleaned title if it is valid.
    private static String requireTitle(String title) {

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException(
                    "Book title is required.");
        }

        return title.trim();
    }

    // Checks that the reading status matches one of the
    // three values supported by this model.
    private static String requireReadingStatus(String status) {

        if (status == null ||
                !(status.equals("read")
                || status.equals("unread")
                || status.equals("currently reading"))) {

            // Throws an exception if the status is invalid.
            throw new IllegalArgumentException(
                    "Reading status must be read, unread, "
                    + "or currently reading.");
        }

        // Returns the status if it passes validation.
        return status;
    }


    // ===== Object methods =====

    // Returns the book title when the object is converted to a string.
    // For example, System.out.println(book) prints the title.
    @Override
    public String toString() {
        return title;
    }

    // Defines when two Book objects are considered equal.
    @Override
    public boolean equals(Object o) {

        // If both references point to the same object,
        // the objects are equal.
        if (this == o) return true;

        // An object of another class cannot be equal to this Book.
        if (!(o instanceof Book)) return false;

        // Converts the object to a Book so its ID can be compared.
        Book book = (Book) o;

        // Two books are considered equal if both have positive IDs
        // and their IDs are identical.
        return id > 0 && book.id > 0 && id == book.id;
    }

    // Provides a hash code for the Book class.
    // Objects considered equal by equals() must have the same hash code.
    @Override
    public int hashCode() {
        return Book.class.hashCode();
    }
}