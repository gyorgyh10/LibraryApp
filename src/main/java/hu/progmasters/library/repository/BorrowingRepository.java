package hu.progmasters.library.repository;

import hu.progmasters.library.domain.Borrowing;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class BorrowingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Borrowing create(Borrowing toSave) {
        entityManager.persist(toSave);
        return toSave;
    }

    public Optional<Borrowing> findById(Integer borrowingId) {
        return Optional.ofNullable(entityManager.find(Borrowing.class, borrowingId));
    }

    public List<Borrowing> findAll(Integer exemplarId, Integer userId) {
        return entityManager.createQuery(
                        "SELECT b FROM Borrowing b " +
                                "WHERE (:exemplarParam IS NULL OR b.exemplar.id= :exemplarParam) AND " +
                                "(:userParam IS NULL OR b.user.id= :userParam)", Borrowing.class)
                .setParameter("exemplarParam", exemplarId)
                .setParameter("userParam", userId)
                .getResultList();
    }

    public void delete(Borrowing toDelete) {
        entityManager.remove(toDelete);
    }

    public List<Borrowing> activeBorrowingsOfUser(Integer userId) {
        return entityManager.createQuery("SELECT b FROM Borrowing B " +
                        "WHERE (b.user.id= :idParam) AND b.active", Borrowing.class)
                .setParameter("idParam", userId)
                .getResultList();
    }

}
