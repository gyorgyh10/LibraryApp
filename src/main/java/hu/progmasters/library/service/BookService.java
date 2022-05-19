package hu.progmasters.library.service;

import hu.progmasters.library.domain.Author;
import hu.progmasters.library.domain.Book;
import hu.progmasters.library.domain.Exemplar;
import hu.progmasters.library.dto.*;
import hu.progmasters.library.exceptionhandling.AuthorNotFoundException;
import hu.progmasters.library.exceptionhandling.BookAndExemplarNotMatchException;
import hu.progmasters.library.exceptionhandling.BookNotFoundException;
import hu.progmasters.library.exceptionhandling.ExemplarNotFoundException;
import hu.progmasters.library.repository.AuthorRepository;
import hu.progmasters.library.repository.BookRepository;
import hu.progmasters.library.repository.ExemplarRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private ExemplarRepository exemplarRepository;
    private ModelMapper modelMapper;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository,
                       ExemplarRepository exemplarRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.exemplarRepository = exemplarRepository;
        this.modelMapper = modelMapper;
    }


    public BookInfo createBook(BookCreateCommand command) {
        Book bookToSave = modelMapper.map(command, Book.class);
        Book bookSaved = bookRepository.create(bookToSave);
        List<Author> authorsToSave = bookSaved.getAuthors();
        for (int i = 0; i < authorsToSave.size(); i++) {
            Author authorToSave = authorsToSave.get(i);
            authorToSave.addBook(bookSaved);
            Author authorSaved = authorRepository.create(authorToSave);
            authorsToSave.set(i, authorSaved);
        }
        return modelMapper.map(bookSaved, BookInfo.class);
    }

    public List<BookInfo> findAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(book -> modelMapper.map(book, BookInfo.class))
                .collect(Collectors.toList());
    }

    public ExemplarInfoNoBook createExemplar(ExemplarCreateCommand command, Integer bookId) {
        Exemplar exemplarToSave = modelMapper.map(command, Exemplar.class);
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            throw new BookNotFoundException(bookId);
        }
        Book ofBook = bookOptional.get();
        exemplarToSave.setOfBook(ofBook);
        Exemplar exemplarSaved = exemplarRepository.create(exemplarToSave);
        ofBook.addExemplar(exemplarSaved);
        return modelMapper.map(exemplarSaved, ExemplarInfoNoBook.class);
    }

    public List<ExemplarInfoNoBook> findAllExemplarsOfBook(Integer bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            throw new BookNotFoundException(bookId);
        }
        Book ofBook = bookOptional.get();
        List<Exemplar> exemplars = exemplarRepository.findAllExemplarsOfBook(bookId);
        return exemplars.stream()
                .map(exemplar -> modelMapper.map(exemplar, ExemplarInfoNoBook.class))
                .collect(Collectors.toList());
    }

    public void deleteExemplar(Integer bookId, Integer exemplarId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            throw new BookNotFoundException(bookId);
        }
        Book bookToDeleteFrom = bookOptional.get();
        Optional<Exemplar> exemplarOptional = exemplarRepository.findById(exemplarId);
        if (exemplarOptional.isEmpty()) {
            throw new ExemplarNotFoundException(exemplarId);
        }
        Exemplar exemplarToDelete = exemplarOptional.get();
        if (!exemplarToDelete.getOfBook().equals(bookToDeleteFrom)) {
            throw new BookAndExemplarNotMatchException(bookId + " " + exemplarId);
        }
        bookToDeleteFrom.getExemplars().remove(exemplarToDelete);
        exemplarRepository.delete(exemplarId);
    }

    public List<AuthorInfoNoBooks> findAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream()
                .map(author -> modelMapper.map(author, AuthorInfoNoBooks.class))
                .collect(Collectors.toList());
    }

    public List<BookInfoNoAuthor> findAllBooksOfAuthor(Integer authorId) {
        Optional<Author> authorOptional = authorRepository.findById(authorId);
        if (authorOptional.isEmpty()) {
            throw new AuthorNotFoundException(authorId);
        }
        List<Book> books = authorOptional.get().getBooks();
        return books.stream()
                .map(book -> modelMapper.map(book, BookInfoNoAuthor.class))
                .collect(Collectors.toList());
    }
}
