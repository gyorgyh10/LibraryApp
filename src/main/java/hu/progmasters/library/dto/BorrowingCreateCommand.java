package hu.progmasters.library.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class BorrowingCreateCommand {

    @FutureOrPresent
    @Schema(description = "The starting date of borrowing")
    private LocalDate fromDate;

    @Future
    @Schema(description = "The ending date of borrowing")
    private LocalDate toDate;

}
