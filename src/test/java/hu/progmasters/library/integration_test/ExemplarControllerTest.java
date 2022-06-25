package hu.progmasters.library.integration_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import hu.progmasters.library.domain.Condition;
import hu.progmasters.library.domain.Exemplar;
import hu.progmasters.library.domain.Genre;
import hu.progmasters.library.dto.*;
import hu.progmasters.library.repository.ExemplarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ExemplarControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ExemplarRepository repository;

    @Autowired
    ModelMapper modelMapper;


    private final ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();

    private ExemplarCreateUpdateCommand firstToSave;
    private ExemplarInfo firstSaved;
    private ExemplarCreateUpdateCommand secondToSave;
    private ExemplarInfo secondSaved;
    private ExemplarCreateUpdateCommand firstToUpdate;
    private ExemplarInfo firstUpdated;
    private BookCreateUpdateCommand firstBookToSave;
    private AuthorCreateUpdateCommand firstAuthorToSave;

    @BeforeEach
    void init() {
        initFirstBook();
        initFirst();
        initSecond();
        initToUpdate();
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
                        .content(objectMapper.writeValueAsString(firstToSave)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(firstSaved)));

        assertThat(repository.findAll())
                .hasSize(1)
                .extracting(Exemplar::getId, Exemplar::getInventoryNumber, Exemplar::getCondition,
                        Exemplar::getBorrowable)
                .containsExactly(tuple(firstSaved.getId(), firstSaved.getInventoryNumber(),
                        firstSaved.getCondition(), firstSaved.getBorrowable()));

        mockMvc.perform(get("/api/library/exemplars"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(firstSaved))));
    }

    @Test
    void testCreate_twoExemplars_infosReturnedAndAllInTheList() throws Exception {
        mockMvc.perform(post("/api/library/authors")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstAuthorToSave)));
        mockMvc.perform(post("/api/library/books")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstBookToSave)));
        mockMvc.perform(post("/api/library/exemplars/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(firstToSave)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(firstSaved)));


        mockMvc.perform(post("/api/library/exemplars/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(secondToSave)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(secondSaved)));


        assertThat(repository.findAll())
                .hasSize(2)
                .extracting(Exemplar::getId, Exemplar::getInventoryNumber, Exemplar::getCondition,
                        Exemplar::getBorrowable)
                .contains(tuple(firstSaved.getId(), firstSaved.getInventoryNumber(),
                        firstSaved.getCondition(), firstSaved.getBorrowable()))
                .contains(tuple(secondSaved.getId(), secondSaved.getInventoryNumber(),
                        secondSaved.getCondition(), secondSaved.getBorrowable()));

        assertThat(repository.findById(2).get().getInventoryNumber())
                .isEqualTo(212);

        mockMvc.perform(get("/api/library/exemplars"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        List.of(firstSaved, secondSaved)))
                );

        mockMvc.perform(get("/api/library/exemplars/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(firstSaved)));
    }

    @Test
    void testUpdate_exemplar123GOOD_updated_exemplar333USED() throws Exception {
        mockMvc.perform(post("/api/library/authors")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstAuthorToSave)));
        mockMvc.perform(post("/api/library/books")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstBookToSave)));
        mockMvc.perform(post("/api/library/exemplars/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstToSave)));

        mockMvc.perform(put("/api/library/exemplars/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(firstToUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(firstUpdated)));

        assertThat(repository.findAll())
                .hasSize(1)
                .extracting(Exemplar::getId, Exemplar::getInventoryNumber, Exemplar::getCondition,
                        Exemplar::getBorrowable)
                .containsExactly(tuple(firstUpdated.getId(), firstUpdated.getInventoryNumber(),
                        firstUpdated.getCondition(), firstUpdated.getBorrowable()));
    }

    @Test
    void testUpdate_invalidId_notFoundResponseAndNothingChanged() throws Exception {
        mockMvc.perform(post("/api/library/authors")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstAuthorToSave)));
        mockMvc.perform(post("/api/library/books")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstBookToSave)));
        mockMvc.perform(post("/api/library/exemplars/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstToSave)));

        mockMvc.perform(put("/api/library/exemplars/2")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(firstToUpdate)))
                .andExpect(status().isBadRequest());

        assertThat(repository.findAll())
                .hasSize(1)
                .extracting(Exemplar::getId, Exemplar::getInventoryNumber, Exemplar::getCondition,
                        Exemplar::getBorrowable)
                .containsExactly(tuple(firstSaved.getId(), firstSaved.getInventoryNumber(),
                        firstSaved.getCondition(), firstSaved.getBorrowable()));
    }

    @Test
    void testDelete_exemplar123_exemplar123Deleted() throws Exception {
        mockMvc.perform(post("/api/library/authors")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstAuthorToSave)));
        mockMvc.perform(post("/api/library/books")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstBookToSave)));
        mockMvc.perform(post("/api/library/exemplars/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstToSave)));
        mockMvc.perform(delete("/api/library/exemplars/1"))
                .andExpect(status().isOk());

        assertThat(repository.findAll()).isEmpty();
        mockMvc.perform(delete("/api/library/exemplars/1"))
                .andExpect(status().isBadRequest());
    }

    private void initFirstBook() {
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


    private void initFirst() {
        firstToSave = new ExemplarCreateUpdateCommand();
        firstToSave.setInventoryNumber(123);
        firstToSave.setCondition(Condition.GOOD);
        firstToSave.setBorrowable(true);
        firstSaved = new ExemplarInfo();
        firstSaved.setId(1);
        firstSaved.setInventoryNumber(123);
        firstSaved.setCondition(Condition.GOOD);
        firstSaved.setBorrowable(true);
        firstSaved.setOfBook(new BookInfoMin(1, "Tom's Adventure"));
    }

    private void initSecond() {
        secondToSave = new ExemplarCreateUpdateCommand();
        secondToSave.setInventoryNumber(212);
        secondToSave.setCondition(Condition.NEW);
        secondToSave.setBorrowable(false);
        secondSaved = new ExemplarInfo();
        secondSaved.setId(2);
        secondSaved.setInventoryNumber(212);
        secondSaved.setCondition(Condition.NEW);
        secondSaved.setBorrowable(false);
        secondSaved.setOfBook(new BookInfoMin(1, "Tom's Adventure"));
    }

    private void initToUpdate() {
        firstToUpdate = new ExemplarCreateUpdateCommand();
        firstToUpdate.setInventoryNumber(333);
        firstToUpdate.setCondition(Condition.USED);
        firstToUpdate.setBorrowable(true);
        firstUpdated = new ExemplarInfo();
        firstUpdated.setId(1);
        firstUpdated.setInventoryNumber(333);
        firstUpdated.setCondition(Condition.USED);
        firstUpdated.setBorrowable(true);
        firstUpdated.setOfBook(new BookInfoMin(1, "Tom's Adventure"));
    }
}