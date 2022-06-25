package hu.progmasters.library.integration_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import hu.progmasters.library.domain.User;
import hu.progmasters.library.dto.UserCreateCommand;
import hu.progmasters.library.dto.UserInfo;
import hu.progmasters.library.repository.UserRepository;
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
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository repository;

    @Autowired
    ModelMapper modelMapper;


    private final ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();

    private UserCreateCommand firstToSave;
    private UserInfo firstSaved;
    private UserCreateCommand secondToSave;
    private UserInfo secondSaved;
    private UserCreateCommand firstToUpdate;
    private UserInfo firstUpdated;

    @BeforeEach
    void init() {
        initFirst();
        initSecond();
        initToUpdate();
    }

    @Test
    void testCreate_TomJohnson_savedInfoReturnedAndTomJohnsonInTheList() throws Exception {
        mockMvc.perform(post("/api/library/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(firstToSave)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(firstSaved)));

        assertThat(repository.findAll())
                .hasSize(1)
                .extracting(User::getId, User::getName, User::getAddress, User::getEmail, User::getPhoneNumber)
                .containsExactly(tuple(firstSaved.getId(), firstSaved.getName(), firstSaved.getAddress(),
                        firstSaved.getEmail(), firstSaved.getPhoneNumber() ));

        mockMvc.perform(get("/api/library/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(firstSaved))));
    }

    @Test
    void testCreate_twoUsers_infosReturnedAndAllInTheList() throws Exception {
        mockMvc.perform(post("/api/library/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(firstToSave)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(firstSaved)));


        mockMvc.perform(post("/api/library/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(secondToSave)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(secondSaved)));


        assertThat(repository.findAll())
                .hasSize(2)
                .extracting(User::getId, User::getName)
                .contains(tuple(firstSaved.getId(), firstSaved.getName()))
                .contains(tuple(secondSaved.getId(), secondSaved.getName()));

        assertThat(repository.findById(2).get().getName())
                .isEqualTo("Tom Connor");

        mockMvc.perform(get("/api/library/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        List.of(firstSaved, secondSaved)))
                );

        mockMvc.perform(get("/api/library/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(firstSaved)));
    }

    @Test
    void testUpdate_TomJohnsonAddressPhone_updatedAddressPhone() throws Exception {
        mockMvc.perform(post("/api/library/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstToSave)));

        mockMvc.perform(put("/api/library/users/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(firstToUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(firstUpdated)));

        assertThat(repository.findAll())
                .hasSize(1)
                .extracting(User::getId, User::getName, User::getAddress, User::getEmail, User::getPhoneNumber)
                .contains(tuple(firstUpdated.getId(), firstUpdated.getName(), firstUpdated.getAddress(),
                        firstUpdated.getEmail(), firstUpdated.getPhoneNumber()));
    }

    @Test
    void testUpdate_invalidId_notFoundResponseAndNothingChanged() throws Exception {
        mockMvc.perform(post("/api/library/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstToSave)));

        mockMvc.perform(put("/api/library/users/2")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(firstToUpdate)))
                .andExpect(status().isBadRequest());

        assertThat(repository.findAll())
                .hasSize(1)
                .extracting(User::getId, User::getName, User::getAddress, User::getEmail, User::getPhoneNumber)
                .containsExactly(tuple(firstSaved.getId(), firstSaved.getName(), firstSaved.getAddress(),
                        firstSaved.getEmail(), firstSaved.getPhoneNumber() ));
    }

    @Test
    void testDelete_TomJohnson_TomJohnsonDeleted() throws Exception {
        mockMvc.perform(post("/api/library/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstToSave)));
        mockMvc.perform(delete("/api/library/users/1"))
                .andExpect(status().isOk());

        assertThat(repository.findAll()).isEmpty();
        mockMvc.perform(delete("/api/subscription/1"))
                .andExpect(status().isNotFound());
    }

    private void initFirst() {
        firstToSave = new UserCreateCommand();
        firstToSave.setName("Tom Johnson");
        firstToSave.setAddress("Principal street, no. 6");
        firstToSave.setEmail("tom.johnson@gmail.com");
        firstToSave.setPhoneNumber("6245874512");
        firstSaved = new UserInfo();
        firstSaved.setId(1);
        firstSaved.setName("Tom Johnson");
        firstSaved.setAddress("Principal street, no. 6");
        firstSaved.setEmail("tom.johnson@gmail.com");
        firstSaved.setPhoneNumber("6245874512");
    }

    private void initSecond() {
        secondToSave = new UserCreateCommand();
        secondToSave.setName("Tom Connor");
        secondToSave.setAddress("Main street, no. 16");
        secondToSave.setEmail("tom.connor@gmail.com");
        secondToSave.setPhoneNumber("6243456557");
        secondSaved = new UserInfo();
        secondSaved.setId(2);
        secondSaved.setName("Tom Connor");
        secondSaved.setAddress("Main street, no. 16");
        secondSaved.setEmail("tom.connor@gmail.com");
        secondSaved.setPhoneNumber("6243456557");
    }

    private void initToUpdate() {
        firstToUpdate = new UserCreateCommand();
        firstToUpdate.setName("Tom Johnson");
        firstToUpdate.setAddress("New street, no. 8");
        firstToUpdate.setEmail("tom.johnson@gmail.com");
        firstToUpdate.setPhoneNumber("4545454545");
        firstUpdated = new UserInfo();
        firstUpdated.setId(1);
        firstUpdated.setName("Tom Johnson");
        firstUpdated.setAddress("New street, no. 8");
        firstUpdated.setEmail("tom.johnson@gmail.com");
        firstUpdated.setPhoneNumber("4545454545");

    }
}