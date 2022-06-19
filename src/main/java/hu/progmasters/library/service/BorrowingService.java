package hu.progmasters.library.service;

import hu.progmasters.library.domain.Borrowing;
import hu.progmasters.library.domain.Exemplar;
import hu.progmasters.library.domain.User;
import hu.progmasters.library.dto.BorrowingCreateCommand;
import hu.progmasters.library.dto.BorrowingInfo;
import hu.progmasters.library.exceptionhandling.*;
import hu.progmasters.library.repository.BorrowingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.LocalDate.now;

@Service
@Transactional
public class BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final ExemplarService exemplarService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    @Value("${borrowing.time}")
    private long borrowingTime;
    @Value("${borrowing.prolongation}")
    private long borrowingProlongation;

    public BorrowingService(BorrowingRepository borrowingRepository, ExemplarService exemplarService,
                            UserService userService, ModelMapper modelMapper) {
        this.borrowingRepository = borrowingRepository;
        this.exemplarService = exemplarService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }


    public BorrowingInfo create(Integer exemplarId, Integer userId) {
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
        toSave.setFromDate(now());
        toSave.setToDate(now().plusDays(borrowingTime));
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
        Borrowing toDelete = findBorrowing(id);
        borrowingRepository.delete(toDelete);

    }


    public BorrowingRepository getBorrowingRepository() {
        return borrowingRepository;
    }

    public BorrowingInfo update(Integer id, BorrowingCreateCommand command) {
        Borrowing toUpdate = findBorrowing(id);
        modelMapper.map(command, toUpdate);
        return modelMapper.map(toUpdate, BorrowingInfo.class);
    }

    public BorrowingInfo prolongation(Integer id) {
        Borrowing forProlongation = findBorrowing(id);
        if (forProlongation.getFromDate().plusDays(borrowingTime + borrowingProlongation).isAfter(now())) {
            forProlongation.setToDate(forProlongation.getToDate().plusDays(borrowingProlongation));
        } else {
            throw new BorrowingTimeHasExpiredException(id);
        }

        return modelMapper.map(forProlongation, BorrowingInfo.class);
    }

    public BorrowingInfo inactivation(Integer id) {
        Borrowing forInactivation = findBorrowing(id);
        forInactivation.setToDate(now());
        forInactivation.setActive(false);
        forInactivation.getExemplar().setBorrowable(true);
        return modelMapper.map(forInactivation, BorrowingInfo.class);
    }

    private Borrowing findBorrowing(Integer id) {
        Optional<Borrowing> optionalBorrowing = borrowingRepository.findById(id);
        if (optionalBorrowing.isEmpty()) {
            throw new BorrowingNotFoundException(id);
        }
        return optionalBorrowing.get();
    }

}
