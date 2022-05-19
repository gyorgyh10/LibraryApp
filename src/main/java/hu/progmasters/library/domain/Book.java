package hu.progmasters.library.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private Integer id;
    private String ISBN;
    private String title;
    private List<Author> authors=new ArrayList<>();
    private int numberOfPages;
    private String publisher;
    private int publishingYear;
    private List<Exemplar> exemplars=new ArrayList<>();

    public void addExemplar(Exemplar exemplar){
        if (!exemplars.contains(exemplar)) {
            exemplars.add(exemplar);
        }



    }
}
