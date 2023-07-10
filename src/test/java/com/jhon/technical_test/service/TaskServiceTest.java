package com.jhon.technical_test.service;

import com.jhon.technical_test.model.Priority;
import com.jhon.technical_test.model.Task;
import com.jhon.technical_test.repository.ITaskRepository;
import com.jhon.technical_test.repository.TaskFilterRepository;
import jakarta.validation.ConstraintViolationException;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;


    @Mock
    private ITaskRepository taskRepository;

    @Mock
    private TaskFilterRepository taskFilterRepository;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Task task;

    private List<Task> taskList;

    @BeforeEach
    void setUp() throws ParseException {
        taskList = new ArrayList<>();
        task = new Task("Id1", "title 1", "description 1", new Date(), Priority.HIGH,new Date(),true,"Jhon");
        taskList.add(task);
        taskList.add(new Task("Id2", "title 2", "description 2", new Date(), Priority.LOW,new Date(),true,"Jhon"));
        taskList.add(new Task("Id3", "title 3", "description 3", new Date(), Priority.MEDIUM,new Date(),true,"Jhon"));
        taskList.add(new Task("Id4", "title 4", "description 4", new Date(), Priority.HIGH,new Date(),true,"Cristian"));
        taskList.add(new Task("Id5", "title 5", "description 5", new Date(), Priority.LOW,new Date(),true,"Cristian"));
        taskList.add(new Task("Id6", "title 5", "description 6", new Date(), Priority.LOW,new Date(),true,"Maria"));
    }

    @Test
    void shouldGetAllTasks() {
        when(taskRepository.findAll()).thenReturn(taskList);
        assertEquals(6, taskService.findAll().size());
        verify(taskRepository, times(1)).findAll();
        assertNotNull(taskList);
    }

    @Test
    void shouldGetTaskById() throws ParseException {
        when(taskRepository.findById("Id1")).thenReturn(java.util.Optional.of(task));
        assertEquals("Id1", taskService.findById("Id1").get().getId());
        verify(taskRepository, times(1)).findById("Id1");
        assertNotNull(task);
    }

    @Test
    void shouldUpdateTask() {
        String taskId = "123";
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Old Title");
        existingTask.setDescription("Old Description");
        existingTask.setCreationDate(new Date());
        existingTask.setPriority(Priority.LOW);

        Task updatedTask = new Task();
        updatedTask.setId(taskId);
        updatedTask.setTitle("Updated Title");
        updatedTask.setDescription("Updated Description");
        updatedTask.setCreationDate(new Date());
        updatedTask.setPriority(Priority.HIGH);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        Optional<Task> result = taskService.updateTask(taskId, updatedTask);

        assertTrue(result.isPresent());
        assertEquals(updatedTask.getTitle(), result.get().getTitle());
        assertEquals(updatedTask.getDescription(), result.get().getDescription());
        assertEquals(updatedTask.getPriority(), result.get().getPriority());

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void shouldDeleteTask() {
        String taskId = "ID1Deleted";
        Task existingTask = new Task(taskId, "Title Task for delete", "Description task for delete", new Date());
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        boolean result = taskService.deleteTask(taskId);

        assertTrue(result);
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).delete(existingTask);
    }


    @Test
    void testFindTaskByFilter() {
        Optional<Boolean> completed = Optional.of(true);
        Optional<String> assignedUser = Optional.of("Jhon");
        Optional<String> title = Optional.of("title");

        List<Task> expectedTasks = new ArrayList<>();
        expectedTasks.add(new Task("Id1", "title 1", "description 1", new Date(), Priority.HIGH,new Date(),true,"Jhon"));
        expectedTasks.add(new Task("Id3", "title 3", "description 3", new Date(), Priority.MEDIUM,new Date(),true,"Jhon"));

        when(taskFilterRepository.findTaskByFilter(completed, assignedUser, title)).thenReturn(expectedTasks);
        List<Task> result = taskService.findTaskByFilter(completed, assignedUser, title);
        assertEquals(expectedTasks, result);
        verify(taskFilterRepository, times(1)).findTaskByFilter(completed, assignedUser, title);
    }

}