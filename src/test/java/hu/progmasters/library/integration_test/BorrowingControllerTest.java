package hu.progmasters.library.integration_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import hu.progmasters.library.domain.Borrowing;
import hu.progmasters.library.domain.Condition;
import hu.progmasters.library.domain.Genre;
import hu.progmasters.library.dto.*;
import hu.progmasters.library.repository.BorrowingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BorrowingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    BorrowingRepository repository;

    @Autowired
    ModelMapper modelMapper;

    private final ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();

    private ExemplarCreateCommand firstExemplarToSave;
    private BorrowingInfo firstBorrowingSaved;
    private ExemplarCreateCommand secondExemplarToSave;
    private BorrowingInfo secondBorrowingSaved;
    private BorrowingInfo borrowingUpdated;
    private BookCreateCommand firstBookToSave;
    private AuthorCreateUpdateCommand firstAuthorToSave;
    private UserCreateCommand firstUserToSave;

    @BeforeEach
    void init() {
        initBook();
        initFirstExemplar();
        initSecondExemplar();
        initFirstUser();
        initFirstBorrowing();

//        initSecondBorrowing();
    }

    @Test
    void testSave_exemplar123_savedInfoReturnedAndExemplar123InTheList() throws Exception {
        mockMvc.perform(post("/api/library/authors")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstAuthorToSave)));
        mockMvc.perform(post("/api/library/books")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstBookToSave)));
        mockMvc.perform(post("/api/library/exemplars/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstExemplarToSave)));
        mockMvc.perform(post("/api/library/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstUserToSave)));

        mockMvc.perform(post("/api/library/borrowings/1/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString("")))
                .andExpect(status().isCreated());
//                .andExpect(content().json(objectMapper.writeValueAsString(firstBorrowingSaved)));
        assertThat(repository.findAll(1, 1))
                .hasSize(1)
                .extracting(Borrowing::getId, Borrowing::getFromDate, Borrowing::getToDate, Borrowing::getActive)
                .containsExactly(tuple(firstBorrowingSaved.getId(), firstBorrowingSaved.getFromDate(),
                        firstBorrowingSaved.getToDate(), firstBorrowingSaved.getActive()));

        mockMvc.perform(get("/api/library/borrowings"))
                .andExpect(status().isOk());
//                .andExpect(content().json(objectMapper.writeValueAsString(List.of(firstBorrowingSaved))));

        mockMvc.perform(get("/api/library/borrowings/1"))
                .andExpect(status().isOk());
    }

//    @Test
//    void testSave_twoExemplars_infosReturnedAndAllInTheList() throws Exception {
//        mockMvc.perform(post("/api/library/authors")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(firstAuthorToSave)));
//        mockMvc.perform(post("/api/library/books")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(firstBookToSave)));
//        mockMvc.perform(post("/api/library/exemplars/1")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsString(firstExemplarToSave)))
//                .andExpect(status().isCreated())
//                .andExpect(content().json(objectMapper.writeValueAsString(firstBorrowingSaved)));
//
//
//        mockMvc.perform(post("/api/library/exemplars/1")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsString(secondExemplarToSave)))
//                .andExpect(status().isCreated())
//                .andExpect(content().json(objectMapper.writeValueAsString(secondSaved)));
//
//
//        assertThat(repository.findAll())
//                .hasSize(2)
//                .extracting(Exemplar::getId, Exemplar::getInventoryNumber, Exemplar::getCondition, Exemplar::getBorrowable)
//                .contains(tuple(firstBorrowingSaved.getId(), firstBorrowingSaved.getInventoryNumber(),
//                        firstBorrowingSaved.getCondition(), firstBorrowingSaved.getBorrowable()))
//                .contains(tuple(secondSaved.getId(), secondSaved.getInventoryNumber(),
//                        secondSaved.getCondition(), secondSaved.getBorrowable()));
//
//        assertThat(repository.findById(2).get().getInventoryNumber())
//                .isEqualTo(212);
//
//        mockMvc.perform(get("/api/library/exemplars"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(
//                        List.of(firstBorrowingSaved, secondSaved)))
//                );
//
//        mockMvc.perform(get("/api/library/exemplars/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(firstBorrowingSaved)));
//    }
//
//    @Test
//    void testUpdate_exemplar123GOOD_updated_exemplar333USED() throws Exception {
//        mockMvc.perform(post("/api/library/authors")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(firstAuthorToSave)));
//        mockMvc.perform(post("/api/library/books")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(firstBookToSave)));
//        mockMvc.perform(post("/api/library/exemplars/1")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(firstExemplarToSave)));
//
//        mockMvc.perform(put("/api/library/exemplars/1")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsString(firstToUpdate)))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(firstUpdated)));
//
//        assertThat(repository.findAll())
//                .hasSize(1)
//                .extracting(Exemplar::getId, Exemplar::getInventoryNumber, Exemplar::getCondition, Exemplar::getBorrowable)
//                .containsExactly(tuple(firstUpdated.getId(), firstUpdated.getInventoryNumber(),
//                        firstUpdated.getCondition(), firstUpdated.getBorrowable()));
//    }
//
//    @Test
//    void testUpdate_invalidId_notFoundResponseAndNothingChanged() throws Exception {
//        mockMvc.perform(post("/api/library/authors")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(firstAuthorToSave)));
//        mockMvc.perform(post("/api/library/books")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(firstBookToSave)));
//        mockMvc.perform(post("/api/library/exemplars/1")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(firstExemplarToSave)));
//
//        mockMvc.perform(put("/api/library/exemplars/2")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsString(firstToUpdate)))
//                .andExpect(status().isBadRequest());
//
//        assertThat(repository.findAll())
//                .hasSize(1)
//                .extracting(Exemplar::getId, Exemplar::getInventoryNumber, Exemplar::getCondition, Exemplar::getBorrowable)
//                .containsExactly(tuple(firstBorrowingSaved.getId(), firstBorrowingSaved.getInventoryNumber(),
//                        firstBorrowingSaved.getCondition(), firstBorrowingSaved.getBorrowable()));
//    }
//
//    @Test
//    void testDelete_exemplar123_exemplar123Deleted() throws Exception {
//        mockMvc.perform(post("/api/library/authors")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(firstAuthorToSave)));
//        mockMvc.perform(post("/api/library/books")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(firstBookToSave)));
//        mockMvc.perform(post("/api/library/exemplars/1")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(firstExemplarToSave)));
//        mockMvc.perform(delete("/api/library/exemplars/1"))
//                .andExpect(status().isOk());
//
//        assertThat(repository.findAll()).isEmpty();
//        mockMvc.perform(delete("/api/library/exemplars/1"))
//                .andExpect(status().isBadRequest());
//    }

    private void initBook() {
        firstAuthorToSave = new AuthorCreateUpdateCommand();
        firstAuthorToSave.setName("Emma Writer");
        firstBookToSave = new BookCreateCommand();
        firstBookToSave.setTitle("Tom's Adventure");
        firstBookToSave.setNumberOfPages(123);
        firstBookToSave.setPublisher("Johnson's");
        firstBookToSave.setPublishingYear(2000);
        firstBookToSave.setISBN("6245874512");
        firstBookToSave.setGenre(Genre.CHILDREN);
        firstBookToSave.setAuthorId(1);
    }


    private void initFirstExemplar() {
        firstExemplarToSave = new ExemplarCreateCommand();
        firstExemplarToSave.setInventoryNumber(123);
        firstExemplarToSave.setCondition(Condition.GOOD);
        firstExemplarToSave.setBorrowable(true);
    }

    private void initSecondExemplar() {
        secondExemplarToSave = new ExemplarCreateCommand();
        secondExemplarToSave.setInventoryNumber(212);
        secondExemplarToSave.setCondition(Condition.NEW);
        secondExemplarToSave.setBorrowable(false);
    }

    private void initFirstUser() {
        firstUserToSave = new UserCreateCommand();
        firstUserToSave.setName("Tom Johnson");
        firstUserToSave.setAddress("Principal street, no. 6");
        firstUserToSave.setEmail("tom.johnson@gmail.com");
        firstUserToSave.setPhoneNumber("215681152");
    }

    private void initFirstBorrowing() {
        firstBorrowingSaved = new BorrowingInfo();
        firstBorrowingSaved.setId(1);
        firstBorrowingSaved.setExemplar(
                new ExemplarInfoNoBorrowings(1, 123, Condition.GOOD, false,
                        new BookInfo(1, "6245874512", "Tom's Adventure",
                                new AuthorInfo(1, "Emma Writer"),
                                123, "Johnson's", 2000, Genre.CHILDREN)));
        firstBorrowingSaved.setUser(new UserInfo(1, "Tom Johnson", "Principal street, no. 6",
                "tom.johnson@gmail.com", "215681152"));
        LocalDate fromDate = now();
        firstBorrowingSaved.setFromDate(fromDate);
        LocalDate toDate = fromDate.plusDays(20);
        firstBorrowingSaved.setToDate(toDate);
        firstBorrowingSaved.setActive(true);
    }

    private int[] transformDate(LocalDate fromDate) {
        int[] fromD = {fromDate.getYear(), fromDate.getMonthValue(), fromDate.getDayOfMonth()};
        return fromD;
    }

//    private void initToUpdate() {
//        firstToUpdate = new ExemplarCreateCommand();
//        firstToUpdate.setInventoryNumber(333);
//        firstToUpdate.setCondition(Condition.USED);
//        firstToUpdate.setBorrowable(true);
//        firstUpdated = new ExemplarInfo();
//        firstUpdated.setId(1);
//        firstUpdated.setInventoryNumber(333);
//        firstUpdated.setCondition(Condition.USED);
//        firstUpdated.setBorrowable(true);
//    }
}