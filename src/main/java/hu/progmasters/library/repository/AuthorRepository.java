package hu.progmasters.library.repository;

import hu.progmasters.library.domain.Author;
import hu.progmasters.library.domain.Book;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AuthorRepository {

    private Map<Integer, Author> authors = new HashMap<>();
    private Integer nextId = 1;


    public Author create(Author author) {
        author.setId(nextId);
        authors.put(nextId, author);
        nextId++;
        return author;
    }

    public List<Author> findAll() {
        return new ArrayList<>(authors.values());
    }

    public Optional<Author> findById(Integer id) {
        return authors.containsKey(id) ? Optional.of(authors.get(id)) : Optional.empty();
    }

}
