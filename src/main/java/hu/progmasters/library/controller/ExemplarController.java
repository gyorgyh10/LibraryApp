package hu.progmasters.library.controller;

import hu.progmasters.library.dto.*;
import hu.progmasters.library.service.ExemplarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/library/exemplars")
public class ExemplarController {

    private final ExemplarService exemplarService;

    public ExemplarController(ExemplarService exemplarService) {
        this.exemplarService = exemplarService;
    }

    @PostMapping("/{bookId}")
    @Operation(summary = "Save an exemplar of the book.")
    @ApiResponse(responseCode = "201", description = "Exemplar has been saved")
    public ResponseEntity<ExemplarInfo> create(@Valid @RequestBody ExemplarCreateCommand command,
                                               @PathVariable("bookId") Integer bookId) {
        ExemplarInfo saved = exemplarService.createExemplar(command, bookId);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{exemplarId}")
    @Operation(summary = "Finds an exemplar by id")
    @ApiResponse(responseCode = "200", description = "Exemplar has been found.")
    public ResponseEntity<ExemplarInfoAll> findById(@PathVariable("exemplarId") Integer id) {
        ExemplarInfoAll exemplar = exemplarService.findById(id);
        return new ResponseEntity<>(exemplar, HttpStatus.OK);
    }

//    @GetMapping("/ofbook/{bookId}")
//    @Operation(summary = "List all exemplars of the book.")
//    @ApiResponse(responseCode = "200", description = "Exemplars have been listed.")
//    public ResponseEntity<List<ExemplarInfoNoBook>> findAllExemplarsOfBook(@PathVariable("bookId") Integer bookId) {
//        List<ExemplarInfoNoBook> exemplars = exemplarService.findAllExemplarsOfBook(bookId);
//        return new ResponseEntity<>(exemplars, HttpStatus.OK);
//    }

//    @GetMapping("/ofbook/{bookId}/borrowable")
//    @Operation(summary = "List all borrowable exemplars of the book.")
//    @ApiResponse(responseCode = "200", description = "Borrowable exemplars have been listed.")
//    public ResponseEntity<List<ExemplarInfoNoBook>> findAllBorrowableExemplarsOfBook(@PathVariable("bookId") Integer bookId) {
//        List<ExemplarInfoNoBook> borrowableExemplars = exemplarService.findAllBorrowableExemplarsOfBook(bookId);
//        return new ResponseEntity<>(borrowableExemplars, HttpStatus.OK);
//    }

    @DeleteMapping("/{exemplarId}")
    @Operation(summary = "Delete the exemplar.")
    @ApiResponse(responseCode = "200", description = "Exemplar has been deleted.")
    public ResponseEntity<Void> delete(@PathVariable("exemplarId") Integer exemplarId) {
        exemplarService.delete(exemplarId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
