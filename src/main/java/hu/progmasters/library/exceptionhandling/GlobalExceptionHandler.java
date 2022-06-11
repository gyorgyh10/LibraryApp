package hu.progmasters.library.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationError>> handleValidationException(MethodArgumentNotValidException exception) {
        List<ValidationError> validationErrors = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<List<ValidationError>> handleBookNotFound(BookNotFoundException exception) {
        ValidationError validationError = new ValidationError("bookId",
                "Book not found with id: "+exception.getBookId());
        return new ResponseEntity<>(List.of(validationError), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExemplarNotFoundException.class)
    public ResponseEntity<List<ValidationError>> handleExemplarNotFound(ExemplarNotFoundException exception) {
        ValidationError validationError = new ValidationError("exemplarId",
                "Exemplar not found with id: "+exception.getExemplarId());
        return new ResponseEntity<>(List.of(validationError), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookAndExemplarNotMatchException.class)
    public ResponseEntity<List<ValidationError>> handleBookAndExemplarNotMatch
                                                            (BookAndExemplarNotMatchException exception) {
        ValidationError validationError = new ValidationError("bookIdAndExemplarId",
                "Book doesn't has this exemplar: "+exception.getBookIdAndExemplarId());
        return new ResponseEntity<>(List.of(validationError), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<List<ValidationError>> handleAuthorNotFound(AuthorNotFoundException exception) {
        ValidationError validationError = new ValidationError("authorId",
                "Author not found with id: "+exception.getAuthorId());
        return new ResponseEntity<>(List.of(validationError), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<List<ValidationError>> handleUserNotFound(UserNotFoundException exception) {
        ValidationError validationError = new ValidationError("userId",
                "User not found with id: "+exception.getUserId());
        return new ResponseEntity<>(List.of(validationError), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BorrowingNotFoundException.class)
    public ResponseEntity<List<ValidationError>> handleBorrowingNotFound(BorrowingNotFoundException exception) {
        ValidationError validationError = new ValidationError("borrowingId",
                "Borrowing not found with id: "+exception.getBorrowingId());
        return new ResponseEntity<>(List.of(validationError), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExemplarNotBorrowableException.class)
    public ResponseEntity<List<ValidationError>> handleExemplarNotBorrowable(ExemplarNotBorrowableException exception) {
        ValidationError validationError = new ValidationError("exemplarId",
                "Exemplar not borrowable with id: "+exception.getExemplarId());
        return new ResponseEntity<>(List.of(validationError), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserHasActiveBorrowingsException.class)
    public ResponseEntity<List<ValidationError>> handleUserHasActiveBorrowings(UserHasActiveBorrowingsException exception) {
        ValidationError validationError = new ValidationError("userId",
                "User with id: "+exception.getUserId() + " has active borrowings. Can't be deleted!");
        return new ResponseEntity<>(List.of(validationError), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExemplarIsInActiveBorrowingException.class)
    public ResponseEntity<List<ValidationError>> handleExemplarIsInActiveBorrowing(ExemplarIsInActiveBorrowingException exception) {
        ValidationError validationError = new ValidationError("exemplarId",
                "Exemplar with id "+exception.getExemplarId()+" is in activ borrowing. Can't be deleted!");
        return new ResponseEntity<>(List.of(validationError), HttpStatus.BAD_REQUEST);
    }
}
