package hu.progmasters.library.service;

import hu.progmasters.library.domain.Author;
import hu.progmasters.library.domain.Book;
import hu.progmasters.library.dto.AuthorCreateUpdateCommand;
import hu.progmasters.library.dto.AuthorInfo;
import hu.progmasters.library.dto.BookInfoNoAuthor;
import hu.progmasters.library.exceptionhandling.AuthorNotFoundException;
import hu.progmasters.library.repository.AuthorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;

    public AuthorService(AuthorRepository authorRepository, ModelMapper modelMapper) {
        this.authorRepository = authorRepository;
        this.modelMapper = modelMapper;
    }

    public AuthorInfo create(AuthorCreateUpdateCommand command) {
        Author toSave = modelMapper.map(command, Author.class);
        toSave.setDeleted(false);
        Author saved = authorRepository.create(toSave);
        return modelMapper.map(saved, AuthorInfo.class);
    }

    public List<AuthorInfo> findAll() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream()
                .map(author -> modelMapper.map(author, AuthorInfo.class))
                .collect(Collectors.toList());
    }

    public AuthorInfo findById(Integer authorId) {
        Author authorFound = findAuthor(authorId);
        return modelMapper.map(authorFound, AuthorInfo.class);
    }

    public List<BookInfoNoAuthor> findAllBooksOfAuthor(Integer authorId) {
        Author author = findAuthor(authorId);
        List<Book> books = author.getBooks();
        return books.stream()
                .map(book -> modelMapper.map(book, BookInfoNoAuthor.class))
                .collect(Collectors.toList());
    }


    public AuthorInfo update(Integer id, AuthorCreateUpdateCommand command) {
        Author toUpdate = findAuthor(id);
        modelMapper.map(command, toUpdate);
        return modelMapper.map(toUpdate, AuthorInfo.class);
    }


    public Author findAuthor(Integer authorId) {
        Optional<Author> authorOptional = authorRepository.findById(authorId);
        if (authorOptional.isEmpty()) {
            throw new AuthorNotFoundException(authorId);
        }
        return authorOptional.get();
    }
}
