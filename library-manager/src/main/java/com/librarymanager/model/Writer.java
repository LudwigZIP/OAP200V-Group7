package com.librarymanager.model;

/**
 * Model class representing a writer in the Library Manager application.
 *
 * @author Mia Bro Auganes
 */

public class Writer {

    private int id;
    private String firstName;
    private String lastName;
    private String nationality;

    // ===== Constructors ======
    /**
     * Full constructor
     * Used when fetching writer from the database
     * 
     * @param id          PK for writer row
     * @param firstName   display first name of the writer
     * @param lastName    display last name of the writer
     * @param nationality display nationality of the writer
     */
    public Writer(int id, String firstName, String lastName, String nationality) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
    }

    /**
     * Constructor for inserts
     * Used when adding writer to the database
     * 
     * @param firstName   display the first name of the writer
     * @param lastName    display the last name of the writer
     * @param nationality display nationality of the writer
     */
    public Writer(String firstName, String lastName, String nationality) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
    }

    // ===== Getters and setters ======

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    // Returns the writer's name when the object is displayed as text in for example
    // ListView
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
