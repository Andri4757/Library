# Library Management System

This project implements a basic **Library Management System** in Java.  It was built as part of the SDAT and Dev Ops combined QAP assignment and demonstrates clean code principles, automated unit testing with JUnit 5, and continuous integration via GitHub Actions.

## Features

The application models a small library with the following capabilities:

* **Book management**: add and remove books from the catalogue.  Each book has an ID, title, author and an availability flag.
* **User management**: register and unregister users.  Every user has a unique ID, a name and a configurable borrowing limit.
* **Borrow/return books**: issue a book to a user if it is available and the user has not reached their borrowing limit.  When returned, a book becomes available again.
* **Search**: search the catalogue by title.  Searches are case‑insensitive and return all books whose titles contain the given keyword.

## Project structure

```
library-management-system/
 ├── pom.xml                     – Maven project descriptor with JUnit dependencies
 ├── README.md                   – This file
 ├── src/
 │   ├── main/java/com/example/library/
 │   │   ├── Book.java          – Represents a single book
 │   │   ├── User.java          – Represents a library patron
 │   │   └── Library.java       – Core class that manages books and users
 │   └── test/java/com/example/library/
 │       └── LibraryTest.java   – JUnit 5 tests covering positive and negative scenarios
 └── .github/workflows/maven.yml – GitHub Actions workflow to run tests on pull requests
```

## Running the application

This project is intentionally simple and does not provide a user interface.  It focuses on the core business logic and automated tests.  To compile and run the tests locally:

```bash
mvn clean test
```

The `mvn test` command will compile the source code and execute all unit tests under `src/test/java`.

## Clean code practices

The code aims to be readable, maintainable and modular.  Here are some of the practices employed:

1. **Descriptive naming**: classes (`Book`, `User`, `Library`), methods (`issueBook`, `returnBook`, `searchBooksByTitle`) and variables use meaningful names rather than abbreviations.
2. **Single responsibility**: each class encapsulates one concept.  `Book` stores book data; `User` tracks user details and borrowed books; `Library` orchestrates the interactions between books and users.
3. **Encapsulation**: fields are `private` and exposed via getter methods.  Modification of internal state (e.g. marking a book as available) happens through clearly defined methods.  This prevents external code from putting objects into inconsistent states.
4. **Small methods**: methods perform one well‑defined task.  For example, `searchBooksByTitle` filters the book map and returns matching books without side effects.
5. **Use of collections**: immutable views are returned where appropriate (`getBorrowedBooks()` returns an unmodifiable list) to avoid unintended modifications.

## Test cases

There are more than ten unit tests in `LibraryTest.java`.  They exercise both normal and edge cases, for example:

* Searching for a book by a keyword returns the expected results.
* Borrowing an available book succeeds and marks it as unavailable.
* Borrowing a book that is already checked out fails.
* Users cannot exceed their borrowing limit (a negative scenario).
* Returning a book makes it available again and removes it from the user’s borrowed list.
* Attempting to return a book that the user never borrowed does nothing and returns `false`.
* Removing a user prevents further borrowing.
* Removing a book prevents future check outs.

Running `mvn test` locally or via GitHub Actions will report these tests.  They serve as both documentation of expected behaviour and a safety net for future changes.

## Dependencies

This project relies on **JUnit 5** for unit testing.  The JUnit Jupiter API and engine are declared in the `pom.xml` with version `5.10.0`.  JUnit 5 is a modern testing framework that supports expressive assertions and a clean annotation‑based syntax.

## GitHub Actions

The `.github/workflows/maven.yml` file configures a simple CI pipeline.  Whenever a pull request is opened against your repository, the workflow checks out the code, sets up Java 17, and runs `mvn --batch-mode test`.  If the tests pass, the workflow succeeds.  If they fail (or the configuration is incorrect), the workflow will mark the build as failed.  This provides immediate feedback on the health of the codebase.

## Notes

This project was developed and packaged in a local environment.  To use it on GitHub, create a new repository, copy the files and commit them.  Ensure that you create branches and pull requests to demonstrate a proper trunk‑based workflow.