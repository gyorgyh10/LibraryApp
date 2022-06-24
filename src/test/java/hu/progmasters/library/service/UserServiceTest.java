package hu.progmasters.library.service;

import hu.progmasters.library.domain.Borrowing;
import hu.progmasters.library.domain.User;
import hu.progmasters.library.dto.UserCreateCommand;
import hu.progmasters.library.dto.UserInfo;
import hu.progmasters.library.exceptionhandling.AuthorNotFoundException;
import hu.progmasters.library.exceptionhandling.UserHasActiveBorrowingsException;
import hu.progmasters.library.exceptionhandling.UserNotFoundException;
import hu.progmasters.library.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;
    private User firstUser;
    private UserInfo firstUserInfo;
    private User secondUser;
    private UserInfo secondUserInfo;


    @BeforeEach
    void init() {
        userService = new UserService(userRepository, new ModelMapper());
        Borrowing firstBorrowing = new Borrowing(1, null, null, null, null, true);
        firstUser = new User(1, "Bill Thomson", "Main Street, Nr. 7", "bill.thomson@gmail.com",
                "3234565434", List.of(), false);
        firstUserInfo = new UserInfo(1, "Bill Thomson", "Main Street, Nr. 7",
                "bill.thomson@gmail.com", "3234565434");
        secondUser = new User(2, "Tom Hover", "Jackson street, Nr. 3", "tom.hover@gmail.com",
                "3245366675", new ArrayList<>(List.of(firstBorrowing)), false);
        secondUserInfo = new UserInfo(2, "Tom Hover", "Jackson street, Nr. 3",
                "tom.hover@gmail.com", "3245366675");
    }

    @Test
    void testCreateUser_newBillThomson_BillThomsonSaved() {
        UserCreateCommand command = new UserCreateCommand("Bill Thomson", "Main Street, Nr. 7",
                "bill.thomson@gmail.com", "3234565434");
        User firstToSave = new User(null, "Bill Thomson", "Main Street, Nr. 7",
                "bill.thomson@gmail.com", "3234565434", null, false);
        when(userRepository.create(firstToSave)).thenReturn(firstUser);
        UserInfo saved = userService.create(command);
        assertThat(saved).isEqualTo(firstUserInfo);
    }


    @Test
    void testFindAll_atStart_emptyList() {
        when(userRepository.findAll()).thenReturn(List.of());
        assertThat(userService.findAll()).isEmpty();
    }

    @Test
    void testFindAll_containingUsers_usersReturned() {
        when(userRepository.findAll()).thenReturn(List.of(firstUser, secondUser));

        assertThat(userService.findAll())
                .hasSize(2)
                .containsExactly(firstUserInfo, secondUserInfo);
    }

    @Test
    void testUpdate_updateBillThomson_updatedToBillThomas() {
        when(userRepository.findById(1)).thenReturn(Optional.of(firstUser));
        UserCreateCommand command = new UserCreateCommand("Bill Thomas", "Main Street, Nr. 7",
                "bill.thomas@gmail.com", "3234565434");
        UserInfo updated = new UserInfo(1, "Bill Thomas", "Main Street, Nr. 7",
                "bill.thomas@gmail.com", "3234565434");

        assertThat(userService.update(1, command))
                .isEqualTo(updated);
    }


    @Test
    void testDelete_existingUserNoActiveBorrowing_setDeletedTrue() {
        when(userRepository.findById(1)).thenReturn(Optional.of(firstUser));

        userService.delete(1);
        assertTrue(firstUser.getDeleted());
    }

    @Test
    void testDelete_UserWithActiveBorrowing_notDeletedAndExceptionThrown() {
        when(userRepository.findById(2)).thenReturn(Optional.of(secondUser));

        assertThrows(UserHasActiveBorrowingsException.class, () -> userService.delete(2));
    }

    @Test
    void testFindAllBorrowings_userHasOneBorrowing_borrowingListed() {
        when(userRepository.findById(2)).thenReturn(Optional.of(secondUser));

        assertThat(userService.findAllBorrowings(2))
                .hasSize(1);
    }

    @Test
    void testFindById_existingUser_userReturned() {
        when(userRepository.findById(1)).thenReturn(Optional.of(firstUser));

        assertThat(userService.findById(1))
                .isEqualTo(firstUserInfo);
    }

    @Test
    void testFindUser_wrongId_exceptionThrown(){
        when(userRepository.findById(11)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, ()->userService.findUser(11));
    }
}