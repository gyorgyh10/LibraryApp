package hu.progmasters.library.service;

import hu.progmasters.library.domain.Borrowing;
import hu.progmasters.library.domain.Condition;
import hu.progmasters.library.domain.Exemplar;
import hu.progmasters.library.domain.User;
import hu.progmasters.library.dto.*;
import hu.progmasters.library.exceptionhandling.BorrowingNotFoundException;
import hu.progmasters.library.exceptionhandling.BorrowingTimeHasExpiredException;
import hu.progmasters.library.exceptionhandling.UserNotFoundException;
import hu.progmasters.library.repository.BorrowingRepository;
import hu.progmasters.library.repository.ExemplarRepository;
import hu.progmasters.library.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BorrowingServiceTest {

    @Mock
    BorrowingRepository borrowingRepository;

    @Mock
    ExemplarService exemplarService;

    @Mock
    UserService userService;

    @InjectMocks
    BorrowingService borrowingService;

    private Exemplar firstExemplar;
    private ExemplarInfoNoBorrowings firstExemplarInfo;
    private User firstUser;
    private UserInfo firstUserInfo;
    private Borrowing firstBorrowing;
    private Borrowing secondBorrowing;
    private BorrowingInfo firstBorrowingInfo;
    private BorrowingInfo secondBorrowingInfo;



    @BeforeEach
    void init() {
        borrowingService = new BorrowingService(borrowingRepository, exemplarService, userService, new ModelMapper());
        borrowingService.setBorrowingTime(20);
        borrowingService.setProlongation(10);
        firstExemplar = new Exemplar(1, 123, Condition.GOOD, true,
                null, null, false);
        firstExemplarInfo = new ExemplarInfoNoBorrowings(1, 123, Condition.GOOD,
                true, null);
        Exemplar secondExemplar = new Exemplar(2, 124, Condition.GOOD, true,
                null, null, false);
        ExemplarInfoNoBorrowings secondExemplarInfo = new ExemplarInfoNoBorrowings(2, 124,
                Condition.GOOD, true, null);
        firstUser = new User(1, "Tom Sedaris", "Main Street, Nr. 1", "tom.sedaris@gmail.com",
                "3243555665", null, false);
        firstUserInfo = new UserInfo(1, "Tom Sedaris", "Main Street, Nr. 1",
                "tom.sedaris@gmail.com", "3243555665");
        firstBorrowing = new Borrowing(1, firstExemplar, firstUser, now(),
                now().plusDays(20), true);
        firstBorrowingInfo = new BorrowingInfo(1, firstExemplarInfo, firstUserInfo, now(),
                now().plusDays(20), true);
        secondBorrowing = new Borrowing(2, secondExemplar, firstUser, now(),
                now().plusDays(20), true);
        secondBorrowingInfo = new BorrowingInfo(2, secondExemplarInfo, firstUserInfo, now(),
                now().plusDays(20), true);
    }

    @Test
    void testCreate_exemplarUserExistAndExemplarBorrowable_borrowingCreated() {
        Borrowing borrowingToSave = new Borrowing(null, firstExemplar, firstUser, now(),
                now().plusDays(20), true);
        when(exemplarService.findExemplar(1)).thenReturn(firstExemplar);
        when(userService.findUser(1)).thenReturn(firstUser);
        when(borrowingRepository.create(borrowingToSave)).thenReturn(firstBorrowing);
        ExemplarInfoNoBorrowings firstExemplarInfoSaved = new ExemplarInfoNoBorrowings(1, 123,
                Condition.GOOD, false, null);
        BorrowingInfo firstBorrowingInfoSaved= new BorrowingInfo(1, firstExemplarInfoSaved, firstUserInfo,
                now(), now().plusDays(20), true);
        BorrowingInfo actual = borrowingService.create(1, 1);
        assertThat(actual)
                .isEqualTo(firstBorrowingInfoSaved);
    }


    @Test
    void testFindAll_atStart_emptyList() {
        when(borrowingRepository.findAll(any(), any())).thenReturn(List.of());
        assertThat(borrowingService.findAll(null, null)).isEmpty();
    }

    @Test
    void testFindAll_containingBorrowings_borrowingsReturned() {
        when(borrowingRepository.findAll(any(), any())).thenReturn(List.of(firstBorrowing, secondBorrowing));

        assertThat(borrowingService.findAll(null, null))
                .hasSize(2)
                .containsExactly(firstBorrowingInfo, secondBorrowingInfo);
    }


    @Test
    void testFindAll_containingBorrowingsOfExemplar_borrowingsOfExemplarReturned() {
        when(borrowingRepository.findAll(any(), any())).thenReturn(List.of(firstBorrowing));

        assertThat(borrowingService.findAll(1, null))
                .hasSize(1)
                .containsExactly(firstBorrowingInfo);
    }


    @Test
    void testUpdate_newDatesGiven_newDatesSet() {
        when(borrowingRepository.findById(1)).thenReturn(Optional.of(firstBorrowing));
        BorrowingUpdateCommand command = new BorrowingUpdateCommand(LocalDate.of(2022, 6, 24),
                LocalDate.of(2022, 7, 10));
        BorrowingInfo updated = new BorrowingInfo(1, firstExemplarInfo, firstUserInfo,
                LocalDate.of(2022, 6, 24),
                LocalDate.of(2022, 7, 10), true);

        assertThat(borrowingService.update(1, command))
                .isEqualTo(updated);
    }

    @Test
    void testProlongation_prolongationPossible_prolongationMade() {
        Borrowing forProlongation = new Borrowing(3, firstExemplar, firstUser,
                LocalDate.of(2022, 6, 24),
                LocalDate.of(2022, 7, 10), true);
        when(borrowingRepository.findById(3)).thenReturn(Optional.of(forProlongation));

        BorrowingInfo prolongated = new BorrowingInfo(3, firstExemplarInfo, firstUserInfo,
                LocalDate.of(2022, 6, 24),
                LocalDate.of(2022, 7, 20), true);

        assertThat(borrowingService.prolongation(3))
                .isEqualTo(prolongated);
    }


    @Test
    void testProlongation_prolongationNotPossible_borrowingTimeHasExpiredExceptionThrown() {
        Borrowing forProlongation = new Borrowing(3, firstExemplar, firstUser,
                LocalDate.of(2022, 5, 24),
                LocalDate.of(2022, 6, 20), true);
        when(borrowingRepository.findById(3)).thenReturn(Optional.of(forProlongation));

        assertThrows(BorrowingTimeHasExpiredException.class, ()->borrowingService.prolongation(3));
    }

    @Test
    void inactivation() {
        Borrowing forInactivation = new Borrowing(3, firstExemplar, firstUser,
                LocalDate.of(2022, 5, 24),
                LocalDate.of(2022, 6, 20), true);
        when(borrowingRepository.findById(3)).thenReturn(Optional.of(forInactivation));

        BorrowingInfo inactivatedBorrowing = borrowingService.inactivation(3);
        assertThat(inactivatedBorrowing.getToDate())
                .isToday();

        assertThat(inactivatedBorrowing.getActive())
                .isFalse();

        assertThat(inactivatedBorrowing.getExemplar().getBorrowable())
                .isTrue();
    }

    @Test
    void testFindById_existingBorrowing_borrowingReturned() {
        when(borrowingRepository.findById(1)).thenReturn(Optional.of(firstBorrowing));

        assertThat(borrowingService.findById(1))
                .isEqualTo(firstBorrowingInfo);
    }

    @Test
    void testFindBorrowing_wrongId_exceptionThrown(){
        when(borrowingRepository.findById(11)).thenReturn(Optional.empty());

        assertThrows(BorrowingNotFoundException.class, ()->borrowingService.findBorrowing(11));
    }
}