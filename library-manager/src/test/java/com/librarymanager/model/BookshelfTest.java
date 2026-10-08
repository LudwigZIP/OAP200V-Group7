package com.librarymanager.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class BookshelfTest {

    @Test
    public void unsavedShelvesHaveIdentityEqualityAndStableHashCode() {
        Bookshelf first = new Bookshelf("A", null, null, null);
        Bookshelf second = new Bookshelf("B", null, null, null);
        Set<Bookshelf> shelves = new HashSet<>();
        shelves.add(first);

        assertNotEquals(first, second);
        first.setId(1);
        assertTrue(shelves.contains(first));
    }

    @Test
    public void persistedShelvesCompareById() {
        Bookshelf first = new Bookshelf(7, "A", null, null, null);
        Bookshelf second = new Bookshelf(7, "Renamed", null, null, null);
        Bookshelf different = new Bookshelf(8, "A", null, null, null);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertFalse(first.equals(different));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsBlankName() {
        new Bookshelf("  ", null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsBlankNameOnUpdate() {
        Bookshelf shelf = new Bookshelf("A", null, null, null);
        shelf.setName(" ");
    }
}
