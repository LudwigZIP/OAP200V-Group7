package com.librarymanager.model;

/**
 * Domain entity representing a physical storage location for books,
 * e.g., a shelf in a particular room.
 *
 * <p>Maps directly to the {@code bookshelf} table in the MySQL database
 * ({@code bookshelfId}, {@code bookshelfName}, {@code bookshelfRoom}, and
 * {@code BookshelfNumber}).</p>
 *
 * <p>Author: [Thomas Mølnvik] — responsible for domain model design
 * and bookshelf-related persistence logic.</p>
 */
public class Bookshelf {

    // ===== Fields =====

    // Primary key. Stays 0 as long as the object has not been saved to the
    // database (int defaults to 0). The database generates the real value.
    private int id;

    // Display name of the shelf. Should always have a value.
    private String name;

    // The room the shelf is located in. May be null (optional).
    private String room;

    // Shelf number. Uses Integer (not int) because it may be null,
    // whereas a primitive int must always hold a value.
    private Integer shelfNumber;

    // ===== Constructors =====

    /**
     * Creates a new, unpersisted Bookshelf.
     *
     * @param name        display name of the shelf, must not be null or blank
     * @param room        room where the shelf is located, may be null
     * @param shelfNumber numeric identifier of the shelf, may be null
     */
    // Used when creating a NEW shelf that does not yet exist in the database.
    // No id is passed in, since the database generates it (auto-increment)
    // when the row is inserted. Until then, id remains 0.
    public Bookshelf(String name, String room, Integer shelfNumber) {
        this.name = requireName(name);
        this.room = room;
        this.shelfNumber = shelfNumber;
    }

    /**
     * Reconstructs a Bookshelf that already exists in the database.
     *
     * @param id          primary key of the bookshelf row
     * @param name        display name of the shelf
     * @param room        room where the shelf is located, may be null
     * @param shelfNumber numeric identifier of the shelf, may be null
     */
    // Used when reading an EXISTING row from the database and building an object from it.
    // The id is already known, so it is passed in.
    // This is constructor overloading: two constructors with the same name
    // but different parameter lists.
    public Bookshelf(int id, String name, String room, Integer shelfNumber) {
        if (id < 0) {
            throw new IllegalArgumentException("Bookshelf id cannot be negative.");
        }
        this.id = id;
        this.name = requireName(name);
        this.room = room;
        this.shelfNumber = shelfNumber;
    }

    /**
     * Returns the database ID, or 0 while this bookshelf is not persisted.
     *
     * @return the bookshelf ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the generated database ID after the bookshelf has been inserted.
     *
     * @param id the positive database ID
     */
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Persisted bookshelf id must be positive.");
        }
        this.id = id;
    }

    /**
     * Returns the display name of this bookshelf.
     *
     * @return the bookshelf name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the display name, which must not be blank.
     *
     * @param name the new bookshelf name
     */
    public void setName(String name) {
        this.name = requireName(name);
    }

    /**
     * Returns the room where this bookshelf is located.
     *
     * @return the room, or null when it has not been set
     */
    public String getRoom() {
        return room;
    }

    /**
     * Sets the room where this bookshelf is located.
     *
     * @param room the room, or null when it is not specified
     */
    public void setRoom(String room) {
        this.room = room;
    }

    /**
     * Returns the optional shelf number.
     *
     * @return the shelf number, or null when it has not been set
     */
    public Integer getShelfNumber() {
        return shelfNumber;
    }

    /**
     * Sets the optional shelf number.
     *
     * @param shelfNumber the shelf number, or null when it is not specified
     */
    public void setShelfNumber(Integer shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    /**
     * Validates and trims a bookshelf name.
     */
    private static String requireName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Bookshelf name is required.");
        }
        return name.trim();
    }

    /**
     * Returns a readable label for use in text-based controls.
     *
     * @return the bookshelf name, optionally followed by its room
     */
    @Override
    public String toString() {
        return room != null ? name + " (" + room + ")" : name;
    }

    /**
     * Compares persisted bookshelves by database ID. Unsaved instances compare
     * equal only when they are the same object.
     *
     * @param o the object to compare
     * @return true when both references identify the same bookshelf
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bookshelf)) {
            return false;
        }
        Bookshelf bookshelf = (Bookshelf) o;
        return id > 0 && bookshelf.id > 0 && id == bookshelf.id;
    }

    /**
     * Returns a hash code that remains stable when a new bookshelf receives an ID.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Bookshelf.class.hashCode();
    }
}
