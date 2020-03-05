package com.service.taskservice.controller;

import com.service.taskservice.model.Task;
import com.service.taskservice.repository.TaskRepository;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.logging.Logger;

@RestController
@RequestMapping("/")
public class TaskController {
    Logger logger = java.util.logging.Logger.getLogger(TaskController.class.getName());

    private final Environment env;
    private final TaskRepository taskRepository;

    public TaskController(Environment env, TaskRepository taskRepository) {
        this.env = env;
        this.taskRepository = taskRepository;
    }

    // Just some sort of info about service
    @RequestMapping("/")
    public String home(){
        String home = "Task-Service running at port: " + env.getProperty("local.server.port");
        logger.info(home);
        return home;
    }

    // @todo Need to have 2 same methods: for feirn & rest
    @GetMapping(path = "/show")
    public Flux<Task> getAllTasksList(){
        logger.info("Getting all of the tasks from DB ");
        return taskRepository.findAll();
    }

    @PostMapping(path = "/create")
    public Mono<Task> createTask(@Valid @RequestBody Task task){
        logger.info("creating new task with id: "+ task.getId());
        return taskRepository.save(task);
    }

    @GetMapping(path = "/get/{id}")
    public Mono<ResponseEntity<Task>> getTaskById(@PathVariable String id){
        return taskRepository.findById(id) // We find the Task
                .map(saveTask -> ResponseEntity.ok(saveTask)) //then call map on this to wrap it in a ReponseEntity
                .defaultIfEmpty(ResponseEntity.notFound().build()); // if ResponceEntity is empty we call default to build it
    }

    @PutMapping(path = "/update/{id}")
    public Mono<ResponseEntity<Task>> updateTask(@PathVariable String id, @Valid @RequestBody Task task){
        return taskRepository.findById(id) // call repository to find task
                .flatMap(existingTask ->{ //then call flatmap to update existingTask to Task
                    existingTask.setDescription(task.getDescription());
                    existingTask.setDate(task.getDate());
                    existingTask.setTitle(task.getTitle());
                    existingTask.setSubjectId(task.getSubjectId());
                    return taskRepository.save(existingTask);
                })
                .map(updateTask -> new ResponseEntity<>(updateTask, HttpStatus.OK)) //then save it to database and wrap in ResponceEntity
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/delete/{id}")
    public Mono<ResponseEntity<Void>> deleteTask(@PathVariable String id){
        return taskRepository.findById(id) //call repository to find task
                .flatMap(existingTask ->
                    taskRepository.delete(existingTask) // deleting tas, and returning 200 OK to show that delete was successful
                            .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND)); //or return 404 Not Found to say that Task was not found
    }

    @DeleteMapping(path = "delete/list")
    public Mono<ResponseEntity<Void>> deleteTaskList(@Valid @RequestBody Flux<Task> tasks){
        return tasks
                .flatMap(task -> taskRepository.delete(task))
                    .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @GetMapping("/subject/{id}")
    public Flux<Task> getTasksBySubjectId(@Valid @PathVariable String id){
        return taskRepository.findAll(id);
    }

    // @todo Delete all
    // @todo Get all as Server sent events
    // @todo Get default value every second
    // @todo Get all from database, one record per second
    // @todo Examples of handling exceptions (Duplicate id or something..)
    // @todo NotFoundException
}
