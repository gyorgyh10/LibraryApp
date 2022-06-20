package hu.progmasters.library.service;

import hu.progmasters.library.domain.Borrowing;
import hu.progmasters.library.domain.Condition;
import hu.progmasters.library.domain.Exemplar;
import hu.progmasters.library.domain.User;
import hu.progmasters.library.dto.BorrowingInfo;
import hu.progmasters.library.dto.ExemplarInfoNoBorrowings;
import hu.progmasters.library.dto.UserInfo;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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

//    @Mock
//    ExemplarRepository exemplarRepository;
//
//    @Mock
//    UserRepository userRepository;


    @InjectMocks
    BorrowingService borrowingService;

    private Exemplar firstExemplar;
    private ExemplarInfoNoBorrowings firstExemplarInfo;
    private User firstUser;
    private UserInfo firstUserInfo;
    private Borrowing firstBorrowing;
    private BorrowingInfo firstBorrowingInfo;


    @BeforeEach
    void init() {
        borrowingService = new BorrowingService(borrowingRepository, exemplarService, userService, new ModelMapper());
        borrowingService.setBorrowingTime(20);
        borrowingService.setProlongation(10);
        firstExemplar = new Exemplar(1, 123, Condition.GOOD, true,
                null, null, false);
        firstExemplarInfo = new ExemplarInfoNoBorrowings(1, 123, Condition.GOOD,
                true, null);
        firstUser = new User(1, "Tom Sedaris", "Main Street, Nr. 1", "tom.sedaris@gmail.com",
                "3243555665", null, false);
        firstUserInfo = new UserInfo(1, "Tom Sedaris", "Main Street, Nr. 1",
                "tom.sedaris@gmail.com", "3243555665");
        firstBorrowing = new Borrowing(1, firstExemplar, firstUser, LocalDate.now(),
                LocalDate.now().plusDays(20), true);
        firstBorrowingInfo = new BorrowingInfo(1, firstExemplarInfo, firstUserInfo, LocalDate.now(),
                LocalDate.now().plusDays(20), true);
    }

    @Test
    void testCreate_exemplarUserExistAndExemplarBorrowable_borrowingCreated() {
        Borrowing borrowingToSave = new Borrowing(null, firstExemplar, firstUser, LocalDate.now(),
                LocalDate.now().plusDays(20), true);
    //    when(exemplarService.getExemplarRepository()).thenReturn(exemplarRepository);
        when(exemplarService.findExemplar(1)).thenReturn(firstExemplar);
       // when(userService.getUserRepository()).thenReturn(userRepository);
        when(userService.findUser(1)).thenReturn(firstUser);
        when(borrowingRepository.create(borrowingToSave)).thenReturn(firstBorrowing);
        ExemplarInfoNoBorrowings firstExemplarInfoSaved = new ExemplarInfoNoBorrowings(1, 123,
                Condition.GOOD, false, null);
        BorrowingInfo firstBorrowingInfoSaved= new BorrowingInfo(1, firstExemplarInfoSaved, firstUserInfo,
                LocalDate.now(), LocalDate.now().plusDays(20), true);
        BorrowingInfo actual = borrowingService.create(1, 1);
        assertThat(actual)
                .isEqualTo(firstBorrowingInfoSaved);
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }

    @Test
    void delete() {
    }

    @Test
    void update() {
    }

    @Test
    void prolongation() {
    }

    @Test
    void inactivation() {
    }
}