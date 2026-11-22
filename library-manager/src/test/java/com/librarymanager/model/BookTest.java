package com.librarymanager.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test class for the Book model
 * 
 * @author Mia Bro Auganes
 */
public class BookTest {

    // Test that book cannot be created with empty Title
    @Test(expected = IllegalArgumentException.class)
    public void rejectEmptyTitle() {
        new Book("", 2026, "unread", null, null, null);
    }

    // Test that book cannot be created with invalid Publication year
    @Test(expected = IllegalArgumentException.class)
    public void rejectInvalidPublicationYear() {
        new Book("Book title", 3000, "unread", null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectInvalidReadingStatus() {
        new Book("Book title", 2026, null, null, null, null);
    }

    // Test that the book returns the correct title
    @Test
    public void returnEqualTitle() {
        Writer writer = new Writer("Firstname", "Lastname", "Nationality");
        Genre genre = new Genre(1, "Genre name");
        Bookshelf bookshelf = new Bookshelf("Bookshelf A", "Room", 1);
        Book book = new Book("Book title", 2026, "unread", writer, genre, bookshelf);
        assertEquals("Book title", book.getTitle());
    }
    
}
