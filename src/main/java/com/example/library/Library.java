package com.example.library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The central class that manages books and users.  This class
 * encapsulates the logic for adding/removing books and users, issuing
 * and returning books, and searching the catalogue.
 */
public class Library {
    private final Map<Integer, Book> books;
    private final Map<Integer, User> users;

    public Library() {
        this.books = new HashMap<>();
        this.users = new HashMap<>();
    }

    /**
     * Adds a new book to the library.  If a book with the same ID already
     * exists, the method returns {@code false} and the existing book
     * remains unchanged.
     *
     * @param book the book to add
     * @return {@code true} if added successfully, {@code false} if a
     *         duplicate ID exists
     */
    public boolean addBook(Book book) {
        if (books.containsKey(book.getId())) {
            return false;
        }
        books.put(book.getId(), book);
        return true;
    }

    /**
     * Removes a book from the library by ID.  When a book is removed,
     * it is also removed from any user's borrowed list.  The book's
     * availability status becomes irrelevant because it is no longer
     * tracked by the library.
     *
     * @param bookId the ID of the book to remove
     * @return {@code true} if the book was removed, {@code false} if
     *         no such book existed
     */
    public boolean removeBook(int bookId) {
        Book book = books.remove(bookId);
        if (book == null) {
            return false;
        }
        // Remove from any user's borrowed list
        for (User user : users.values()) {
            if (user.hasBorrowed(book)) {
                user.removeBorrowedBook(book);
            }
        }
        return true;
    }

    /**
     * Registers a new user.  If a user with the same ID already
     * exists, the method returns {@code false}.
     *
     * @param user the new user
     * @return {@code true} if the user was added, {@code false} if the
     *         ID is already in use
     */
    public boolean addUser(User user) {
        if (users.containsKey(user.getId())) {
            return false;
        }
        users.put(user.getId(), user);
        return true;
    }

    /**
     * Removes a user by ID.  Any books they had borrowed remain
     * unavailable until explicitly returned via {@link #returnBook(int, int)}.
     *
     * @param userId the ID of the user to remove
     * @return {@code true} if the user was removed, {@code false} if
     *         no such user existed
     */
    public boolean removeUser(int userId) {
        User user = users.remove(userId);
        return user != null;
    }

    /**
     * Finds a book by its ID.
     *
     * @param bookId the book ID
     * @return the book or {@code null} if not found
     */
    public Book findBookById(int bookId) {
        return books.get(bookId);
    }

    /**
     * Finds a user by ID.
     *
     * @param userId the user ID
     * @return the user or {@code null} if not found
     */
    public User findUserById(int userId) {
        return users.get(userId);
    }

    /**
     * Searches the catalogue for books whose titles contain the
     * specified keyword (case‑insensitive).
     *
     * @param keyword the search keyword
     * @return a list of matching books; may be empty but never null
     */
    public List<Book> searchBooksByTitle(String keyword) {
        if (keyword == null) {
            return Collections.emptyList();
        }
        String lower = keyword.toLowerCase(Locale.ROOT);
        List<Book> matches = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getTitle().toLowerCase(Locale.ROOT).contains(lower)) {
                matches.add(book);
            }
        }
        return matches;
    }

    /**
     * Issues a book to a user.  The operation succeeds only if the
     * user and book exist, the book is currently available and the
     * user has not reached their borrowing limit.
     *
     * @param userId the ID of the user
     * @param bookId the ID of the book
     * @return {@code true} if the book was issued, {@code false}
     *         otherwise
     */
    public boolean issueBook(int userId, int bookId) {
        User user = users.get(userId);
        Book book = books.get(bookId);
        if (user == null || book == null) {
            return false;
        }
        if (!book.isAvailable()) {
            return false;
        }
        if (!user.canBorrow()) {
            return false;
        }
        // Mark book as checked out and assign to user
        book.setAvailable(false);
        user.addBorrowedBook(book);
        return true;
    }

    /**
     * Returns a book that was previously issued to a user.  The
     * operation succeeds only if the user and book exist and the user
     * actually has the book checked out.  On success the book becomes
     * available again and is removed from the user's borrowed list.
     *
     * @param userId the user ID
     * @param bookId the book ID
     * @return {@code true} if the book was returned, {@code false}
     *         otherwise
     */
    public boolean returnBook(int userId, int bookId) {
        User user = users.get(userId);
        Book book = books.get(bookId);
        if (user == null || book == null) {
            return false;
        }
        if (!user.hasBorrowed(book)) {
            return false;
        }
        // Mark as available and remove from user
        book.setAvailable(true);
        user.removeBorrowedBook(book);
        return true;
    }

    /**
     * Returns an immutable list of all books currently registered in
     * the library.  Provided for testing and inspection purposes.
     */
    public List<Book> getAllBooks() {
        return Collections.unmodifiableList(new ArrayList<>(books.values()));
    }

    /**
     * Returns an immutable list of all registered users.  Provided
     * for testing and inspection purposes.
     */
    public List<User> getAllUsers() {
        return Collections.unmodifiableList(new ArrayList<>(users.values()));
    }
}