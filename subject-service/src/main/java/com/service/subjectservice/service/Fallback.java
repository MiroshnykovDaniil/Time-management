package com.service.subjectservice.service;

import com.service.noteservice.model.Task;

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
}
