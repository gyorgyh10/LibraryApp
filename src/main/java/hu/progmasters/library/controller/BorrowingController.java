package hu.progmasters.library.controller;

import hu.progmasters.library.dto.BorrowingInfo;
import hu.progmasters.library.service.BorrowingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/library/borrowings")
@Slf4j
public class BorrowingController {

    private final BorrowingService borrowingService;

    public BorrowingController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }


    @PostMapping("/{exemplarId}/{userId}")
    @Operation(summary = "Save a borrowing")
    @ApiResponse(responseCode = "201", description = "Borrowing has been saved")
    public ResponseEntity<BorrowingInfo> create(@PathVariable("exemplarId") Integer exemplarId,
                                                @PathVariable("userId") Integer userId) {
        log.info("Http request, POST /api/library/borrowings/{exemplarId}/{userId}, variables: " +
                exemplarId + ", " + userId);
        BorrowingInfo saved = borrowingService.create(exemplarId, userId);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping()
    @Operation(summary = "List all borrowings OR all for an exemplar or for a user")
    @ApiResponse(responseCode = "200", description = "Borrowings have been listed")
    public ResponseEntity<List<BorrowingInfo>> findAll(
            @RequestParam(value = "exemplarId", required = false) Integer exemplarId,
            @RequestParam(value = "userId", required = false) Integer userId) {
        log.info("Http request, GET /api/library/borrowings with parameters exemplarId: " + exemplarId +
                " userId: " + userId);
        List<BorrowingInfo> borrowings = borrowingService.findAll(exemplarId, userId);
        return new ResponseEntity<>(borrowings, HttpStatus.OK);
    }

    @GetMapping("/{borrowingId}")
    @Operation(summary = "Find a borrowing by id")
    @ApiResponse(responseCode = "200", description = "Borrowing has been found")
    public ResponseEntity<BorrowingInfo> findById(@PathVariable("borrowingId") Integer id) {
        log.info("Http request, GET /api/library/borrowings/{borrowingId} with variable: " + id);
        BorrowingInfo borrowing = borrowingService.findById(id);
        return new ResponseEntity<>(borrowing, HttpStatus.OK);
    }

    @DeleteMapping("/{borrowingId}")
    @Operation(summary = "Delete a borrowing")
    @ApiResponse(responseCode = "200", description = "Borrowing has been deleted")
    public ResponseEntity<Void> delete(@PathVariable("borrowingId") Integer id) {
        log.info("Http request, DELETE /api/library/borrowings{/borrowingId} with variable: " + id);
        borrowingService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/extend/{borrowingId}")
    @Operation(summary = "Extend a borrowing")
    @ApiResponse(responseCode = "200", description = "Borrowing has been extended")
    public ResponseEntity<BorrowingInfo> prolongation(@PathVariable("borrowingId") Integer id) {
        log.info("Http request, PUT /api/library/borrowings/extend/{borrowingId} with variable: " + id);
        BorrowingInfo extended = borrowingService.prolongation(id);
        return new ResponseEntity<>(extended, HttpStatus.OK);
    }

    @PutMapping("/bring_back/{borrowingId}")
    @Operation(summary = "Inactivate a borrowing. (Book is back.)")
    @ApiResponse(responseCode = "200", description = "Borrowing has been inactivated")
    public ResponseEntity<BorrowingInfo> inactivation(@PathVariable("borrowingId") Integer id) {
        log.info("Http request, PUT /api/library/borrowings/bring_back/{borrowingId} with variable " + id);
        BorrowingInfo inactivated = borrowingService.inactivation(id);
        return new ResponseEntity<>(inactivated, HttpStatus.OK);
    }
}
