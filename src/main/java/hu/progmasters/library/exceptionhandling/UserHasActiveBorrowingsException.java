package hu.progmasters.library.exceptionhandling;

public class UserHasActiveBorrowingsException extends RuntimeException {
    private final int userId;

    public UserHasActiveBorrowingsException(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }
}
