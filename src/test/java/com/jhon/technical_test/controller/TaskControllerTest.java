package com.jhon.technical_test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhon.technical_test.model.Task;
import com.jhon.technical_test.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private Task task;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    public void init() throws ParseException {

        task = new Task();
        task.setId("task1");
        task.setTitle("title");
        task.setDescription("description");
        Date date = dateFormat.parse("2021-10-10");
        task.setCreationDate(date);
    }

    @Test
    void addTask() throws ParseException {
        // Crear una lista de tareas de muestra
        List<Task> taskList = new ArrayList<>();
        Date dateTask1;
        Date dateTask2;
        dateTask1 = dateFormat.parse("2021-10-10");
        dateTask2 = dateFormat.parse("2021-10-10");
    }

    @Test
    void findAll() {
        Task task = new Task();
    }

    @Test
    void findById() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void findTaskByFilter() {
    }

    @Test
    void findAllByOrderByCreationDateDesc() {
    }

    @Test
    void findAllByPriority() {
    }
}