package exercise.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import org.instancio.Instancio;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.TaskRepository;
import exercise.model.Task;

// BEGIN
@SpringBootTest
@AutoConfigureMockMvc
// END
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring!");
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }


    // BEGIN
    @Test
    public void testShow() throws Exception {
        var task = Instancio.of(Task.class)
            .ignore(Select.field(Task::getId))
            .ignore(Select.all(LocalDate.class))
            .supply(Select.field(Task::getTitle), () -> faker.cat().name())
            .supply(Select.field(Task::getDescription), () -> faker.lorem().sentence())
            .create();
        taskRepository.save(task);

        var result = mockMvc.perform(get("/tasks/" + task.getId()))
            .andExpect(status().isOk())
            .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
            a -> a.node("title").isEqualTo(task.getTitle()),
            a -> a.node("description").isEqualTo(task.getDescription())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var allTasks = mockMvc.perform(get("/tasks"))
            .andReturn()
            .getResponse()
            .getContentAsString();

        assertThatJson(allTasks).isArray().isEmpty();

        var data = new HashMap<>();
        data.put("title", "Hello");
        data.put("description", "world");

        var request = post("/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(data));
        var result = mockMvc.perform(request)
            .andExpect(status().isCreated())
            .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
            a -> a.node("title").isEqualTo("Hello"),
            a -> a.node("description").isEqualTo("world")
        );
    }

    @Test
    public void testDelete() throws Exception {
        var task = Instancio.of(Task.class)
            .ignore(Select.field(Task::getId))
            .ignore(Select.all(LocalDate.class))
            .supply(Select.field(Task::getTitle), () -> faker.cat().name())
            .supply(Select.field(Task::getDescription), () -> faker.lorem().sentence())
            .create();
        taskRepository.save(task);

        var allTasks = mockMvc.perform(get("/tasks"))
            .andReturn()
            .getResponse()
            .getContentAsString();

        assertThatJson(allTasks).isArray().hasSize(1);

        mockMvc.perform(delete("/tasks/" + task.getId()))
            .andExpect(status().isOk());

        var allTasks2 = mockMvc.perform(get("/tasks"))
            .andReturn()
            .getResponse()
            .getContentAsString();

        assertThatJson(allTasks2).isArray().isEmpty();
    }
    // END
}
