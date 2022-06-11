package hu.progmasters.library.exceptionhandling;

public class ExemplarNotBorrowableException extends RuntimeException {
    private final int exemplarId;

    public ExemplarNotBorrowableException(int exemplarId) {
        this.exemplarId = exemplarId;
    }

    public int getExemplarId() {
        return exemplarId;
    }

}
