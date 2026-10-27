package com.librarymanager.model;

import java.time.Year;
import java.util.List;
import java.util.Locale;

/**
 * Represents a book in the private library.
 *
 * Stores the book's title, publication year and reading status, and
 * references to its writer, genre, and bookshelf.
 *
 * All values are checked in the constructors and setters before they
 * are stored. This way the rest of the program can never end up with
 * a book that has an empty title, no writer, or a reading status the
 * database doesn't support.
 *
 * A book with ID 0 has not been saved yet. The database creates the
 * real ID, and BookDAOImpl puts it into the object by calling setId().
 *
 * @author Dior Cessay — responsible for the Book model: fields,
 *         constructors, validation, getters and setters, equals(),
 *         hashCode() and toString().
 */
public class Book {

    // ===== Reading statuses =====

    // The three reading statuses are written here once, exactly as they
    // are stored in the reading_status column of the database.
    // A status like "read" or "currently reading" from the view is
    // turned into one of these when it is set (see requireReadingStatus).

    /** The user has not started the book yet. */
    public static final String STATUS_UNREAD = "Unread";

    /** The user is reading the book at the moment. */
    public static final String STATUS_READING = "Reading";

    /** The user has finished reading the book. */
    public static final String STATUS_READ = "Read";

    /**
     * All reading statuses that are allowed.
     * The view uses this list to fill the reading status ComboBox.
     */
    public static final List<String> READING_STATUSES =
            List.of(STATUS_UNREAD, STATUS_READING, STATUS_READ);

    /** The longest title the database can store (VARCHAR(200)). */
    public static final int MAX_TITLE_LENGTH = 200;


    // ===== Fields =====

    /**
     * Unique identifier for the book, assigned by the database.
     * It is 0 until the book has been saved.
     */
    private int id;

    /** The title of the book. */
    private String title;

    /**
     * The year the book was published.
     * Integer is used instead of int because the year can be null.
     */
    private Integer publicationYear;

    /** The reading status: "Unread", "Reading", or "Read". */
    private String readingStatus;

    /** The writer of this book. */
    private Writer writer;

    /** The genre of this book. */
    private Genre genre;

    /** The bookshelf where the book is stored. */
    private Bookshelf bookshelf;


    // ===== Constructors =====

    /**
     * Creates a new book that has not yet been saved to the database.
     * The ID stays 0 until BookDAO.save() gives the book its real ID.
     *
     * @param title           the title of the book
     * @param publicationYear the year it was published, or null if unknown
     * @param readingStatus   "Unread", "Reading" or "Read"
     * @param writer          the writer of the book
     * @param genre           the genre of the book
     * @param bookshelf       the bookshelf where the book is stored
     * @throws IllegalArgumentException if a value is missing or not valid
     */
    public Book(String title, Integer publicationYear,
                String readingStatus, Writer writer,
                Genre genre, Bookshelf bookshelf) {

        // Validates the title, year and reading status before storing them.
        this.title = requireTitle(title);
        this.publicationYear = requireValidYear(publicationYear);
        this.readingStatus = requireReadingStatus(readingStatus);

        // Every book needs a writer, a genre and a bookshelf,
        // because the database doesn't allow these to be empty.
        this.writer = requireWriter(writer);
        this.genre = requireGenre(genre);
        this.bookshelf = requireBookshelf(bookshelf);
    }

    /**
     * Reconstructs a book that already exists in the database.
     * BookDAOImpl uses this constructor when it reads a book from the database.
     *
     * @param id              the book's ID in the database (must be above 0)
     * @param title           the title of the book
     * @param publicationYear the year it was published, or null if unknown
     * @param readingStatus   "Unread", "Reading" or "Read"
     * @param writer          the writer of the book
     * @param genre           the genre of the book
     * @param bookshelf       the bookshelf where the book is stored
     * @throws IllegalArgumentException if the ID or another value is not valid
     */
    public Book(int id, String title, Integer publicationYear,
                String readingStatus, Writer writer,
                Genre genre, Bookshelf bookshelf) {

        // Calls the first constructor, so the same validation code
        // doesn't have to be written twice.
        this(title, publicationYear, readingStatus, writer, genre, bookshelf);

        // A book that comes from the database always has an ID above 0.
        this.id = requirePositiveId(id);
    }


