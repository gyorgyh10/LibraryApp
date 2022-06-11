package hu.progmasters.library.exceptionhandling;

public class BookAndExemplarNotMatchException extends RuntimeException {
    private final String bookIdAndExemplarId;

    public BookAndExemplarNotMatchException(String bookIdAndExemplarId) {
        this.bookIdAndExemplarId = bookIdAndExemplarId;
    }

    public String getBookIdAndExemplarId() {
        return bookIdAndExemplarId;
    }
}
