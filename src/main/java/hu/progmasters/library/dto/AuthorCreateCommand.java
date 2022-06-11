package hu.progmasters.library.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorCreateCommand {
    @NotBlank(message = "Most not be blank")
    @Schema(description = "Author of the book", example="Emma Writer")
    private String name;
}
