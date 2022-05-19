package hu.progmasters.library.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Author {
    private Integer id;
    private String name;
    private List<Book> books = new ArrayList<>();

    public void addBook(Book book){
        if (!books.contains(book)) {
            books.add(book);
        }
    }
}
