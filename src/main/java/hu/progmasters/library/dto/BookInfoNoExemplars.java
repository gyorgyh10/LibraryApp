package hu.progmasters.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookInfoNoExemplars {
    private Integer id;
    private String ISBN;
    private String title;
    private List<AuthorInfoNoBooks> authors=new ArrayList<>();
    private int numberOfPages;
    private String publisher;
    private int publishingYear;
}
