package hu.progmasters.library.dto;

import hu.progmasters.library.domain.Condition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExemplarCreateUpdateCommand {

    @NotNull(message = "Most not be Null")
    @Schema(description = "The inventory number of the exemplar", example = "3241")
    private Integer inventoryNumber;

    @NotNull(message = "Most not be Null")
    @Schema(description = "The condition the book is in", example = "NEW")
    private Condition condition;

    @NotNull(message = "Most not be Null")
    @Schema(description = "Is this exemplar borrowable?", example = "true")
    private Boolean borrowable;
}
