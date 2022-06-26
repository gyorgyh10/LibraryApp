package hu.progmasters.library.service;

import hu.progmasters.library.domain.Author;
import hu.progmasters.library.domain.Book;
import hu.progmasters.library.domain.Genre;
import hu.progmasters.library.dto.AuthorCreateUpdateCommand;
import hu.progmasters.library.dto.AuthorInfo;
import hu.progmasters.library.dto.BookInfoNoAuthor;
import hu.progmasters.library.exceptionhandling.AuthorHasBooksException;
import hu.progmasters.library.exceptionhandling.AuthorNotFoundException;
import hu.progmasters.library.exceptionhandling.UserHasActiveBorrowingsException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    AuthorRepository authorRepository;

    @InjectMocks
    AuthorService authorService;
    private BookInfoNoAuthor firstBookInfoNoAuthor;
    private BookInfoNoAuthor secondBookInfoNoAuthor;
    private Author firstAuthor;
    private AuthorInfo firstAuthorInfo;
    private Author secondAuthor;
    private AuthorInfo secondAuthorInfo;


    @BeforeEach
    void init() {
        authorService = new AuthorService(authorRepository, new ModelMapper());
        Book firstBook = new Book(1, "4321344", "The One", secondAuthor, 232,
                "The Best", 1999, Genre.FANTASY_AND_SF, List.of(), false);
        Book secondBook = new Book(2, "4321345", "The Second", secondAuthor, 200,
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
    void testCreate_newSueGrafton_SueGraftonSaved() {
        AuthorCreateUpdateCommand command = new AuthorCreateUpdateCommand("Sue Grafton");
        Author firstToSave = new Author(null, "Sue Grafton", null, false);
        when(authorRepository.create(firstToSave)).thenReturn(firstAuthor);
        AuthorInfo saved = authorService.create(command);
        assertThat(saved).isEqualTo(firstAuthorInfo);
    }

    @Test
    void testFindAllBooksOfAuthor() {
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
    void testUpdate_updateSueGrafton_updatedToSueGriff() {
        when(authorRepository.findById(1)).thenReturn(Optional.of(firstAuthor));
        AuthorCreateUpdateCommand command = new AuthorCreateUpdateCommand("Sue Griff");
        AuthorInfo updated = new AuthorInfo(1, "Sue Griff");

        assertThat(authorService.update(1, command))
                .isEqualTo(updated);
    }

    @Test
    void testFindAuthor_wrongId_exceptionThrown(){
        when(authorRepository.findById(11)).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class, ()->authorService.findAuthor(11));
    }

    @Test
    void testFindById_existingAuthor_authorReturned() {
        when(authorRepository.findById(1)).thenReturn(Optional.of(firstAuthor));

        assertThat(authorService.findById(1))
                .isEqualTo(firstAuthorInfo);
    }

    @Test
    void testDelete_existingAuthorWithNoBooks_setDeletedTrue() {
        when(authorRepository.findById(1)).thenReturn(Optional.of(firstAuthor));

        authorService.delete(1);
        assertTrue(firstAuthor.getDeleted());
    }

    @Test
    void testDelete_AuthorWithABooks_notDeletedAndExceptionThrown() {
        when(authorRepository.findById(2)).thenReturn(Optional.of(secondAuthor));

        assertThrows(AuthorHasBooksException.class, () -> authorService.delete(2));
    }

}