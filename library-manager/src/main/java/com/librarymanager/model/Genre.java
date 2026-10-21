package com.librarymanager.model;

/**
 * Model class representing a Genre entity in the application.
 * A Genre consists of two pieces of information:
 * - a unique database-generated ID
 * - a name describing the genre (e.g., "Science Fiction", "Romance")
 *
 * This class is part of the application's model layer and is used by
 * controllers, DAO implementations, and JavaFX views to represent
 * genre data consistently throughout the system.
 *
 * Constructors:
 * - Genre(String name):
 *      Used when creating a new genre before it is saved to the database.
 *      The ID is not yet known and will be assigned automatically by the database.
 *
 * - Genre(int id, String name):
 *      Used when reading an existing genre from the database,
 *      where both ID and name are already known.
 *
 * Getters and setters provide access to the genre's fields and allow
 * the controller or DAO to modify or retrieve values when needed.
 *
 * The toString() method returns the genre's name, making the object
 * display-friendly in UI components such as lists, tables, or dropdown menus.
 * This ensures that when a Genre object is shown in the interface,
 * the user sees the genre name rather than a technical object reference.
 * 
 * @author: Erika Marie Gåsland
 */


public class Genre {
    
    //This is the unique ID for the genre in the database. 
    //Each row in the genre table has its own unique ID. 
    private int id;
    //This is the name of the genre, such as "Science Fiction", "Romance", or "Mystery".
    private String name;

    //Constructor used when creating a new genre that does not yet have an ID assigned by the database.
    //This constructor is useful when you want to create a new genre and save it to the database, where the ID will be generated automatically.
    //@param name the name of the genre
    public Genre(String name) {
        this.name = name;
    }
    //Constructor used when reading a genre from the database, where the ID is already known.
    //@param id the unique ID of the genre
    //@param name the name of the genre
    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }
    // Getters and setters

    // Returns the unique ID of the genre
    // @return the ID of the genre
    public int getId() {
        return id;
    }

    // Sets the unique ID of the genre
    //@param id the new ID to set for the genre
    public void setId(int id) {
        this.id = id;
    }

    // Returns the name of the genre
    // @return the name of the genre
    public String getName() {
        return name;
    }

    // Sets the name of the genre
    //@param name the new name to set for the genre
    public void setName(String name) {
        this.name = name;
    }
    //Returns a string representation of the genre, which is its name.
    //This method tells Java how the object should be represented as a string.
    //This is useful when showing the genre in a user interface, such as a dropdown menu, table or a list. 
    // @return the name of the genre as a string
    @Override
    public String toString() {
        return name;
       // return "Genre{" +
        //        "id=" + id +
        //        ", name='" + name + '\'' +
        //        '}';
    }
}
