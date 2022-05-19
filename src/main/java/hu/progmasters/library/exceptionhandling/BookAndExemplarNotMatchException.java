package hu.progmasters.library.exceptionhandling;

public class BookAndExemplarNotMatchException extends RuntimeException {
    private String bookIdAndExemplarId;

    public BookAndExemplarNotMatchException(String bookIdAndExemplarId) {
        this.bookIdAndExemplarId = bookIdAndExemplarId;
    }

    public String getBookIdAndExemplarId() {
        return bookIdAndExemplarId;
    }
}
