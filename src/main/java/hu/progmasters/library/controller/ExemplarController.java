package hu.progmasters.library.controller;

import hu.progmasters.library.dto.ExemplarCreateCommand;
import hu.progmasters.library.dto.ExemplarInfoNoBook;
import hu.progmasters.library.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/library/books")
public class ExemplarController {

    private BookService bookService;

    public ExemplarController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/{bookId}")
    @Operation(summary = "Save an exemplar of the book.")
    @ApiResponse(responseCode = "201", description = "Exemplar has been saved")
    public ResponseEntity<ExemplarInfoNoBook> create(@Valid @RequestBody ExemplarCreateCommand command,
                                                     @PathVariable("bookId") Integer bookId) {
        ExemplarInfoNoBook saved = bookService.createExemplar(command, bookId);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }


    @GetMapping("{bookId}/exemplars")
    @Operation(summary = "List all exemplars of the book.")
    @ApiResponse(responseCode = "200", description = "Exemplars have been listed.")
    public ResponseEntity<List<ExemplarInfoNoBook>> findAllExemplarsOfBook(@PathVariable("bookId") Integer bookId) {
        List<ExemplarInfoNoBook> exemplars = bookService.findAllExemplarsOfBook(bookId);
        return new ResponseEntity<>(exemplars, HttpStatus.OK);
    }

    @DeleteMapping("{bookId}/exemplars/{exemplarId}")
    @Operation(summary = "Delete the exemplar of the book.")
    @ApiResponse(responseCode = "200", description = "Exemplar has been deleted.")
    public ResponseEntity<Void> delete(@PathVariable("bookId") Integer bookId,
                                       @PathVariable("exemplarId") Integer exemplarId) {
        bookService.deleteExemplar(bookId, exemplarId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
