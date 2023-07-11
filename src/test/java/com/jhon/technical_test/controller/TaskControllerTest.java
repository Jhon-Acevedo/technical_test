package com.jhon.technical_test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhon.technical_test.model.Priority;
import com.jhon.technical_test.model.Task;
import com.jhon.technical_test.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;


    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Test add new task successfully with all fields")
    void testAddTask() throws Exception {
        Task task = new Task("Id1", "title 1", "description 1", new Date(), Priority.HIGH, new Date(), true, "Jhon");
        when(taskService.addTask(task)).thenReturn(task);
        ResultActions response = mockMvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(task)));
        response.andExpect(status().isCreated()).andDo(print());
    }

    @Test
    @DisplayName("Test add new task successfully with required fields")
    void testAddTaskWithFieldRequired() throws Exception {
        Task task = new Task("Id1", "title 1", "description 1", new Date());
        when(taskService.addTask(task)).thenReturn(task);
        ResultActions response = mockMvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(task)));
        response.andExpect(status().isCreated()).andDo(print());
    }

    @Test
    @DisplayName("Test add new Task with title empty and return bad request")
    void testAddTaskWithoutTitle() throws Exception {
        Task task = new Task("Id1", "", "description 1", new Date());
        when(taskService.addTask(task)).thenReturn(task);
        ResultActions response = mockMvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(task)));
        response.andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    @DisplayName("Test add new Task with description empty and return bad request")
    void testAddTaskWithoutDescription() throws Exception {
        Task task = new Task("Id1", "title 1", "", new Date());
        when(taskService.addTask(task)).thenReturn(task);
        ResultActions response = mockMvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(task)));
        response.andExpect(status().isBadRequest()).andDo(print());
    }


    @Test
    @DisplayName("Test get all tasks successfully and verify size of list is 2")
    void getAllTasks() throws Exception {
        List<Task> listTask = new ArrayList<>();
        listTask.add(new Task("Id1", "title 1", "description 1", new Date(), Priority.HIGH, new Date(), true, "Jhon"));
        listTask.add(new Task("Id2", "title 2", "description 2", new Date(), Priority.LOW, new Date(), true, "Jhon"));
        given(taskService.findAll()).willReturn(listTask);
        ResultActions response = mockMvc.perform(get("/api/tasks"));
        response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.size()", is(listTask.size())));
    }

    @Test
    @DisplayName("Test get Task by id successfully")
    void getTaskById() throws Exception {
        Task task = new Task("Id1", "title 1", "description 1", new Date(), Priority.HIGH, new Date(), true, "Jhon");
        when(taskService.findById("Id1")).thenReturn(java.util.Optional.of(task));
        ResultActions response = mockMvc.perform(get("/api/tasks/Id1"));
        // Verify response is equal to task object created
        response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.id", is(task.getId()))).andExpect(jsonPath("$.title", is(task.getTitle()))).andExpect(jsonPath("$.description", is(task.getDescription()))).andExpect(jsonPath("$.priority", is(task.getPriority().toString())));
    }

    @Test
    @DisplayName("Test get Task by id not found")
    void getTaskByIdNotFound() throws Exception {
        when(taskService.findById("Id1")).thenReturn(Optional.empty());
        ResultActions response = mockMvc.perform(get("/api/tasks/Id1"));
        response.andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    @DisplayName("Test delete Task by id successfully")
    void deleteTaskById() throws Exception {
        Task task = new Task("IdDelete", "title 1", "description 1", new Date());
        when(taskService.deleteTask("IdDelete")).thenReturn(true);

        mockMvc.perform(delete("/api/tasks/{id}", task.getId())).andExpect(status().isNoContent()).andDo(print());
//        Verify that the method deleteTask was called only once
        verify(taskService, times(1)).deleteTask(task.getId());
    }

    @Test
    @DisplayName("Test delete Task by id not found")
    void deleteTaskByIdNotFound() throws Exception {
        Task task = new Task("IdDelete", "title 1", "description 1", new Date());
        when(taskService.deleteTask("IdDelete")).thenReturn(false);
        mockMvc.perform(delete("/api/tasks/{id}", task.getId())).andExpect(status().isNotFound()).andDo(print());
        verify(taskService, times(1)).deleteTask(task.getId());
    }

    @Test
    @DisplayName("Test update title, description field task successfully")
    void testUpdateTitleTask() throws Exception {
        Task taskSaved = new Task("IdTaskSave", "title 1", "description 1", new Date());
        Task taskUpdate = new Task("IdTaskSave", "New Title Update", "Description update ", new Date());
        when(taskService.updateTask(anyString(), any(Task.class))).thenReturn(Optional.of(taskUpdate));
        ResultActions response = mockMvc.perform(put("/api/tasks/{id}", taskSaved.getId()).contentType(MediaType.APPLICATION_JSON).content("{\"title\": \"New Title Update\", \"description\": \"Description update \"}"));
        response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.title", is(taskUpdate.getTitle()))).andExpect(jsonPath("$.description", is(taskUpdate.getDescription())));
    }

    @Test
    @DisplayName("Test get all task by high priority successfully")
    void getAllTaskByHighPriority() throws Exception {
        List<Task> listTask = new ArrayList<>();
        listTask.add(new Task("Id1", "title 1", "description 1", new Date(), Priority.HIGH));
        listTask.add(new Task("Id2", "title 2", "description 2", new Date(), Priority.HIGH));
        listTask.add(new Task("Id3", "title 3", "description 3", new Date(), Priority.MEDIUM));
        listTask.add(new Task("Id4", "title 4", "description 4", new Date(), Priority.LOW));
        listTask.add(new Task("Id5", "title 5", "description 5", new Date()));
        given(taskService.findAllByHighPriority()).willReturn(listTask);
        ResultActions response = mockMvc.perform(get("/api/tasks/orderByPriority"));
        response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.size()", is(listTask.size())));
    }

}