package hu.progmasters.library.repository;

import hu.progmasters.library.domain.Author;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

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
        return entityManager.createQuery("SELECT a FROM Author a " +
                "WHERE a.deleted = false", Author.class).getResultList();
    }
}
