package hu.progmasters.library.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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

    @NotEmpty
    @Schema(description = "Author of the book", example=" [\"Emma Writer\"]")
    private List<AuthorCreateCommand> authors=new ArrayList<>();

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

}
