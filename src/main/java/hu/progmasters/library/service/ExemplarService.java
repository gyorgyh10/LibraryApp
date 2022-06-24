package hu.progmasters.library.service;


import hu.progmasters.library.domain.Book;
import hu.progmasters.library.domain.Exemplar;
import hu.progmasters.library.dto.ExemplarCreateUpdateCommand;
import hu.progmasters.library.dto.ExemplarInfo;
import hu.progmasters.library.dto.ExemplarInfoAll;
import hu.progmasters.library.dto.ExemplarInfoNoBook;
import hu.progmasters.library.exceptionhandling.ExemplarIsInActiveBorrowingException;
import hu.progmasters.library.exceptionhandling.ExemplarNotFoundException;
import hu.progmasters.library.repository.ExemplarRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExemplarService {

    private final ExemplarRepository exemplarRepository;
    private final BookService bookService;
    private final ModelMapper modelMapper;

    public ExemplarService(ExemplarRepository exemplarRepository, BookService bookService, ModelMapper modelMapper) {
        this.exemplarRepository = exemplarRepository;
        this.bookService = bookService;
        this.modelMapper = modelMapper;
    }

    public ExemplarInfo create(ExemplarCreateUpdateCommand command, Integer bookId) {
        Exemplar toSave = modelMapper.map(command, Exemplar.class);
        Book book = bookService.findBook(bookId);
        toSave.setOfBook(book);
        toSave.setDeleted(false);
        Exemplar saved = exemplarRepository.create(toSave);
        return modelMapper.map(saved, ExemplarInfo.class);
    }

    public List<ExemplarInfo> findAll() {
        List<Exemplar> exemplars = exemplarRepository.findAll();
        return exemplars.stream()
                .map(exemplar -> modelMapper.map(exemplar, ExemplarInfo.class))
                .collect(Collectors.toList());
    }

    public ExemplarInfoAll findById(Integer id) {
        Exemplar exemplarFound = findExemplar(id);
        return modelMapper.map(exemplarFound, ExemplarInfoAll.class);
    }

    public List<ExemplarInfoNoBook> findAllBorrowableExemplarsOfBook(Integer bookId) {
        List<Exemplar> borrowableExemplars = exemplarRepository.findAllBorrowableExemplarOfBook(bookId);
        return borrowableExemplars.stream()
                .map(exemplar -> modelMapper.map(exemplar, ExemplarInfoNoBook.class))
                .collect(Collectors.toList());
    }

    public void delete(Integer exemplarId) {
        Exemplar toDelete = findExemplar(exemplarId);
        if (!toDelete.getBorrowable()) {
            throw new ExemplarIsInActiveBorrowingException(exemplarId);
        }
        toDelete.setDeleted(true);
    }

    public ExemplarInfo update(Integer id, ExemplarCreateUpdateCommand command) {
        Exemplar toUpdate = findExemplar(id);
        modelMapper.map(command, toUpdate);
        return modelMapper.map(toUpdate, ExemplarInfo.class);
    }

    public Exemplar findExemplar(Integer id) {
        Optional<Exemplar> exemplarOptional = exemplarRepository.findById(id);
        if (exemplarOptional.isEmpty() || exemplarOptional.get().getDeleted()) {
            throw new ExemplarNotFoundException(id);
        }
        return exemplarOptional.get();
    }

}
