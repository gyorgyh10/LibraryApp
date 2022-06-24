package hu.progmasters.library.controller;

import hu.progmasters.library.dto.*;
import hu.progmasters.library.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/library/authors")
@Slf4j
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }


    @PostMapping
    @Operation(summary = "Save an author")
    @ApiResponse(responseCode = "201", description = "Author has been saved")
    public ResponseEntity<AuthorInfo> create(@Valid @RequestBody AuthorCreateUpdateCommand command) {
        log.info("Http request, POST /api/library/authors, body: " + command.toString());
        AuthorInfo saved = authorService.create(command);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }


    @GetMapping
    @Operation(summary = "List all authors.")
    @ApiResponse(responseCode = "200", description = "Authors have been listed.")
    public ResponseEntity<List<AuthorInfo>> findAll() {
        log.info("Http request, GET /api/library/authors");
        List<AuthorInfo> authors = authorService.findAll();
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @GetMapping("/{authorId}")
    @Operation(summary = "List all books of the author.")
    @ApiResponse(responseCode = "200", description = "Books of the author have been listed.")
    public ResponseEntity<List<BookInfoNoAuthor>> findAllBooksOfAuthor(@PathVariable("authorId") Integer id) {
        log.info("Http request, GET /api/library/authors/{authorId} with variable: " + id);
        List<BookInfoNoAuthor> books = authorService.findAllBooksOfAuthor(id);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PutMapping("/{authorId}")
    @Operation(summary = "Update an author")
    @ApiResponse(responseCode = "200", description = "Author has been updated")
    public ResponseEntity<AuthorInfo> update(@PathVariable("authorId") Integer id,
                                           @Valid @RequestBody AuthorCreateUpdateCommand command) {
        log.info("Http request, PUT /api/library/authors/{authorId} body: " + command.toString() +
                " with variable: " + id);
        AuthorInfo updated = authorService.update(id, command);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

}
