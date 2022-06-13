package hu.progmasters.library.integration_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import hu.progmasters.library.domain.Book;
import hu.progmasters.library.domain.Genre;
import hu.progmasters.library.dto.AuthorCreateCommand;
import hu.progmasters.library.dto.AuthorInfo;
import hu.progmasters.library.dto.BookCreateCommand;
import hu.progmasters.library.dto.BookInfo;
import hu.progmasters.library.repository.BookRepository;
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
class BookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    BookRepository repository;

    @Autowired
    ModelMapper modelMapper;


    private final ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();

    private BookCreateCommand firstToSave;
    private BookInfo firstSaved;
    private BookCreateCommand secondToSave;
    private BookInfo secondSaved;
    private BookCreateCommand firstToUpdate;
    private BookInfo firstUpdated;
    private AuthorCreateCommand firstAuthorToSave;

    @BeforeEach
    void init() {
        initFirstAuthor();
        initFirst();
        initSecond();
        initToUpdate();
    }

    @Test
    void testSave_TomsAdventure_savedInfoReturnedAndTomsAdventureInTheList() throws Exception {
        mockMvc.perform(post("/api/library/authors")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstAuthorToSave)));
        mockMvc.perform(post("/api/library/books")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(firstToSave)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(firstSaved)));

        assertThat(repository.findAll(Genre.CHILDREN))
                .hasSize(1)
                .extracting(Book::getId, Book::getTitle, Book::getNumberOfPages, Book::getPublisher, Book::getISBN)
                .containsExactly(tuple(firstSaved.getId(), firstSaved.getTitle(), firstSaved.getNumberOfPages(),
                        firstSaved.getPublisher(), firstSaved.getISBN()));

        mockMvc.perform(get("/api/library/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(firstSaved))));
    }

    @Test
    void testSave_twoBooks_infosReturnedAndAllInTheList() throws Exception {
        mockMvc.perform(post("/api/library/authors")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstAuthorToSave)));
        mockMvc.perform(post("/api/library/books")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(firstToSave)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(firstSaved)));


        mockMvc.perform(post("/api/library/books")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(secondToSave)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(secondSaved)));


        assertThat(repository.findAll(Genre.CHILDREN))
                .hasSize(2)
                .extracting(Book::getId, Book::getTitle)
                .contains(tuple(firstSaved.getId(), firstSaved.getTitle()))
                .contains(tuple(secondSaved.getId(), secondSaved.getTitle()));

        assertThat(repository.findById(2).get().getTitle())
                .isEqualTo("Tom Connor");

        mockMvc.perform(get("/api/library/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        List.of(firstSaved, secondSaved)))
                );

        mockMvc.perform(get("/api/library/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(firstSaved)));
    }

    @Test
    void testUpdate_TomsAdventure_updatedTomsNewAdventure() throws Exception {
        mockMvc.perform(post("/api/library/authors")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstAuthorToSave)));
        mockMvc.perform(post("/api/library/books")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstToSave)));

        mockMvc.perform(put("/api/library/books/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(firstToUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(firstUpdated)));

        assertThat(repository.findAll(Genre.CHILDREN))
                .hasSize(1)
                .extracting(Book::getId, Book::getTitle, Book::getNumberOfPages, Book::getPublisher, Book::getISBN)
                .contains(tuple(firstUpdated.getId(), firstUpdated.getTitle(), firstUpdated.getNumberOfPages(),
                        firstUpdated.getPublisher(), firstUpdated.getISBN()));
    }

    @Test
    void testUpdate_invalidId_notFoundResponseAndNothingChanged() throws Exception {
        mockMvc.perform(post("/api/library/authors")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstAuthorToSave)));
        mockMvc.perform(post("/api/library/books")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstToSave)));

        mockMvc.perform(put("/api/library/books/2")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(firstToUpdate)))
                .andExpect(status().isBadRequest());

        assertThat(repository.findAll(Genre.CHILDREN))
                .hasSize(1)
                .extracting(Book::getId, Book::getTitle, Book::getNumberOfPages, Book::getPublisher, Book::getISBN)
                .containsExactly(tuple(firstSaved.getId(), firstSaved.getTitle(), firstSaved.getNumberOfPages(),
                        firstSaved.getPublisher(), firstSaved.getISBN()));
    }

