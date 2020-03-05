package com.service.subjectservice.controller;


import com.service.taskservice.model.Task;
import com.service.subjectservice.service.ServiceFeignClient;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/")
public class SubjectController {

    Logger logger = Logger.getLogger(SubjectController.class.getName());

    private final Environment env;

    public SubjectController(Environment env) {
        this.env = env;
    }


    @RequestMapping("/")
    public String home(){
        String home = "Subjects-Service running at port: " + env.getProperty("local.server.port");
        logger.info(home);
        return home;
    }

    @GetMapping("/get/{id}")
    public List<Task> getSubjectTasksById(@PathVariable String id){
        logger.info("Calling Feign Client");
        return ServiceFeignClient.FeignHolder.create().getTasksListBySubjectId(id);
    }

}
