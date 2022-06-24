package hu.progmasters.library.service;

import hu.progmasters.library.domain.*;
import hu.progmasters.library.dto.*;
import hu.progmasters.library.exceptionhandling.ExemplarIsInActiveBorrowingException;
import hu.progmasters.library.repository.ExemplarRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExemplarServiceTest {

    @Mock
    ExemplarRepository exemplarRepository;

    @Mock
    BookService bookService;

    @InjectMocks
    ExemplarService exemplarService;

    private final ModelMapper modelMapper = new ModelMapper();
    private Book firstBook;
    private BookInfo firstBookInfo;
    private BookInfoMin firstBookInfoMin;
    private Exemplar firstExemplar;
    private ExemplarInfo firstExemplarInfo;
    private Exemplar secondExemplar;
    private ExemplarInfo secondExemplarInfo;

    @BeforeEach
    void init() {
        exemplarService = new ExemplarService(exemplarRepository, bookService, modelMapper);
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        Author firstAuthor = new Author(1, "Sue Grafton", List.of(), false);
        AuthorInfo firstAuthorInfo = new AuthorInfo(1, "Sue Grafton");
        firstBook = new Book(1, "4321344", "The One", firstAuthor, 232,
                "The Best", 1999, Genre.FANTASY_AND_SF, List.of(), false);
        firstBookInfo = new BookInfo(1, "4321344", "The One", firstAuthorInfo, 232,
                "The Best", 1999, Genre.FANTASY_AND_SF);
        firstBookInfoMin = new BookInfoMin(1, "The One");
        firstExemplar = new Exemplar(1, 123, Condition.GOOD, true, firstBook,
                null, false);
        firstExemplarInfo = new ExemplarInfo(1, 123, Condition.GOOD, true, firstBookInfoMin);
        secondExemplar = new Exemplar(2, 124, Condition.USED, true, firstBook,
                null, false);
        secondExemplarInfo = new ExemplarInfo(2, 124, Condition.USED, true, firstBookInfoMin);

    }

    @Test
    void testCreate_newExemplar_exemplarSaved() {
        ExemplarCreateUpdateCommand command = new ExemplarCreateUpdateCommand(123, Condition.GOOD, true);
        Exemplar firstToSave = new Exemplar(null, 123, Condition.GOOD, true, null,
                null, false);
        when(exemplarRepository.create(firstToSave)).thenReturn(firstExemplar);
        ExemplarInfo saved = exemplarService.create(command, 1);
        assertThat(saved).isEqualTo(firstExemplarInfo);
    }


    @Test
    void testFindAll_atStart_emptyList() {
        when(exemplarRepository.findAll()).thenReturn(List.of());
        assertThat(exemplarService.findAll()).isEmpty();
    }

    @Test
    void testFindAll_containingAuthors_exemplarsReturned() {
        when(exemplarRepository.findAll()).thenReturn(List.of(firstExemplar, secondExemplar));

        assertThat(exemplarService.findAll())
                .hasSize(2)
                .containsExactly(firstExemplarInfo, secondExemplarInfo);
    }


    @Test
    void testFindById_existingExemplar_exemplarReturned() {
        when(exemplarRepository.findById(1)).thenReturn(Optional.of(firstExemplar));
        ExemplarInfoAll firstExemplarInfoAll = new ExemplarInfoAll(1, 123, Condition.GOOD,
                true, firstBookInfo, null);
        assertThat(exemplarService.findById(1))
                .isEqualTo(firstExemplarInfoAll);
    }

    @Test
    void testDelete_withActiveBorrowing_ExemplarIsInActiveBorrowingExceptionThrown() {
        Exemplar firstExemplarWithActiveBorrowing = new Exemplar(1, 123, Condition.GOOD,
                false, firstBook, null, false);
        when(exemplarRepository.findById(1)).thenReturn(Optional.of(firstExemplarWithActiveBorrowing));

        assertThrows(ExemplarIsInActiveBorrowingException.class, () -> exemplarService.delete(1));
    }

    @Test
    void testUpdate_update123GOOD_updatedTo123USED() {
        when(exemplarRepository.findById(1)).thenReturn(Optional.of(firstExemplar));
        ExemplarCreateUpdateCommand command = new ExemplarCreateUpdateCommand(123, Condition.USED,
                true);
        ExemplarInfo updated = new ExemplarInfo(1, 123, Condition.USED, true, firstBookInfoMin);

        assertThat(exemplarService.update(1, command))
                .isEqualTo(updated);
    }

}