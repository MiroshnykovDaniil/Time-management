package com.service.taskservice.repository;

import com.service.taskservice.model.Task;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TaskRepository extends ReactiveMongoRepository<Task,String> {

    @Query("{'subjectId' : ?0}")
    Flux<Task> findAll(String subjectId);
}
