package hu.progmasters.library.service;

import hu.progmasters.library.domain.Author;
import hu.progmasters.library.domain.Book;
import hu.progmasters.library.domain.Exemplar;
import hu.progmasters.library.domain.Genre;
import hu.progmasters.library.dto.BookCreateCommand;
import hu.progmasters.library.dto.BookInfo;
import hu.progmasters.library.dto.ExemplarInfo;
import hu.progmasters.library.exceptionhandling.AuthorNotFoundException;
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


    public BookInfo createBook(BookCreateCommand command) {
        Book toSave = modelMapper.map(command, Book.class);
        Optional<Author> authorOptional = authorService.getAuthorRepository().findById(command.getAuthorId());
        if (authorOptional.isEmpty()) {
            throw new AuthorNotFoundException(command.getAuthorId());
        }
        toSave.setAuthor(authorOptional.get());
        toSave.setDeleted(false);
        Book saved = bookRepository.save(toSave);
        return modelMapper.map(saved, BookInfo.class);
    }

    public List<BookInfo> findAllBooks(Genre genre) {
        List<Book> books = bookRepository.findAll(genre);
        return books.stream()
                .map(book -> modelMapper.map(book, BookInfo.class))
                .collect(Collectors.toList());
    }

    public BookInfo findById(Integer bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            throw new BookNotFoundException(bookId);
        }
        return modelMapper.map(bookOptional.get(), BookInfo.class);
    }


    public List<ExemplarInfo> findAllExemplars(Integer bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            throw new BookNotFoundException(bookId);
        }
        List<Exemplar> exemplars = bookOptional.get().getExemplars();
        return exemplars.stream()
                .map(exemplar -> modelMapper.map(exemplar, ExemplarInfo.class))
                .collect(Collectors.toList());
    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }
}
