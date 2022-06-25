package hu.progmasters.library.service;

import hu.progmasters.library.domain.*;
import hu.progmasters.library.dto.BookCreateUpdateCommand;
import hu.progmasters.library.dto.BookInfo;
import hu.progmasters.library.dto.ExemplarInfo;
import hu.progmasters.library.exceptionhandling.AuthorHasBooksException;
import hu.progmasters.library.exceptionhandling.BookHasExemplarsException;
import hu.progmasters.library.exceptionhandling.BookNotFoundException;
import hu.progmasters.library.repository.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final ModelMapper modelMapper;

    public BookService(BookRepository bookRepository, AuthorService authorService, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.modelMapper = modelMapper;
    }


    public BookInfo create(BookCreateUpdateCommand command) {
        Book toSave = modelMapper.map(command, Book.class);
        Author author = authorService.findAuthor(command.getAuthorId());
        toSave.setAuthor(author);
        toSave.setDeleted(false);
        Book saved = bookRepository.save(toSave);
        return modelMapper.map(saved, BookInfo.class);
    }

    public List<BookInfo> findAll(Genre genre) {
        List<Book> books = bookRepository.findAll(genre);
        return books.stream()
                .map(book -> modelMapper.map(book, BookInfo.class))
                .collect(Collectors.toList());
    }

    public BookInfo findById(Integer bookId) {
        Book bookFound = findBook(bookId);
        return modelMapper.map(bookFound, BookInfo.class);
    }


    public List<ExemplarInfo> findAllExemplarsOfBook(Integer bookId) {
        Book ofBook = findBook(bookId);
        List<Exemplar> exemplars = ofBook.getExemplars()
                .stream()
                .filter(exemplar -> !exemplar.getDeleted())
                .collect(Collectors.toList());
        return exemplars.stream()
                .map(exemplar -> modelMapper.map(exemplar, ExemplarInfo.class))
                .collect(Collectors.toList());
    }

    public BookInfo update(Integer id, BookCreateUpdateCommand command) {
        Book toUpdate = findBook(id);
        modelMapper.map(command, toUpdate);
        return modelMapper.map(toUpdate, BookInfo.class);
    }

    public Book findBook(Integer bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty() || bookOptional.get().getDeleted()) {
            throw new BookNotFoundException(bookId);
        }
        return bookOptional.get();
    }

    public void delete(Integer id) {
        Book bookToDelete = findBook(id);
        List<Exemplar> exemplars = bookToDelete.getExemplars()
                .stream()
                .filter(exemplar -> !exemplar.getDeleted())
                .collect(Collectors.toList());
        if (exemplars.isEmpty()) {
            bookToDelete.setDeleted(true);
        } else {
            throw new BookHasExemplarsException(id);
        }
    }
}
