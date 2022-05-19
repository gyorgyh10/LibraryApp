package hu.progmasters.library.controller;

import hu.progmasters.library.dto.BookCreateCommand;
import hu.progmasters.library.dto.BookInfo;
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

    private BookService bookService;

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
    @Operation(summary = "List all books")
    @ApiResponse(responseCode = "200", description = "Books have been listed.")
    public ResponseEntity<List<BookInfo>> findAll(){
        List<BookInfo> books=bookService.findAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
}
