package com.jhon.technical_test.controller;

import com.jhon.technical_test.model.Task;
import com.jhon.technical_test.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new task")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Task created"), @ApiResponse(responseCode = "400", description = "Bad Request: Please enter the necessary fields.", content = @Content), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    public ResponseEntity<Task> addTask(@Valid @RequestBody Task task) {
        if (task.getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(taskService.addTask(task), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all tasks")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Found all tasks"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    public ResponseEntity<List<Task>> findAll() {
        return new ResponseEntity<>(taskService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by Task ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Found task by id"), @ApiResponse(responseCode = "404", description = "Task not found", content = @Content), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    public ResponseEntity<Task> findById(@Parameter(description = "Task id", required = true, example = "idTask001") @PathVariable String id) {
        return taskService.findById(id).map(task -> new ResponseEntity<>(task, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update task by id")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Task updated"), @ApiResponse(responseCode = "404", description = "Task not found", content = @Content), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    public ResponseEntity<Task> updateTask(@PathVariable String id, @RequestBody Task task) {
        Optional<Task> taskOptional = taskService.updateTask(id, task);
        return taskOptional.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task by id")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Task deleted"), @ApiResponse(responseCode = "404", description = "Task not found", content = @Content), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        return taskService.deleteTask(id) ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/filter")
    @Operation(summary = "Get task by filter")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Found task by filter"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    public ResponseEntity<List<Task>> findTaskByFilter(@RequestParam(required = false) Boolean completed, @RequestParam(required = false) String assignedUser, @RequestParam(required = false) String title) {
        return new ResponseEntity<>(taskService.findTaskByFilter(Optional.ofNullable(completed), Optional.ofNullable(assignedUser), Optional.ofNullable(title)), HttpStatus.OK);
    }

    @GetMapping("/orderByCreationDateDesc")
    @Operation(summary = "Get all tasks ordered by creation date")
    @ApiResponse(responseCode = "200", description = "Found all tasks ordered by creation date")
    public ResponseEntity<List<Task>> findAllByOrderByCreationDateDesc() {
        return new ResponseEntity<>(taskService.findAllByOrderByCreationDateDesc(), HttpStatus.OK);
    }

    @GetMapping("/orderByPriority")
    @Operation(summary = "Get all tasks ordered by priority")
    @ApiResponse(responseCode = "200", description = "Found all tasks ordered by priority")
    public ResponseEntity<List<Task>> findAllByPriority() {
        return new ResponseEntity<>(taskService.findAllByHighPriority(), HttpStatus.OK);
    }

}