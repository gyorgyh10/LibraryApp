package hu.progmasters.library.service;

import hu.progmasters.library.domain.Borrowing;
import hu.progmasters.library.domain.Exemplar;
import hu.progmasters.library.domain.User;
import hu.progmasters.library.dto.BorrowingInfo;
import hu.progmasters.library.dto.BorrowingUpdateCommand;
import hu.progmasters.library.exceptionhandling.BorrowingNotFoundException;
import hu.progmasters.library.exceptionhandling.BorrowingTimeHasExpiredException;
import hu.progmasters.library.exceptionhandling.ExemplarNotBorrowableException;
import hu.progmasters.library.repository.BorrowingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
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
    private long borrowingTime;
    private long prolongation;

    public BorrowingService(BorrowingRepository borrowingRepository, ExemplarService exemplarService,
                            UserService userService, ModelMapper modelMapper) {
        this.borrowingRepository = borrowingRepository;
        this.exemplarService = exemplarService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }


    public BorrowingInfo create(Integer exemplarId, Integer userId) {
        Exemplar exemplarOfBorrowing = exemplarService.findExemplar(exemplarId);
        if (!exemplarOfBorrowing.getBorrowable()) {
            throw new ExemplarNotBorrowableException(exemplarId);
        }
        User userOfBorrowing = userService.findUser(userId);
        exemplarOfBorrowing.setBorrowable(false);
        Borrowing toSave = new Borrowing();
        LocalDate now = now();
        toSave.setFromDate(now);
        System.out.println(borrowingTime);
        LocalDate toDate = now.plusDays(borrowingTime);
        toSave.setToDate(toDate);
        toSave.setExemplar(exemplarOfBorrowing);
        toSave.setUser(userOfBorrowing);
        toSave.setActive(true);
        Borrowing saved = borrowingRepository.create(toSave);

        return modelMapper.map(saved, BorrowingInfo.class);
    }


    public List<BorrowingInfo> findAll(Integer exemplarId, Integer userId) {
        List<Borrowing> borrowings = borrowingRepository.findAll(exemplarId, userId);
        return borrowings.stream()
                .map(borrowing -> modelMapper.map(borrowing, BorrowingInfo.class))
                .collect(Collectors.toList());
    }

    public BorrowingInfo findById(Integer id) {
        Borrowing borrowingFound = findBorrowing(id);
        return modelMapper.map(borrowingFound, BorrowingInfo.class);
    }

    public void delete(Integer id) {
        Borrowing toDelete = findBorrowing(id);
        toDelete.getExemplar().setBorrowable(true);
        borrowingRepository.delete(toDelete);
    }

    public BorrowingInfo update(Integer id, BorrowingUpdateCommand command) {
        Borrowing toUpdate = findBorrowing(id);
        modelMapper.map(command, toUpdate);
        return modelMapper.map(toUpdate, BorrowingInfo.class);
    }

    public BorrowingInfo prolongation(Integer id) {
        Borrowing forProlongation = findBorrowing(id);
        if (forProlongation.getFromDate().plusDays(borrowingTime + prolongation).isAfter(now())) {
            forProlongation.setToDate(forProlongation.getToDate().plusDays(prolongation));
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

    public Borrowing findBorrowing(Integer id) {
        Optional<Borrowing> optionalBorrowing = borrowingRepository.findById(id);
        if (optionalBorrowing.isEmpty()) {
            throw new BorrowingNotFoundException(id);
        }
        return optionalBorrowing.get();
    }

    @Value("${borrowing.time}")
    public void setBorrowingTime(long borrowingTime) {
        this.borrowingTime = borrowingTime;
    }

    @Value("${borrowing.prolongation}")
    public void setProlongation(long prolongation) {
        this.prolongation = prolongation;
    }
}
