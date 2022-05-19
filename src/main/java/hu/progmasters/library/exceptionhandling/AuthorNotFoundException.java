package hu.progmasters.library.exceptionhandling;

public class AuthorNotFoundException extends RuntimeException {
    private int authorId;

    public AuthorNotFoundException(int authorId) {
        this.authorId = authorId;
    }

    public int getAuthorId() {
        return authorId;
    }
}
