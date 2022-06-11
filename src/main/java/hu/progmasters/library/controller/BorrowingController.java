package hu.progmasters.library.controller;

import hu.progmasters.library.dto.BorrowingCreateCommand;
import hu.progmasters.library.dto.BorrowingInfo;
import hu.progmasters.library.service.BorrowingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
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
    public ResponseEntity<BorrowingInfo> create(@Valid @RequestBody BorrowingCreateCommand command,
                                                @PathVariable("exemplarId") Integer exemplarId,
                                                @PathVariable("userId") Integer userId) {
        BorrowingInfo saved = borrowingService.create(command, exemplarId, userId);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping()
    @Operation(summary = "List all borrowings for an exemplar or for a user")
    @ApiResponse(responseCode = "200", description = "Borrowings have been listed.")
    public ResponseEntity<List<BorrowingInfo>> findAll(
            @RequestParam(value="exemplarId", required = false) Integer exemplarId,
            @RequestParam(value="userId", required = false) Integer userId) {
        List<BorrowingInfo> borrowings = borrowingService.findAll(exemplarId, userId);
        return new ResponseEntity<>(borrowings, HttpStatus.OK);
    }

    @GetMapping("/{borrowingId}")
    @Operation(summary = "Finds a borrowing by id")
    @ApiResponse(responseCode = "200", description = "Borrowing has been found.")
    public ResponseEntity<BorrowingInfo> findById(@PathVariable("borrowingId") Integer id) {
        BorrowingInfo borrowing = borrowingService.findById(id);
        return new ResponseEntity<>(borrowing, HttpStatus.OK);
    }

    @DeleteMapping("/{/borrowingId}")
    @Operation(summary = "Deletes a borrowing")
    @ApiResponse(responseCode = "200", description = "Borrowing has been deleted.")
    public ResponseEntity<Void> delete(@PathVariable("borrowingId")Integer id) {
        borrowingService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
//    @PutMapping("/{borrowingId}")
//    @Operation(summary = "Update a borrowing")
//    @ApiResponse(responseCode = "200", description = "Borrowing has been updated")
//    public ResponseEntity<BorrowingInfo> update(@PathVariable("borrowingId") Integer id,
//                                           @Valid @RequestBody BorrowingCreateCommand command) {
//        BorrowingInfo updated = borrowingService.update(id, command);
//        return new ResponseEntity<>(updated, HttpStatus.CREATED);
//    }
}