    // ===== Getters and setters =====

    // A getter returns the current value of a field.
    // A setter changes the value of a field.

    /**
     * Returns the ID of the book.
     *
     * @return the ID, or 0 if the book has not been saved yet
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID that the database created for this book.
     * BookDAOImpl calls this method once, right after the book is inserted.
     *
     * @param id the new ID (must be above 0)
     * @throws IllegalArgumentException if the ID is 0 or negative
     * @throws IllegalStateException    if the book already has an ID
     */
    public void setId(int id) {

        // The ID connects this object to one row in the database.
        // If it could be changed later, the object would suddenly point
        // to a different book, so the ID can only be set once.
        if (this.id != 0) {
            throw new IllegalStateException(
                    "Book already has id " + this.id + "; it cannot be changed.");
        }

        this.id = requirePositiveId(id);
    }

    /**
     * Returns the title of the book.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Changes the title of the book.
     *
     * @param title the new title
     * @throws IllegalArgumentException if the title is empty or too long
     */
    public void setTitle(String title) {

        // Reuses the validation method to ensure the title is valid.
        this.title = requireTitle(title);
    }

    /**
     * Returns the year the book was published.
     *
     * @return the year, or null if it is unknown
     */
    public Integer getPublicationYear() {
        return publicationYear;
    }

    /**
     * Changes the year the book was published.
     *
     * @param publicationYear the new year, or null if unknown
     * @throws IllegalArgumentException if the year is not realistic
     */
    public void setPublicationYear(Integer publicationYear) {

        // Uses the same check as the constructor.
        this.publicationYear = requireValidYear(publicationYear);
    }

    /**
     * Returns the reading status of the book.
     *
     * @return "Unread", "Reading" or "Read"
     */
    public String getReadingStatus() {
        return readingStatus;
    }

    /**
     * Changes the reading status, for example when the user
     * marks the book as read. Small letters and "currently reading"
     * are accepted too, and are stored as "Unread", "Reading" or "Read".
     *
     * @param readingStatus "Unread", "Reading" or "Read"
     * @throws IllegalArgumentException if the status is not one of these
     */
    public void setReadingStatus(String readingStatus) {

        // Only accepted reading statuses can be assigned.
        this.readingStatus = requireReadingStatus(readingStatus);
    }

    /**
     * Returns the writer of the book.
     *
     * @return the writer
     */
    public Writer getWriter() {
        return writer;
    }

    /**
     * Changes the writer of the book.
     *
     * @param writer the new writer
     * @throws IllegalArgumentException if the writer is null
     */
    public void setWriter(Writer writer) {
        this.writer = requireWriter(writer);
    }

    /**
     * Returns the genre of the book.
     *
     * @return the genre
     */
    public Genre getGenre() {
        return genre;
    }

    /**
     * Changes the genre of the book.
     *
     * @param genre the new genre
     * @throws IllegalArgumentException if the genre is null
     */
    public void setGenre(Genre genre) {
        this.genre = requireGenre(genre);
    }

    /**
     * Returns the bookshelf where the book is stored.
     *
     * @return the bookshelf
     */
    public Bookshelf getBookshelf() {
        return bookshelf;
    }

    /**
     * Moves the book to another bookshelf.
     *
     * @param bookshelf the new bookshelf
     * @throws IllegalArgumentException if the bookshelf is null
     */
    public void setBookshelf(Bookshelf bookshelf) {
        this.bookshelf = requireBookshelf(bookshelf);
    }


    // ===== Validation =====

    /**
     * Checks that the ID is above 0.
     * IDs made by MySQL's AUTO_INCREMENT start at 1, so 0 or a
     * negative number can never be a real book ID.
     *
     * @param id the ID to check
     * @return the same ID if it is valid
     * @throws IllegalArgumentException if the ID is 0 or negative
     */
    private static int requirePositiveId(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Book id must be positive, but was " + id + ".");
        }

