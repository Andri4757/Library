package com.example.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Library} class and related domain classes.
 *
 * These tests cover both positive and negative scenarios, ensuring
 * proper behaviour of borrowing limits, search functionality,
 * availability flags, and duplicate handling.
 */
public class LibraryTest {
    private Library library;
    private User user1;
    private User user2;
    private Book book1;
    private Book book2;
    private Book book3;
    private Book book4;

    @BeforeEach
    void setUp() {
        library = new Library();
        book1 = new Book(1, "Effective Java", "Joshua Bloch");
        book2 = new Book(2, "Clean Code", "Robert C. Martin");
        book3 = new Book(3, "Java Concurrency in Practice", "Brian Goetz");
        book4 = new Book(4, "Design Patterns", "Gang of Four");
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);
        library.addBook(book4);
        user1 = new User(1, "Alice", 2);
        user2 = new User(2, "Bob", 1);
        library.addUser(user1);
        library.addUser(user2);
    }

    @Test
    void searchBooksByTitleReturnsMatches() {
        List<Book> results = library.searchBooksByTitle("Java");
        assertEquals(2, results.size());
        assertTrue(results.contains(book1));
        assertTrue(results.contains(book3));
    }

    @Test
    void searchBooksByTitleIsCaseInsensitive() {
        List<Book> results = library.searchBooksByTitle("clean");
        assertEquals(1, results.size());
        assertEquals(book2, results.get(0));
    }

    @Test
    void issueBookWhenAvailableAndUserUnderLimitSucceeds() {
        assertTrue(library.issueBook(user1.getId(), book1.getId()));
        assertFalse(book1.isAvailable());
        assertTrue(user1.getBorrowedBooks().contains(book1));
    }

    @Test
    void issueBookWhenBookAlreadyBorrowedFails() {
        assertTrue(library.issueBook(user1.getId(), book1.getId()));
        assertFalse(library.issueBook(user2.getId(), book1.getId()));
    }

    @Test
    void issueBookWhenUserAtLimitFails() {
        assertTrue(library.issueBook(user2.getId(), book2.getId()));
        // user2 has limit 1, so second borrow should fail
        assertFalse(library.issueBook(user2.getId(), book3.getId()));
    }

    @Test
    void returnBookRestoresAvailabilityAndRemovesFromUser() {
        library.issueBook(user1.getId(), book2.getId());
        assertTrue(library.returnBook(user1.getId(), book2.getId()));
        assertTrue(book2.isAvailable());
        assertFalse(user1.getBorrowedBooks().contains(book2));
    }

    @Test
    void returnBookNotBorrowedByUserFails() {
        library.issueBook(user1.getId(), book3.getId());
        assertFalse(library.returnBook(user2.getId(), book3.getId()));
    }

    @Test
    void removeUserPreventsFurtherBorrowing() {
        assertTrue(library.issueBook(user1.getId(), book4.getId()));
        assertTrue(library.removeUser(user1.getId()));
        // Even though the book is still checked out, issuing further books should fail
        assertFalse(library.issueBook(user1.getId(), book1.getId()));
    }

    @Test
    void removeBookRemovesFromLibraryAndUsers() {
        library.issueBook(user1.getId(), book4.getId());
        assertTrue(library.removeBook(book4.getId()));
        assertNull(library.findBookById(book4.getId()));
        assertFalse(user1.getBorrowedBooks().contains(book4));
    }

    @Test
    void addBookWithDuplicateIdFails() {
        Book duplicate = new Book(1, "Duplicate", "Author");
        assertFalse(library.addBook(duplicate));
    }

    @Test
    void addUserWithDuplicateIdFails() {
        User duplicateUser = new User(1, "Alice2", 3);
        assertFalse(library.addUser(duplicateUser));
    }
}