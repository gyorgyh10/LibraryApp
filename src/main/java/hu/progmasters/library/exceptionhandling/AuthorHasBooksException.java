package hu.progmasters.library.exceptionhandling;

public class AuthorHasBooksException extends RuntimeException {
    private final int authorId;

    public AuthorHasBooksException(int authorId) {
        this.authorId = authorId;
    }

    public int getAuthorId() {
        return authorId;
    }
}
