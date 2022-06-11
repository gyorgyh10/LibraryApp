package hu.progmasters.library.dto;

import hu.progmasters.library.domain.Author;
import hu.progmasters.library.domain.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCreateCommand {

    @NotBlank(message = "Most not be blank")
    @Schema(description = "ISBN of the book", example="18763")
    private String ISBN;

    @NotBlank(message = "Most not be blank")
    @Schema(description = "Title of the book", example="Unicorns and Their Magic Friends")
    private String title;

    @NotNull
    @Schema(description = "Author(id) of the book", example="1")
    private Integer authorId;

    @Min(5)
    @Schema(description = "The number of pages of the book", example="67")
    private int numberOfPages;

    @NotBlank(message = "Most not be blank")
    @Schema(description = "The publisher of the book", example="The Best")
    private String publisher;

    @Min(1500)
    @Max(2022)
    @Schema(description = "The publishing year of the book", example="2021")
    private int publishingYear;

    @Schema(description = "The genre of the book", example="CHILDREN")
    private Genre genre;
}
