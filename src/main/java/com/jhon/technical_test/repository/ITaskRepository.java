package com.jhon.technical_test.repository;

import com.jhon.technical_test.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ITaskRepository extends MongoRepository<Task, String> {
    List<Task> findAllByOrderByCreationDateAsc();

    @Query("{'$sort': {'priority': -1}}")
    List<Task> findAllByOrOrderByPriority();

    List<Task> findAllByOrderByPriorityDesc();
}