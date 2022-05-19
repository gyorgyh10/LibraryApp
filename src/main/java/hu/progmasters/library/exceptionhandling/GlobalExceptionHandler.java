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
    public ResponseEntity<List<ValidationError>> handleBookAndExemplarNotMatchException
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

}
