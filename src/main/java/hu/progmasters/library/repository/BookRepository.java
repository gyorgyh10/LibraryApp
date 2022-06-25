package hu.progmasters.library.repository;

import hu.progmasters.library.domain.Book;
import hu.progmasters.library.domain.Genre;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Repository
public class BookRepository {

    @PersistenceContext
    private EntityManager entityManager;


    public Book save(Book toSave) {
        entityManager.persist(toSave);
        return toSave;
    }

    public List<Book> findAll(Genre genre) {
        return entityManager.createQuery(
                "SELECT b FROM Book b " +
                        "WHERE (:genreParam IS NULL OR b.genre= :genreParam) " +
                        "AND b.deleted = false", Book.class)
                .setParameter("genreParam", genre)
                .getResultList();
    }

    public Optional<Book> findById(Integer id) {
        return Optional.ofNullable(entityManager.find(Book.class, id));
    }
}
