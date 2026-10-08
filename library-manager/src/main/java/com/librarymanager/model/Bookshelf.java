package com.librarymanager.model;

/**
 * Domain entity representing a physical storage location for books,
 * e.g., a shelf in a particular room.
 *
 * <p>Maps directly to the {@code bookshelf} table in the MySQL database
 * (name, room, an integer shelf number, and a free-text description).</p>
 *
 * <p>Author: [Team member name] — responsible for domain model design
 * and bookshelf-related persistence logic.</p>
 */
public class Bookshelf {

    // ===== Fields =====
    // Each field corresponds to a column in the "bookshelf" table.

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

    // Free-text description of the shelf. May be null.
    private String description;

    // ===== Constructors =====

    /**
     * Creates a new, unpersisted Bookshelf.
     *
     * @param name        display name of the shelf, must not be null or blank
     * @param room        room where the shelf is located, may be null
     * @param shelfNumber numeric identifier of the shelf, may be null
     * @param description free-text description, may be null
     */
    // Used when creating a NEW shelf that does not yet exist in the database.
    // No id is passed in, since the database generates it (auto-increment)
    // when the row is inserted. Until then, id remains 0.
    public Bookshelf(String name, String room, Integer shelfNumber, String description) {
        this.name = requireName(name);
        this.room = room;
        this.shelfNumber = shelfNumber;
        this.description = description;
    }

    /**
     * Reconstructs a Bookshelf that already exists in the database.
     *
     * @param id          primary key of the bookshelf row
     * @param name        display name of the shelf
     * @param room        room where the shelf is located, may be null
     * @param shelfNumber numeric identifier of the shelf, may be null
     * @param description free-text description, may be null
     */
    // Used when reading an EXISTING row from the database and building an object from it.
    // The id is already known, so it is passed in.
    // This is constructor overloading: two constructors with the same name
    // but different parameter lists.
    public Bookshelf(int id, String name, String room, Integer shelfNumber, String description) {
        if (id < 0) {
            throw new IllegalArgumentException("Bookshelf id cannot be negative.");
        }
        this.id = id;
        this.name = requireName(name);
        this.room = room;
        this.shelfNumber = shelfNumber;
        this.description = description;
    }

    // ===== Getters and setters =====
    // Standard encapsulation: fields are private, and other classes
    // read them via getX() and modify them via setX().

    public int getId() {
        return id;
    }

    // Sets the id after the database has generated it, e.g. right after an INSERT.
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Persisted bookshelf id must be positive.");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = requireName(name);
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Integer getShelfNumber() {
        return shelfNumber;
    }

    public void setShelfNumber(Integer shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private static String requireName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Bookshelf name is required.");
        }
        return name.trim();
    }

    // ===== Overridden methods from Object =====

    // Defines how the object is displayed as text, e.g. in a dropdown list
    // or in printed output. Returns "Name (Room)" if a room exists, otherwise just "Name".
    // Example: "Shelf A (Living room)" or "Shelf B".
    @Override
    public String toString() {
        return room != null ? name + " (" + room + ")" : name;
    }

    // Unsaved shelves have no stable database identity, so only the same
    // instance can be equal until both objects have persisted ids.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bookshelf)) return false;
        Bookshelf bookshelf = (Bookshelf) o;
        return id > 0 && bookshelf.id > 0 && id == bookshelf.id;
    }

    // Keep the hash stable when a new shelf receives its database id.
    @Override
    public int hashCode() {
        return Bookshelf.class.hashCode();
    }
}
