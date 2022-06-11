package hu.progmasters.library.controller;

import hu.progmasters.library.domain.Genre;
import hu.progmasters.library.dto.BookCreateCommand;
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
@RequestMapping("api/library/books")
@Slf4j
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @Operation(summary = "Save a book")
    @ApiResponse(responseCode = "201", description = "Book has been saved")
    public ResponseEntity<BookInfo> create(@Valid @RequestBody BookCreateCommand command) {
        BookInfo saved = bookService.createBook(command);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }


    @GetMapping
    @Operation(summary = "List all books OR all books from a genre")
    @ApiResponse(responseCode = "200", description = "Books have been listed.")
    public ResponseEntity<List<BookInfo>> findAll(@RequestParam(value="genre", required = false) Genre genre) {
        List<BookInfo> books = bookService.findAllBooks(genre);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{bookId}")
    @Operation(summary = "Finds a book by id")
    @ApiResponse(responseCode = "200", description = "Book has been found.")
    public ResponseEntity<BookInfo> findById(@PathVariable("bookId") Integer id) {
        BookInfo book = bookService.findById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @GetMapping("/{bookId}/exemplars")
    @Operation(summary = "List ALL EXEMPLARS of a book")
    @ApiResponse(responseCode = "200", description = "Exemplars have been listed.")
    public ResponseEntity<List<ExemplarInfo>> findAllExemplars(@PathVariable("bookId") Integer id) {
        List<ExemplarInfo> exemplars = bookService.findAllExemplars(id);
        return new ResponseEntity<>(exemplars, HttpStatus.OK);
    }
}
