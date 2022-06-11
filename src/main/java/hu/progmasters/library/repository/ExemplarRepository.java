package hu.progmasters.library.repository;

import hu.progmasters.library.domain.Exemplar;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ExemplarRepository {

    @PersistenceContext
    private EntityManager entityManager;


    public Exemplar create(Exemplar toSave) {
        entityManager.persist(toSave);
        return toSave;
    }

    public List<Exemplar> findAllExemplarOfBook(Integer bookId) {
        return entityManager.createQuery("SELECT e FROM Exemplar e WHERE e.ofBook.id= :bookIdParam", Exemplar.class)
                .setParameter("bookIdParam", bookId)
                .getResultList();
    }

    public List<Exemplar> findAllBorrowableExemplarOfBook(Integer bookId) {
        return entityManager.createQuery("SELECT e FROM Exemplar e " +
                                "WHERE (e.ofBook.id= :bookIdParam) AND e.borrowable=true", Exemplar.class)
                .setParameter("bookIdParam", bookId)
                .getResultList();
    }

    public Optional<Exemplar> findById(Integer id) {
        return Optional.ofNullable(entityManager.find(Exemplar.class, id));
    }

}
