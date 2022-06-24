package hu.progmasters.library.service;

import hu.progmasters.library.domain.Author;
import hu.progmasters.library.domain.Book;
import hu.progmasters.library.domain.Genre;
import hu.progmasters.library.dto.AuthorInfo;
import hu.progmasters.library.dto.BookCreateUpdateCommand;
import hu.progmasters.library.dto.BookInfo;
import hu.progmasters.library.exceptionhandling.BookNotFoundException;
import hu.progmasters.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    AuthorService authorService;

    @InjectMocks
    BookService bookService;

    private final ModelMapper modelMapper = new ModelMapper();
    private Author firstAuthor;
    private AuthorInfo firstAuthorInfo;
    private Book firstBook;
    private BookInfo firstBookInfo;
    private Book secondBook;
    private BookInfo secondBookInfo;


    @BeforeEach
    void init() {
        bookService = new BookService(bookRepository, authorService, modelMapper);
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        firstAuthor = new Author(1, "Sue Grafton", List.of(), false);
        firstAuthorInfo = new AuthorInfo(1, "Sue Grafton");
        firstBook = new Book(1, "4321344", "The One", firstAuthor, 232,
                "The Best", 1999, Genre.FANTASY_AND_SF, List.of(), false);
        firstBookInfo = new BookInfo(1, "4321344", "The One", firstAuthorInfo, 232,
                "The Best", 1999, Genre.FANTASY_AND_SF);
        secondBook = new Book(2, "4321345", "The Second", firstAuthor, 200,
                "The Best", 2001, Genre.FANTASY_AND_SF, List.of(), false);
        secondBookInfo = new BookInfo(2, "4321345", "The Second", firstAuthorInfo, 200,
                "The Best", 2001, Genre.FANTASY_AND_SF);
    }

    @Test
    void testCreate_newTheOne_TheOneCreated() {
        when(authorService.findAuthor(1)).thenReturn(firstAuthor);
        BookCreateUpdateCommand command = new BookCreateUpdateCommand("4321344", "The One", 1, 232,
                "The Best", 1999, Genre.FANTASY_AND_SF);
        Book bookToSave = new Book(null, "4321344", "The One", firstAuthor, 232,
                "The Best", 1999, Genre.FANTASY_AND_SF, null, false);

        when(bookRepository.save(bookToSave)).thenReturn(firstBook);

        assertThat(bookService.create(command))
                .isEqualTo(firstBookInfo);
    }


    @Test
    void testFindAll_atStart_emptyList() {
        when(bookRepository.findAll(any())).thenReturn(List.of());
        assertThat(bookService.findAll(null)).isEmpty();
    }

    @Test
    void testFindAll_containingBooks_booksReturned() {
        when(bookRepository.findAll(any())).thenReturn(List.of(firstBook, secondBook));

        assertThat(bookService.findAll(null))
                .hasSize(2)
                .containsExactly(firstBookInfo, secondBookInfo);
    }


    @Test
    void testFindById_existingBook_bookReturned() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(firstBook));

        assertThat(bookService.findById(1))
                .isEqualTo(firstBookInfo);
    }

    @Test
    void testFindBook_wrongId_exceptionThrown(){
        when(bookRepository.findById(11)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, ()->bookService.findBook(11));
    }

    @Test
    void testUpdate_updateTheOne_updatedToTheOnlyOne() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(firstBook));
        BookCreateUpdateCommand command = new BookCreateUpdateCommand("4321344", "The Only One", 1,
                200,"The Best", 2000, Genre.FANTASY_AND_SF);
        BookInfo updated = new BookInfo(1, "4321344", "The Only One", firstAuthorInfo, 200,
                "The Best", 2000, Genre.FANTASY_AND_SF);

        assertThat(bookService.update(1, command))
                .isEqualTo(updated);
    }

}