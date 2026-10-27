package com.librarymanager.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

/**
 * Test class for the Bookshelf model.
 *
 * @author Thomas Mølnvik
 */
public class BookshelfTest {

    /**
     * Tests that unsaved bookshelves are compared by object identity.
     */
    @Test
    public void unsavedShelvesHaveIdentityEqualityAndStableHashCode() {
        Bookshelf first = new Bookshelf("A", null, null);
        Bookshelf second = new Bookshelf("B", null, null);
        Set<Bookshelf> shelves = new HashSet<>();
        shelves.add(first);

        assertNotEquals(first, second);
        first.setId(1);
        assertTrue(shelves.contains(first));
    }

    /**
     * Tests that saved bookshelves are compared by ID.
     */
    @Test
    public void persistedShelvesCompareById() {
        Bookshelf first = new Bookshelf(7, "A", null, null);
        Bookshelf second = new Bookshelf(7, "Renamed", null, null);
        Bookshelf different = new Bookshelf(8, "A", null, null);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertFalse(first.equals(different));
    }

    /**
     * Tests that a bookshelf cannot be created with a blank name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void rejectsBlankName() {
        new Bookshelf("  ", null, null);
    }

    /**
     * Tests that a bookshelf cannot be updated with a blank name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void rejectsBlankNameOnUpdate() {
        Bookshelf shelf = new Bookshelf("A", null, null);
        shelf.setName(" ");
    }
}
