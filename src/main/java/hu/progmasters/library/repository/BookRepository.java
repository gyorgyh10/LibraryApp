package hu.progmasters.library.repository;

import hu.progmasters.library.domain.Book;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class BookRepository {
    private Map<Integer, Book> books = new HashMap<>();
    private Integer nextId = 1;


    public Book create(Book toSave) {
        toSave.setId(nextId);
        books.put(nextId, toSave);
        nextId++;
        return toSave;
    }

    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }

    public Optional<Book> findById(Integer id) {
        return books.containsKey(id) ? Optional.of(books.get(id)) : Optional.empty();
    }
}
