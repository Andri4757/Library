package com.example.library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a library patron.  A user has an immutable ID and name
 * and tracks which books they currently have checked out.  Each user
 * has a borrowing limit that controls how many books they can have at
 * once.
 */
public class User {
    private final int id;
    private final String name;
    private final int borrowingLimit;
    private final List<Book> borrowedBooks;

    /**
     * Constructs a new user.
     *
     * @param id             unique identifier
     * @param name           user name
     * @param borrowingLimit maximum number of books the user may
     *                       check out simultaneously
     */
    public User(int id, String name, int borrowingLimit) {
        this.id = id;
        this.name = name;
        this.borrowingLimit = borrowingLimit;
        this.borrowedBooks = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * Returns an immutable view of the books this user has borrowed.
     */
    public List<Book> getBorrowedBooks() {
        return Collections.unmodifiableList(borrowedBooks);
    }

    /**
     * Returns the maximum number of books this user is allowed to
     * borrow at once.
     */
    public int getBorrowingLimit() {
        return borrowingLimit;
    }

    /**
     * Returns {@code true} if the user may borrow another book,
     * based on their borrowing limit.
     */
    boolean canBorrow() {
        return borrowedBooks.size() < borrowingLimit;
    }

    /**
     * Adds a book to the user's list of borrowed books.  Package‑private
     * because only the library should modify this list.
     */
    void addBorrowedBook(Book book) {
        borrowedBooks.add(book);
    }

    /**
     * Removes a book from the user's list of borrowed books.  Package‑private
     * because only the library should modify this list.
     */
    void removeBorrowedBook(Book book) {
        borrowedBooks.remove(book);
    }

    /**
     * Returns {@code true} if this user has the given book on loan.
     */
    boolean hasBorrowed(Book book) {
        return borrowedBooks.contains(book);
    }
}