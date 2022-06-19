package hu.progmasters.library.service;

import hu.progmasters.library.domain.Author;
import hu.progmasters.library.domain.Book;
import hu.progmasters.library.domain.Genre;
import hu.progmasters.library.dto.AuthorCreateUpdateCommand;
import hu.progmasters.library.dto.AuthorInfo;
import hu.progmasters.library.dto.BookInfoNoAuthor;
import hu.progmasters.library.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    AuthorRepository authorRepository;

    @InjectMocks
    AuthorService authorService;
    private Book firstBook;
    private Book secondBook;
    private BookInfoNoAuthor firstBookInfoNoAuthor;
    private BookInfoNoAuthor secondBookInfoNoAuthor;
    private Author firstAuthor;
    private AuthorInfo firstAuthorInfo;
    private Author secondAuthor;
    private AuthorInfo secondAuthorInfo;


    @BeforeEach
    void init() {
        authorService = new AuthorService(authorRepository, new ModelMapper());
        firstBook = new Book(1, "4321344", "The One", secondAuthor, 232,
                "The Best", 1999, Genre.FANTASY_AND_SF, List.of(), false);
        secondBook = new Book(2, "4321345", "The Second", secondAuthor, 200,
                "The Best", 2001, Genre.FANTASY_AND_SF, List.of(), false);
        firstBookInfoNoAuthor = new BookInfoNoAuthor(1, "4321344", "The One", 232,
                "The Best", 1999, Genre.FANTASY_AND_SF);
        secondBookInfoNoAuthor = new BookInfoNoAuthor(2, "4321345", "The Second", 200,
                "The Best", 2001, Genre.FANTASY_AND_SF);
        firstAuthor = new Author(1, "Sue Grafton", List.of(), false);
        firstAuthorInfo = new AuthorInfo(1, "Sue Grafton");
        secondAuthor = new Author(2, "Coleen Hoover", List.of(firstBook, secondBook), false);
        secondAuthorInfo = new AuthorInfo(2, "Coleen Hoover");
    }

    @Test
    void test_createAuthor_newSueGrafton_SueGraftonSaved() {
        AuthorCreateUpdateCommand command = new AuthorCreateUpdateCommand("Sue Grafton");
        Author firstToSave = new Author(null, "Sue Grafton", null, false);
        when(authorRepository.create(firstToSave)).thenReturn(firstAuthor);
        AuthorInfo saved = authorService.createAuthor(command);
        assertThat(saved).isEqualTo(firstAuthorInfo);
    }

    @Test
    void findAllBooksOfAuthor() {
        when(authorRepository.findById(2)).thenReturn(Optional.of(secondAuthor));
        assertThat(authorService.findAllBooksOfAuthor(2))
                .hasSize(2)
                .containsExactly(firstBookInfoNoAuthor, secondBookInfoNoAuthor);
    }


    @Test
    void testFindAll_atStart_emptyList() {
        when(authorRepository.findAll()).thenReturn(List.of());
        assertThat(authorService.findAll()).isEmpty();
    }

    @Test
    void testFindAll_containingAuthors_authorsReturned() {
        when(authorRepository.findAll()).thenReturn(List.of(firstAuthor, secondAuthor));

        assertThat(authorService.findAll())
                .hasSize(2)
                .containsExactly(firstAuthorInfo, secondAuthorInfo);
    }

    @Test
    void update_updateSueGrafton_updatedToSueGriff() {
        when(authorRepository.findById(1)).thenReturn(Optional.of(firstAuthor));
        AuthorCreateUpdateCommand command = new AuthorCreateUpdateCommand("Sue Griff");
        AuthorInfo updated = new AuthorInfo(1, "Sue Griff");

        assertThat(authorService.update(1, command))
                .isEqualTo(updated);
    }


}