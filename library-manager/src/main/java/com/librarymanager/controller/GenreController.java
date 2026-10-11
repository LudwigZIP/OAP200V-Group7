package com.librarymanager.controller;

// Import statements for required classes and interfaces.
import java.util.List;
import java.util.Objects;

// Import statements for the DAO and model classes used in this controller.
import com.librarymanager.dao.GenreDAO;
import com.librarymanager.model.Genre;

/**
 * @author: Erika Marie Gåsland
 */

// This class is the controller for the Genre feature.
// It acts as the middle layer between the user interface and the database access layer.
public class GenreController {
    
    // This field holds the DAO object used to communicate with the database.
    // DAO = Data Access Object, which stores and retrieves Genre data.
    private final GenreDAO genreDAO;

    // Constructor: creates the controller and stores the DAO instance.
    // Objects.requireNonNull checks that the DAO is not null.
    public GenreController(GenreDAO genreDAO) {
        this.genreDAO = Objects.requireNonNull(genreDAO, "genreDAO");
    }

    // This method gets all genres from the database and returns them as a list.
    // It is used when the app wants to display all genres in a table or list.
    public List<Genre> getAllGenres() {
        return genreDAO.findAll();
    }

    // This method adds a new genre to the database.
    // It accepts a Genre object and passes it to the DAO create method.
    public Genre addGenre(Genre genre) {
        return genreDAO.create(Objects.requireNonNull(genre, "genre"));
    }
    
    // This method updates an existing genre in the database.
    // It accepts a Genre object and passes it to the DAO update method.   
     // It is used when the user edits a genre name or other information. 
    public void updateGenre(Genre genre) {
        genreDAO.update(Objects.requireNonNull(genre, "genre"));
    }

    // This method deletes a genre from the database by its ID.
    // It is used when the user removes a genre.
    public void removeGenre(int id) {
        genreDAO.delete(id);
    }
    
}