//    @Test
//    void testDelete_TomsAdventure_TomsAdventureMarkedDeleted() throws Exception {
//        mockMvc.perform(post("/api/library/authors")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(firstAuthorToSave)));
//        mockMvc.perform(post("/api/library/books")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(firstToSave)));
//        mockMvc.perform(delete("/api/library/books/1"))
//                .andExpect(status().isOk());
//
//        assertThat(repository.findAll(Genre.CHILDREN)).isEmpty();
//        mockMvc.perform(delete("/api/library/books/1"))
//                .andExpect(status().isNotFound());
//    }

    private void initFirstAuthor() {
        firstAuthorToSave = new AuthorCreateCommand();
        firstAuthorToSave.setName("Emma Writer");
    }

    private void initFirst() {
        firstToSave = new BookCreateCommand();
        firstToSave.setTitle("Tom's Adventure");
        firstToSave.setNumberOfPages(123);
        firstToSave.setPublisher("Johnson's");
        firstToSave.setPublishingYear(2000);
        firstToSave.setISBN("6245874512");
        firstToSave.setGenre(Genre.CHILDREN);
        firstToSave.setAuthorId(1);
        firstSaved = new BookInfo();
        firstSaved.setId(1);
        firstSaved.setTitle("Tom's Adventure");
        firstSaved.setNumberOfPages(123);
        firstSaved.setPublisher("Johnson's");
        firstSaved.setPublishingYear(2000);
        firstSaved.setISBN("6245874512");
        firstSaved.setGenre(Genre.CHILDREN);
        firstSaved.setAuthor(new AuthorInfo(1, "Emma Writer"));
    }

    private void initSecond() {
        secondToSave = new BookCreateCommand();
        secondToSave.setTitle("Tom Connor");
        secondToSave.setNumberOfPages(212);
        secondToSave.setPublisher("The Best");
        secondToSave.setPublishingYear(2001);
        secondToSave.setISBN("6243456557");
        secondToSave.setGenre(Genre.CHILDREN);
        secondToSave.setAuthorId(1);
        secondSaved = new BookInfo();
        secondSaved.setId(2);
        secondSaved.setTitle("Tom Connor");
        secondSaved.setNumberOfPages(212);
        secondSaved.setPublisher("The Best");
        secondSaved.setPublishingYear(2001);
        secondSaved.setISBN("6243456557");
        secondSaved.setGenre(Genre.CHILDREN);
        secondSaved.setAuthor(new AuthorInfo(1, "Emma Writer"));
    }

    private void initToUpdate() {
        firstToUpdate = new BookCreateCommand();
        firstToUpdate.setTitle("Tom's New Adventure");
        firstToUpdate.setNumberOfPages(333);
        firstToUpdate.setPublisher("Johnson's");
        firstToUpdate.setPublishingYear(2000);
        firstToUpdate.setISBN("6245874512");
        firstToUpdate.setGenre(Genre.CHILDREN);
        firstToUpdate.setAuthorId(1);
        firstUpdated = new BookInfo();
        firstUpdated.setId(1);
        firstUpdated.setTitle("Tom's New Adventure");
        firstUpdated.setNumberOfPages(333);
        firstUpdated.setPublisher("Johnson's");
        firstUpdated.setPublishingYear(2000);
        firstUpdated.setISBN("6245874512");
        firstUpdated.setGenre(Genre.CHILDREN);
        firstUpdated.setAuthor(new AuthorInfo(1, "Emma Writer"));
    }
}