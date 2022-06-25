package hu.progmasters.library.integration_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import hu.progmasters.library.domain.Author;
import hu.progmasters.library.dto.AuthorCreateUpdateCommand;
import hu.progmasters.library.dto.AuthorInfo;
import hu.progmasters.library.repository.AuthorRepository;
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
class AuthorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AuthorRepository repository;

    @Autowired
    ModelMapper modelMapper;


    private final ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();

    private AuthorCreateUpdateCommand firstToSave;
    private AuthorInfo firstSaved;
    private AuthorCreateUpdateCommand secondToSave;
    private AuthorInfo secondSaved;
    private AuthorCreateUpdateCommand thirdToSave;
    private AuthorInfo thirdSaved;
    private AuthorCreateUpdateCommand firstToUpdate;
    private AuthorInfo firstUpdated;

    @BeforeEach
    void init() {
        initFirst();
        initSecond();
        initThird();
        initToUpdate();
    }

    @Test
    void testCreate_TomWriter_savedInfoReturnedAndTomWriterInTheList() throws Exception {
        mockMvc.perform(post("/api/library/authors")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstToSave)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(firstSaved)));

        assertThat(repository.findAll())
                .hasSize(1)
                .extracting(Author::getId, Author::getName)
                .containsExactly(tuple(firstSaved.getId(), firstSaved.getName()));

        mockMvc.perform(get("/api/library/authors"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(firstSaved))));
    }

    @Test
    void testCreate_threeAuthors_infosReturnedAndAllInTheList() throws Exception {
        mockMvc.perform(post("/api/library/authors")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstToSave)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(firstSaved)));

        mockMvc.perform(post("/api/library/authors")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(secondToSave)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(secondSaved)));

        mockMvc.perform(post("/api/library/authors")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(thirdToSave)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(thirdToSave)));

        assertThat(repository.findAll())
                .hasSize(3)
                .extracting(Author::getId, Author::getName)
                .contains(tuple(firstSaved.getId(), firstSaved.getName()))
                .contains(tuple(secondSaved.getId(), secondSaved.getName()))
                .contains(tuple(thirdSaved.getId(), thirdSaved.getName()));



        mockMvc.perform(get("/api/library/authors"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        List.of(firstSaved, secondSaved, thirdSaved)))
                );
    }

    @Test
    void testUpdate_TomWriter_updatedToUpdatedWriter() throws Exception {
        mockMvc.perform(post("/api/library/authors")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstToSave)));

        mockMvc.perform(put("/api/library/authors/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstToUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(firstUpdated)));

        assertThat(repository.findAll())
                .hasSize(1)
                .extracting(Author::getId, Author::getName)
                .contains(tuple(firstUpdated.getId(), firstUpdated.getName()));
    }

    @Test
    void testUpdate_invalidId_notFoundResponseAndNothingChanged() throws Exception {
        mockMvc.perform(post("/api/library/authors")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstToSave)));

        mockMvc.perform(put("/api/library/authors/2")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(firstToUpdate)))
                .andExpect(status().isBadRequest());

        assertThat(repository.findAll())
                .hasSize(1)
                .extracting(Author::getId, Author::getName)
                .containsExactly(tuple(firstSaved.getId(), firstSaved.getName()));
    }

    @Test
    void testDelete_TomWriter_TomWriterDeleted() throws Exception {
        mockMvc.perform(post("/api/library/authors")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstToSave)));

        mockMvc.perform(delete("/api/library/authors/1"))
                .andExpect(status().isOk());

        assertThat(repository.findAll()).isEmpty();
        mockMvc.perform(delete("/api/library/authors/1"))
                .andExpect(status().isBadRequest());
    }


    private void initFirst() {
        firstToSave = new AuthorCreateUpdateCommand();
        firstToSave.setName("Tom Writer");
        firstSaved = new AuthorInfo();
        firstSaved.setId(1);
        firstSaved.setName("Tom Writer");
    }

    private void initSecond() {
        secondToSave = new AuthorCreateUpdateCommand();
        secondToSave.setName("John Writer");
        secondSaved = new AuthorInfo();
        secondSaved.setId(2);
        secondSaved.setName("John Writer");
    }

    private void initThird() {
        thirdToSave = new AuthorCreateUpdateCommand();
        thirdToSave.setName("Emma Writer");
        thirdSaved = new AuthorInfo();
        thirdSaved.setId(3);
        thirdSaved.setName("Emma Writer");
    }

    private void initToUpdate() {
        firstToUpdate = new AuthorCreateUpdateCommand();
        firstToUpdate.setName("Updated Writer");
        firstUpdated = new AuthorInfo();
        firstUpdated.setId(1);
        firstUpdated.setName("Updated Writer");
    }
}