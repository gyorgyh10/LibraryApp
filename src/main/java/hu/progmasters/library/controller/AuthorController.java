package hu.progmasters.library.controller;

import hu.progmasters.library.dto.AuthorInfoNoBooks;
import hu.progmasters.library.dto.BookInfoNoAuthor;
import hu.progmasters.library.dto.ExemplarInfoNoBook;
import hu.progmasters.library.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/library/authors")
public class AuthorController {

    private BookService bookService;

    public AuthorController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping
    @Operation(summary = "List all authors without their books.")
    @ApiResponse(responseCode = "200", description = "Authors have been listed.")
    public ResponseEntity<List<AuthorInfoNoBooks>> findAll() {
        List<AuthorInfoNoBooks> authors = bookService.findAllAuthors();
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @GetMapping("{authorId}")
    @Operation(summary = "List all books of the author.")
    @ApiResponse(responseCode = "200", description = "Books of the author have been listed.")
    public ResponseEntity<List<BookInfoNoAuthor>> findAllBooksOfAuthor(@PathVariable("authorId") Integer authorId) {
        List<BookInfoNoAuthor> books = bookService.findAllBooksOfAuthor(authorId);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }


}
