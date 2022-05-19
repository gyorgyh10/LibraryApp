package hu.progmasters.library.repository;

import hu.progmasters.library.domain.Exemplar;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ExemplarRepository {

    private Map<Integer, Exemplar> exemplars=new HashMap<>();
    private Integer nextId=1;


    public Exemplar create(Exemplar exemplarToSave) {
        exemplarToSave.setId(nextId);
        exemplars.put(nextId, exemplarToSave);
        nextId++;
        return exemplarToSave;
    }

    public List<Exemplar> findAll() {
        return new ArrayList<>(exemplars.values());
    }

    public List<Exemplar> findAllExemplarsOfBook(Integer bookId) {
        return exemplars.values().stream()
                .filter(exemplar -> exemplar.getOfBook().getId().equals(bookId))
                .collect(Collectors.toList());
    }

    public Optional<Exemplar> findById(Integer id) {
        return exemplars.containsKey(id) ? Optional.of(exemplars.get(id)) : Optional.empty();
    }

    public void delete(Integer id) {
        exemplars.remove(id);
    }
}
