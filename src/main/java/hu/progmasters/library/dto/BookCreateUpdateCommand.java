package hu.progmasters.library.dto;

import hu.progmasters.library.domain.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCreateUpdateCommand {

    @NotBlank(message = "Must not be blank")
    @Schema(description = "ISBN of the book", example = "18763")
    private String ISBN;

    @NotBlank(message = "Must not be blank")
    @Schema(description = "Title of the book", example = "Unicorns and Their Magic Friends")
    private String title;

    @NotNull
    @Schema(description = "Author(id) of the book", example = "1")
    private Integer authorId;

    @Min(value = 5, message = "Must be at least 5")
    @Schema(description = "The number of pages of the book", example = "67")
    private int numberOfPages;

    @NotBlank(message = "Must not be blank")
    @Schema(description = "The publisher of the book", example = "The Best")
    private String publisher;

    @Min(1700)
    @Max(2022)
    @Schema(description = "The publishing year of the book", example = "2021")
    private int publishingYear;

    @NotNull(message = "Must not be null")
    @Schema(description = "The genre of the book", example = "CHILDREN")
    private Genre genre;
}
