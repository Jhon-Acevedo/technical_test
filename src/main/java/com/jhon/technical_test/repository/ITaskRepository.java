package com.jhon.technical_test.repository;

import com.jhon.technical_test.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ITaskRepository extends MongoRepository<Task, String> {
    List<Task> findAllByOrderByCreationDateAsc();

    List<Task> findAllByOrderByPriorityDesc();
}