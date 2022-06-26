package hu.progmasters.library.controller;

import hu.progmasters.library.dto.ExemplarCreateUpdateCommand;
import hu.progmasters.library.dto.ExemplarInfo;
import hu.progmasters.library.dto.ExemplarInfoAll;
import hu.progmasters.library.service.ExemplarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/library/exemplars")
@Slf4j
public class ExemplarController {

    private final ExemplarService exemplarService;

    public ExemplarController(ExemplarService exemplarService) {
        this.exemplarService = exemplarService;
    }

    @PostMapping("/{bookId}")
    @Operation(summary = "Save an exemplar of the book")
    @ApiResponse(responseCode = "201", description = "Exemplar has been saved")
    public ResponseEntity<ExemplarInfo> create(@Valid @RequestBody ExemplarCreateUpdateCommand command,
                                               @PathVariable("bookId") Integer bookId) {
        log.info("Http request, POST /api/library/exemplars/{bookId}, body: " + command.toString() +
                " and variable: " + bookId);
        ExemplarInfo saved = exemplarService.create(command, bookId);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "List all exemplars")
    @ApiResponse(responseCode = "200", description = "Exemplars have been listed")
    public ResponseEntity<List<ExemplarInfo>> findAll() {
        log.info("Http request, GET /api/library/exemplars");
        List<ExemplarInfo> exemplars = exemplarService.findAll();
        return new ResponseEntity<>(exemplars, HttpStatus.OK);
    }

    @GetMapping("/{exemplarId}")
    @Operation(summary = "Find an exemplar by id")
    @ApiResponse(responseCode = "200", description = "Exemplar has been found.")
    public ResponseEntity<ExemplarInfoAll> findById(@PathVariable("exemplarId") Integer id) {
        log.info("Http request, GET /api/library/exemplars/{exemplarId} with variable: " + id);
        ExemplarInfoAll exemplar = exemplarService.findById(id);
        return new ResponseEntity<>(exemplar, HttpStatus.OK);
    }


    @DeleteMapping("/{exemplarId}")
    @Operation(summary = "Delete the exemplar.")
    @ApiResponse(responseCode = "200", description = "Exemplar has been deleted")
    public ResponseEntity<Void> delete(@PathVariable("exemplarId") Integer exemplarId) {
        log.info("Http request, DELETE /api/library/exemplars/{exemplarId} with variable: " + exemplarId);
        exemplarService.delete(exemplarId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{exemplarId}")
    @Operation(summary = "Update a exemplar")
    @ApiResponse(responseCode = "200", description = "Exemplar has been updated")
    public ResponseEntity<ExemplarInfo> update(@PathVariable("exemplarId") Integer id,
                                               @Valid @RequestBody ExemplarCreateUpdateCommand command) {
        log.info("Http request, PUT /api/library/exemplars/{exemplarId} body: " + command.toString() +
                " with variable: " + id);
        ExemplarInfo updated = exemplarService.update(id, command);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

}
