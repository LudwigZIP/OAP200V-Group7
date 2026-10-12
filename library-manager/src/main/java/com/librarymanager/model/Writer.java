package com.librarymanager.model;

public class Writer {
    
    private int writerId;
    private String writerName;
    private String writerNationality;

    // ===== Constructors ======
    /**
     * Full constructor
     * Used when fetching writer from the database
     * 
     * @param writerId PK for writer row
     * @param writerName display name of the writer
     * @param writerNationality display nationality of the writer
     */
    public Writer(int writerId, String writerName, String writerNationality) {
        this.writerId = writerId;
        this.writerName = writerName;
        this.writerNationality = writerNationality;
    }

    /**
     * Constructor for inserts
     * Used when adding writer to the database
     * 
     * @param writerName display name of the writer
     * @param writerNationality display nationality of the writer
     */
    public Writer(String writerName, String writerNationality) {
        this.writerName = writerName;
        this.writerNationality = writerNationality;
    }

    // ===== Getters and setters ======

    public int getWriterId() {
        return writerId;
    }

    public void setWriterId(int writerId) {
        this.writerId = writerId;
    }

    public String getWriterName() {
        return writerName;
    }
    
    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    public String getWriterNationality() {
        return writerNationality;
    }
    
    public void setWriterNationality(String writerNationality) {
        this.writerNationality = writerNationality;
    }

    // Returns the writer's name when the object is displayed as text in for example ListView 
    @Override 
    public String toString() {
        return writerName;
    }
}



