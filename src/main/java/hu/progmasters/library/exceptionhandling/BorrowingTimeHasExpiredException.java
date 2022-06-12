package hu.progmasters.library.exceptionhandling;

public class BorrowingTimeHasExpiredException extends RuntimeException {
    private final int borrowingId;

    public BorrowingTimeHasExpiredException(int borrowingId) {
        this.borrowingId = borrowingId;
    }

    public int getBorrowingId() {
        return borrowingId;
    }
}
