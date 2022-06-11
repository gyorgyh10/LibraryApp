package hu.progmasters.library.repository;

import hu.progmasters.library.domain.Author;
import hu.progmasters.library.domain.Book;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Repository
public class AuthorRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Author> findById(Integer authorId) {
        return Optional.ofNullable(entityManager.find(Author.class, authorId));
    }

    public Author create(Author toSave) {
        entityManager.persist(toSave);
        return toSave;
    }

    public List<Author> findAll() {
        return entityManager.createQuery("SELECT a FROM Author a", Author.class).getResultList();
    }
}
