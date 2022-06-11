package hu.progmasters.library.exceptionhandling;

public class ExemplarIsInActiveBorrowingException extends RuntimeException {
    private final int exemplarId;

    public ExemplarIsInActiveBorrowingException(int exemplarId) {
        this.exemplarId = exemplarId;
    }

    public int getExemplarId() {
        return exemplarId;
    }

}
