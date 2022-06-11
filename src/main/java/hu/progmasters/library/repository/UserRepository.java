package hu.progmasters.library.repository;

import hu.progmasters.library.domain.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public User create(User toSave) {
        entityManager.persist(toSave);
        return toSave;
    }

    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u " +
                "WHERE u.deleted = false", User.class).getResultList();
    }


    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }
}
