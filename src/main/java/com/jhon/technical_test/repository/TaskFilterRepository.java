package com.jhon.technical_test.repository;

import com.jhon.technical_test.model.Task;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TaskFilterRepository {

    private final MongoTemplate mongoTemplate;

    public TaskFilterRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Task> findTaskByFilter(Optional<Boolean> completed, Optional<String> assignedUser, Optional<String> title) {
        Criteria criteria = new Criteria();
        completed.ifPresent(assignBoolean -> criteria.and("completed").is(assignBoolean));
        assignedUser.ifPresent(assignUser -> criteria.and("assignedTo").regex(assignUser, "i"));
        title.ifPresent(assignTitle -> criteria.and("title").regex(assignTitle, "i"));
        return mongoTemplate.find(Query.query(criteria), Task.class);
    }

}
