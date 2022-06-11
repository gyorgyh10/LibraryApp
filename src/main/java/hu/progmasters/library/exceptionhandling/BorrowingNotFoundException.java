package hu.progmasters.library.exceptionhandling;

public class BorrowingNotFoundException extends RuntimeException {
    private final int borrowingId;

    public BorrowingNotFoundException(int borrowingId) {
        this.borrowingId = borrowingId;
    }

    public int getBorrowingId() {
        return borrowingId;
    }
}
