package hu.progmasters.library.exceptionhandling;

public class BookHasExemplarsException extends RuntimeException {
    private final int bookId;

    public BookHasExemplarsException(int bookId) {
        this.bookId = bookId;
    }

    public int getBookId() {
        return bookId;
    }
}
