package com.librarymanager.model;

/**
 * Represents a bookshelf in the Library Manager application.
 *
 * @author Thomas Mølnvik
 */
public class Bookshelf {

    // Zero indicates that this bookshelf has not yet been saved.
    private int id;

    // Required display name of the bookshelf.
    private String name;

    // Optional room where the bookshelf is located.
    private String room;

    // Optional shelf number; Integer allows the value to be null.
    private Integer shelfNumber;

    /**
     * Constructor for new bookshelves.
     * Used when adding a bookshelf to the database.
     *
     * @param name the name of the bookshelf
     * @param room the room where the bookshelf is located
     * @param shelfNumber the number assigned to the bookshelf
     */
    public Bookshelf(String name, String room, Integer shelfNumber) {
        this.name = requireName(name);
        this.room = room;
        this.shelfNumber = shelfNumber;
    }

    /**
     * Full constructor.
     * Used when retrieving a bookshelf from the database.
     *
     * @param id the primary key of the bookshelf
     * @param name the name of the bookshelf
     * @param room the room where the bookshelf is located
     * @param shelfNumber the number assigned to the bookshelf
     */
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
     * Returns the bookshelf ID.
     *
     * @return the bookshelf ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the bookshelf ID.
     *
     * @param id the bookshelf ID
     */
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Persisted bookshelf id must be positive.");
        }
        this.id = id;
    }

    /**
     * Returns the bookshelf name.
     *
     * @return the bookshelf name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the bookshelf name.
     *
     * @param name the new bookshelf name
     */
    public void setName(String name) {
        this.name = requireName(name);
    }

    /**
     * Returns the bookshelf room.
     *
     * @return the room where the bookshelf is located
     */
    public String getRoom() {
        return room;
    }

    /**
     * Sets the bookshelf room.
     *
     * @param room the room where the bookshelf is located
     */
    public void setRoom(String room) {
        this.room = room;
    }

    /**
     * Returns the shelf number.
     *
     * @return the number assigned to the bookshelf
     */
    public Integer getShelfNumber() {
        return shelfNumber;
    }

    /**
     * Sets the shelf number.
     *
     * @param shelfNumber the number assigned to the bookshelf
     */
    public void setShelfNumber(Integer shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    /**
     * Checks that the bookshelf name is not blank.
     *
     * @param name the name to check
     * @return the name without surrounding whitespace
     * @throws IllegalArgumentException if the name is blank
     */
    private static String requireName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Bookshelf name is required.");
        }
        return name.trim();
    }

    /**
     * Returns the bookshelf name and room as text.
     *
     * @return the bookshelf name and room
     */
    @Override
    public String toString() {
        return room != null ? name + " (" + room + ")" : name;
    }

    /**
     * Checks whether this bookshelf is equal to another object.
     *
     * @param o the object to compare with this bookshelf
     * @return true if both objects have the same bookshelf ID
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
     * Returns the hash code of this bookshelf.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Bookshelf.class.hashCode();
    }
}
