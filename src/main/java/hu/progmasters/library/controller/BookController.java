package hu.progmasters.library.controller;

import hu.progmasters.library.domain.Genre;
import hu.progmasters.library.dto.BookCreateUpdateCommand;
import hu.progmasters.library.dto.BookInfo;
import hu.progmasters.library.dto.ExemplarInfo;
import hu.progmasters.library.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/library/books")
@Slf4j
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @Operation(summary = "Save a book")
    @ApiResponse(responseCode = "201", description = "Book has been saved")
    public ResponseEntity<BookInfo> create(@Valid @RequestBody BookCreateUpdateCommand command) {
        log.info("Http request, POST /api/library/books, body: " + command.toString());
        BookInfo saved = bookService.create(command);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }


    @GetMapping
    @Operation(summary = "List all books OR all books from a genre")
    @ApiResponse(responseCode = "200", description = "Books have been listed.")
    public ResponseEntity<List<BookInfo>> findAll(@RequestParam(value = "genre", required = false) Genre genre) {
        log.info("Http request, GET /api/library/books with parameter genre: " + genre);
        List<BookInfo> books = bookService.findAll(genre);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{bookId}")
    @Operation(summary = "Finds a book by id")
    @ApiResponse(responseCode = "200", description = "Book has been found")
    public ResponseEntity<BookInfo> findById(@PathVariable("bookId") Integer id) {
        log.info("Http request, GET /api/library/books/{bookId} with variable: " + id);
        BookInfo book = bookService.findById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @GetMapping("/{bookId}/exemplars")
    @Operation(summary = "List all EXEMPLARS of a book")
    @ApiResponse(responseCode = "200", description = "Exemplars have been listed")
    public ResponseEntity<List<ExemplarInfo>> findAllExemplars(@PathVariable("bookId") Integer id) {
        log.info("Http request, GET /api/library/books/{bookId}/exemplars with variable: " + id);
        List<ExemplarInfo> exemplars = bookService.findAllExemplarsOfBook(id);
        return new ResponseEntity<>(exemplars, HttpStatus.OK);
    }

    @PutMapping("/{bookId}")
    @Operation(summary = "Update a book")
    @ApiResponse(responseCode = "200", description = "Book has been updated")
    public ResponseEntity<BookInfo> update(@PathVariable("bookId") Integer id,
                                           @Valid @RequestBody BookCreateUpdateCommand command) {
        log.info("Http request, PUT /api/library/books/{bookId} body: " + command.toString() +
                " with variable: " + id);
        BookInfo updated = bookService.update(id, command);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{bookId}")
    @Operation(summary = "Delete a book")
    @ApiResponse(responseCode = "200", description = "Book has been deleted")
    public ResponseEntity<Void> delete(@PathVariable("bookId") Integer id) {
        log.info("Http request, DELETE /api/library/books/{bookId} with variable: " + id);
        bookService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
