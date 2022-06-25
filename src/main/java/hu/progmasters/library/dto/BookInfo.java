package hu.progmasters.library.dto;

import hu.progmasters.library.domain.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookInfo {
    private Integer id;
    private String ISBN;
    private String title;
    private AuthorInfo author;
    private int numberOfPages;
    private String publisher;
    private int publishingYear;
    private Genre genre;
}