        return id;
    }

    /**
     * Checks that the title is not null, empty or too long.
     * trim() removes whitespace at the beginning and end.
     * The method returns the cleaned title if it is valid.
     *
     * @param title the title to check
     * @return the trimmed title
     * @throws IllegalArgumentException if the title is empty or too long
     */
    private static String requireTitle(String title) {

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException(
                    "Book title is required.");
        }

        // The title column in the database is VARCHAR(200).
        if (title.trim().length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException(
                    "Book title can be at most " + MAX_TITLE_LENGTH + " characters.");
        }

        return title.trim();
    }

    /**
     * Checks that the publication year makes sense.
     * The year may be null (unknown). Otherwise it has to be between 1
     * and the current year, so a typo like 20077 is caught right away.
     *
     * @param year the year to check
     * @return the same year if it is valid
     * @throws IllegalArgumentException if the year is outside that range
     */
    private static Integer requireValidYear(Integer year) {

        // Year.now() gives the current year, so this check
        // keeps working without changes next year too.
        int currentYear = Year.now().getValue();

        if (year != null && (year < 1 || year > currentYear)) {
            throw new IllegalArgumentException(
                    "Publication year must be between 1 and "
                    + currentYear + ".");
        }

        return year;
    }

    /**
     * Checks the reading status and returns it exactly the way the
     * database stores it: "Unread", "Reading" or "Read".
     *
     * The status may come in small or capital letters, and "currently
     * reading" means the same as "Reading". This way a status from the
     * view, for example "read", is always saved as "Read", which is the
     * value the reading_status column in the database accepts.
     *
     * @param status the status to check
     * @return "Unread", "Reading" or "Read"
     * @throws IllegalArgumentException if the status is null or unknown
     */
    private static String requireReadingStatus(String status) {

        if (status != null) {

            // Removes spaces at the ends and ignores upper and lower case.
            switch (status.trim().toLowerCase(Locale.ROOT)) {
                case "unread":
                    return STATUS_UNREAD;
                case "reading":
                case "currently reading":
                    return STATUS_READING;
                case "read":
                    return STATUS_READ;
                default:
                    break;
            }
        }

        // Throws an exception if the status is null or not one of the three.
        throw new IllegalArgumentException(
                "Reading status must be Unread, Reading or Read.");
    }

    /**
     * Checks that the book has a writer.
     *
     * @param writer the writer to check
     * @return the same writer if it is not null
     * @throws IllegalArgumentException if the writer is null
     */
    private static Writer requireWriter(Writer writer) {
        if (writer == null) {
            throw new IllegalArgumentException("Please choose a writer for the book.");
        }
        return writer;
    }

    /**
     * Checks that the book has a genre.
     *
     * @param genre the genre to check
     * @return the same genre if it is not null
     * @throws IllegalArgumentException if the genre is null
     */
    private static Genre requireGenre(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("Please choose a genre for the book.");
        }
        return genre;
    }

    /**
     * Checks that the book has a bookshelf.
     *
     * @param bookshelf the bookshelf to check
     * @return the same bookshelf if it is not null
     * @throws IllegalArgumentException if the bookshelf is null
     */
    private static Bookshelf requireBookshelf(Bookshelf bookshelf) {
        if (bookshelf == null) {
            throw new IllegalArgumentException("Please choose a bookshelf for the book.");
        }
        return bookshelf;
    }


    // ===== Object methods =====

    /**
     * Returns the book title when the object is converted to a string.
     * For example, System.out.println(book) prints the title, and a
     * JavaFX ComboBox or ListView shows the title of each book.
     *
     * @return the title of the book
     */
    @Override
    public String toString() {
        return title;
    }

    /**
     * Defines when two Book objects are considered equal.
     * Two books are equal if both have been saved and have the same ID,
     * because then they are the same row in the database. A book that
     * has not been saved yet (ID 0) is only equal to itself.
     *
     * @param o the object to compare with
     * @return true if both objects are the same saved book
     */
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

    /**
     * Provides a hash code for the Book class.
     * Objects considered equal by equals() must have the same hash code.
     *
     * We return the same value for every book on purpose. The ID changes
     * from 0 to a real number when the book is saved, so a hash code based
     * on the ID would change as well. Then a book that was put in a HashSet
     * before it was saved could not be found in the set afterwards.
     * A fixed value avoids this. It makes a HashSet a little slower, but
     * that does not matter for a private library.
     *
     * @return the same hash code for every book
     */
    @Override
    public int hashCode() {
        return Book.class.hashCode();
    }
}