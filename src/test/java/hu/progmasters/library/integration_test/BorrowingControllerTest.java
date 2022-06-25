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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private ExemplarCreateUpdateCommand firstExemplarToSave;
    private BorrowingInfo firstBorrowingSaved;
    private BorrowingInfo secondBorrowingSaved;
    private BorrowingInfo borrowingUpdated;
    private BookCreateUpdateCommand firstBookToSave;
    private AuthorCreateUpdateCommand firstAuthorToSave;
    private UserCreateCommand firstUserToSave;

    @BeforeEach
    void init() {
        initBook();
        initFirstExemplar();
        initSecondExemplar();
        initFirstUser();
        initFirstBorrowing();
    }

    @Test
    void testCreate_exemplar123_savedInfoReturnedAndExemplar123InTheList() throws Exception {
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
        assertThat(repository.findAll(1, 1))
                .hasSize(1)
                .extracting(Borrowing::getId, Borrowing::getFromDate, Borrowing::getToDate, Borrowing::getActive)
                .containsExactly(tuple(firstBorrowingSaved.getId(), firstBorrowingSaved.getFromDate(),
                        firstBorrowingSaved.getToDate(), firstBorrowingSaved.getActive()));

        mockMvc.perform(get("/api/library/borrowings"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/library/borrowings/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete_borrowingExists_borrowingDeleted() throws Exception {
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
                .content(objectMapper.writeValueAsString("")));

        mockMvc.perform(delete("/api/library/borrowings/1"))
                .andExpect(status().isOk());

        assertThat(repository.findAll(null, null)).isEmpty();
        mockMvc.perform(delete("/api/library/borrowings/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testProlongation_borrowingNotExpired_borrowingExtended() throws Exception {
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
                .content(objectMapper.writeValueAsString("")));

        mockMvc.perform(put("/api/library/borrowings/extend/1"))
                .andExpect(status().isOk());

        assertThat(repository.findAll(1, 1))
                .hasSize(1)
                .extracting(Borrowing::getId, Borrowing::getFromDate, Borrowing::getToDate, Borrowing::getActive)
                .containsExactly(tuple(firstBorrowingSaved.getId(), firstBorrowingSaved.getFromDate(),
                        firstBorrowingSaved.getToDate().plusDays(10), firstBorrowingSaved.getActive()));

    }

    private void initBook() {
        firstAuthorToSave = new AuthorCreateUpdateCommand();
        firstAuthorToSave.setName("Emma Writer");
        firstBookToSave = new BookCreateUpdateCommand();
        firstBookToSave.setTitle("Tom's Adventure");
        firstBookToSave.setNumberOfPages(123);
        firstBookToSave.setPublisher("Johnson's");
        firstBookToSave.setPublishingYear(2000);
        firstBookToSave.setISBN("6245874512");
        firstBookToSave.setGenre(Genre.CHILDREN);
        firstBookToSave.setAuthorId(1);
    }


    private void initFirstExemplar() {
        firstExemplarToSave = new ExemplarCreateUpdateCommand();
        firstExemplarToSave.setInventoryNumber(123);
        firstExemplarToSave.setCondition(Condition.GOOD);
        firstExemplarToSave.setBorrowable(true);
    }

    private void initSecondExemplar() {
        ExemplarCreateUpdateCommand secondExemplarToSave = new ExemplarCreateUpdateCommand();
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

}