package hu.progmasters.library.service;

import hu.progmasters.library.domain.Borrowing;
import hu.progmasters.library.domain.User;
import hu.progmasters.library.dto.BorrowingInfoNoUser;
import hu.progmasters.library.dto.UserCreateCommand;
import hu.progmasters.library.dto.UserInfo;
import hu.progmasters.library.exceptionhandling.UserHasActiveBorrowingsException;
import hu.progmasters.library.exceptionhandling.UserNotFoundException;
import hu.progmasters.library.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public UserInfo create(UserCreateCommand command) {
        User toSave = modelMapper.map(command, User.class);
        toSave.setDeleted(false);
        User saved = userRepository.create(toSave);
        return modelMapper.map(saved, UserInfo.class);
    }

    public List<UserInfo> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserInfo.class))
                .collect(Collectors.toList());
    }

    public UserInfo findById(Integer id) {
        User userFound = findUser(id);
        return modelMapper.map(userFound, UserInfo.class);
    }


    public UserInfo update(Integer id, UserCreateCommand command) {
        User toUpdate = findUser(id);
        modelMapper.map(command, toUpdate);
        return modelMapper.map(toUpdate, UserInfo.class);
    }


    public void delete(Integer id) {
        User userToDelete = findUser(id);
        List<Borrowing> activeBorrowings = getActiveBorrowingsOfUser(userToDelete);
        if (activeBorrowings.isEmpty()) {
            userToDelete.setDeleted(true);
        } else {
            throw new UserHasActiveBorrowingsException(id);
        }
    }

    private List<Borrowing> getActiveBorrowingsOfUser(User user) {
        return user.getBorrowings().stream()
                .filter(Borrowing::getActive)
                .collect(Collectors.toList());
    }

    public List<BorrowingInfoNoUser> findAllBorrowings(Integer id) {
        User user = findUser(id);
        return user.getBorrowings().stream()
                .map(borrowing -> modelMapper.map(borrowing, BorrowingInfoNoUser.class))
                .collect(Collectors.toList());
    }

    public User findUser(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty() || userOptional.get().getDeleted()) {
            throw new UserNotFoundException(id);
        }
        return userOptional.get();
    }

   }
