package hu.progmasters.library.exceptionhandling;

public class BookNotFoundException extends RuntimeException {
    private final int bookId;

    public BookNotFoundException(int bookId) {
        this.bookId = bookId;
    }

    public int getBookId() {
        return bookId;
    }
}
