package hu.progmasters.library.service;

import hu.progmasters.library.domain.Borrowing;
import hu.progmasters.library.domain.Exemplar;
import hu.progmasters.library.domain.User;
import hu.progmasters.library.dto.BorrowingCreateCommand;
import hu.progmasters.library.dto.BorrowingInfo;
import hu.progmasters.library.exceptionhandling.BorrowingNotFoundException;
import hu.progmasters.library.exceptionhandling.ExemplarNotBorrowableException;
import hu.progmasters.library.exceptionhandling.ExemplarNotFoundException;
import hu.progmasters.library.exceptionhandling.UserNotFoundException;
import hu.progmasters.library.repository.BorrowingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final ExemplarService exemplarService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public BorrowingService(BorrowingRepository borrowingRepository, ExemplarService exemplarService,
                            UserService userService, ModelMapper modelMapper) {
        this.borrowingRepository = borrowingRepository;
        this.exemplarService = exemplarService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }


    public BorrowingInfo create(BorrowingCreateCommand command, Integer exemplarId, Integer userId) {
        Optional<Exemplar> optionalExemplar = exemplarService.getExemplarRepository().findById(exemplarId);
        if (optionalExemplar.isEmpty()) {
            throw new ExemplarNotFoundException(exemplarId);
        }
        Exemplar exemplarOfBorrowing = optionalExemplar.get();
        if (!exemplarOfBorrowing.getBorrowable()) {
            throw new ExemplarNotBorrowableException(exemplarId);
        }
        exemplarOfBorrowing.setBorrowable(false);
        Optional<User> optionalUser = userService.getUserRepository().findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        User userOfBorrowing = optionalUser.get();
        Borrowing toSave = new Borrowing();
        modelMapper.map(command, toSave);
        toSave.setExemplar(exemplarOfBorrowing);
        toSave.setUser(userOfBorrowing);
        toSave.setActive(true);
        Borrowing saved = borrowingRepository.create(toSave);

        return modelMapper.map(saved, BorrowingInfo.class);
    }


    public BorrowingInfo findById(Integer id) {
        Optional<Borrowing> optionalBorrowing = borrowingRepository.findById(id);
        if (optionalBorrowing.isEmpty()) {
            throw new BorrowingNotFoundException(id);
        }
        return modelMapper.map(optionalBorrowing.get(), BorrowingInfo.class);
    }

    public List<BorrowingInfo> findAll(Integer exemplarId, Integer userId) {
        List<Borrowing> borrowings = borrowingRepository.findAll(exemplarId, userId);
        return borrowings.stream()
                .map(borrowing -> modelMapper.map(borrowing, BorrowingInfo.class))
                .collect(Collectors.toList());
    }

    public void delete(Integer id) {
        Optional<Borrowing> optionalBorrowing = borrowingRepository.findById(id);
        if (optionalBorrowing.isEmpty()) {
            throw new BorrowingNotFoundException(id);
        }
        Borrowing toDelete=optionalBorrowing.get();
        borrowingRepository.delete(toDelete);

    }

    public BorrowingRepository getBorrowingRepository() {
        return borrowingRepository;
    }

}
