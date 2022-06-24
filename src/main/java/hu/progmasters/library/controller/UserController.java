package hu.progmasters.library.controller;

import hu.progmasters.library.dto.BorrowingInfoNoUser;
import hu.progmasters.library.dto.UserCreateCommand;
import hu.progmasters.library.dto.UserInfo;
import hu.progmasters.library.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/library/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Save a user")
    @ApiResponse(responseCode = "201", description = "User has been saved")
    public ResponseEntity<UserInfo> create(@Valid @RequestBody UserCreateCommand command) {
        log.info("Http request, POST /api/library/users, body: " + command.toString());
        UserInfo saved = userService.create(command);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "List all users")
    @ApiResponse(responseCode = "200", description = "Users have been listed.")
    public ResponseEntity<List<UserInfo>> findAll() {
        log.info("Http request, GET /api/library/users");
        List<UserInfo> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Finds a user by id")
    @ApiResponse(responseCode = "200", description = "User has been found.")
    public ResponseEntity<UserInfo> findById(@PathVariable("userId") Integer id) {
        log.info("Http request, GET /api/library/users/{userId} with variable: " + id);
        UserInfo user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{userId}/borrowings")
    @Operation(summary = "List all borrowings of a user")
    @ApiResponse(responseCode = "200", description = "Borrowings have been listed.")
    public ResponseEntity<List<BorrowingInfoNoUser>> findAllBorrowings(@PathVariable("userId") Integer id) {
        log.info("Http request, GET /api/library/users/{userId}/borrowings with variable: " + id);
        List<BorrowingInfoNoUser> borrowings = userService.findAllBorrowings(id);
        return new ResponseEntity<>(borrowings, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update a user")
    @ApiResponse(responseCode = "200", description = "User has been updated")
    public ResponseEntity<UserInfo> update(@PathVariable("userId") Integer id,
                                           @Valid @RequestBody UserCreateCommand command) {
        log.info("Http request, PUT /api/library/users/{userId} body: " + command.toString() +
                " with variable: " + id);
        UserInfo updated = userService.update(id, command);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Deletes a user")
    @ApiResponse(responseCode = "200", description = "User has been deleted.")
    public ResponseEntity<Void> delete(@PathVariable("userId") Integer id) {
        log.info("Http request, DELETE /api/library/users/{userId} with variable: " + id);
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
