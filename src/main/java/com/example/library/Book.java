package com.example.library;

/**
 * Represents a single book in the library catalogue.  A book has an
 * immutable ID, title and author, and an availability flag that
 * indicates whether it is currently checked out.
 */
public class Book {
    private final int id;
    private final String title;
    private final String author;
    private boolean available;

    /**
     * Creates a new book with the given properties.  New books are
     * available by default.
     *
     * @param id     unique identifier
     * @param title  the book title
     * @param author the book author
     */
    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.available = true;
    }

    /**
     * Returns the unique identifier for this book.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the title of the book.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the author of the book.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Returns {@code true} if the book is currently available for
     * checkout.
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Sets the availability flag.  Package‑private to restrict
     * modifications to classes within this package.
     */
    void setAvailable(boolean available) {
        this.available = available;
    }
}