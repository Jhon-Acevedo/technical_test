package com.jhon.technical_test.service;

import com.jhon.technical_test.model.Task;
import com.jhon.technical_test.repository.ITaskRepository;
import com.jhon.technical_test.repository.TaskFilterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final ITaskRepository taskRepository;
    private final TaskFilterRepository taskFilterRepository;

    public TaskService(ITaskRepository taskRepository, TaskFilterRepository taskFilterRepository) {
        this.taskRepository = taskRepository;
        this.taskFilterRepository = taskFilterRepository;
    }

    public Task addTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Optional<Task> findById(String id) {
        return taskRepository.findById(id);
    }

    public boolean deleteTask(String id) {
        return findById(id).map(task -> {
            taskRepository.delete(task);
            return true;
        }).orElse(false);
    }

    public List<Task> findTaskByFilter(Optional<Boolean> completed, Optional<String> assignedUser, Optional<String> title) {
        return taskFilterRepository.findTaskByFilter(completed, assignedUser, title);
    }

    public List<Task> findAllByOrderByCreationDateDesc() {
        return taskRepository.findAllByOrderByCreationDateAsc();
    }

    public List<Task> findAllByHighPriority() {
        return taskRepository.findAllByOrderByPriorityDesc();
    }

}
