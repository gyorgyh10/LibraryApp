package hu.progmasters.library.exceptionhandling;

public class ExemplarNotFoundException extends RuntimeException {
    private final int exemplarId;

    public ExemplarNotFoundException(int exemplarId) {
        this.exemplarId = exemplarId;
    }

    public int getExemplarId() {
        return exemplarId;
    }
}
