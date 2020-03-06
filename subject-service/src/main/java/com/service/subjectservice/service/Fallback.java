package com.service.subjectservice.service;

import com.service.taskservice.model.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Fallback implements ServiceFeignClient {
    Logger logger = Logger.getLogger(Fallback.class.getName());

    @Override
    public List<Task> getTasksListBySubjectId(String id) {
        logger.info("Service is not available now");
        return new ArrayList<>();
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteTasksBySubjectId(String id) {
        logger.info("Service is not available now");
        return null;
    }
}
