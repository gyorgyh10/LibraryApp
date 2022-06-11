package hu.progmasters.library.exceptionhandling;

public class UserNotFoundException extends RuntimeException {
    private final int userId;

    public UserNotFoundException(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }
}
