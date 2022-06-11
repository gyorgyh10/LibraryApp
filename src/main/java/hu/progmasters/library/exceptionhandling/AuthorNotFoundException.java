package hu.progmasters.library.exceptionhandling;

public class AuthorNotFoundException extends RuntimeException {
    private final int authorId;

    public AuthorNotFoundException(int authorId) {
        this.authorId = authorId;
    }

    public int getAuthorId() {
        return authorId;
    }
}
