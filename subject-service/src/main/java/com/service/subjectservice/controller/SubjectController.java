package com.service.subjectservice.controller;


import com.service.subjectservice.model.Subject;
import com.service.subjectservice.repository.SubjectRepository;
import com.service.taskservice.model.Task;
import com.service.subjectservice.service.ServiceFeignClient;
import com.service.taskservice.repository.TaskRepository;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/")
public class SubjectController {

    Logger logger = Logger.getLogger(SubjectController.class.getName());

    private final Environment env;
    private final SubjectRepository subjectRepository;

    public SubjectController(Environment env, SubjectRepository subjectRepository) {
        this.env = env;
        this.subjectRepository = subjectRepository;
    }


    @RequestMapping("/")
    public String home(){
        String home = "Subjects-Service running at port: " + env.getProperty("local.server.port");
        logger.info(home);
        return home;
    }

    @GetMapping("/get/{id}/tasks")
    public List<Task> getSubjectTasksById(@PathVariable String id){
        logger.info("Calling Feign Client");
        return ServiceFeignClient.FeignHolder.create().getTasksListBySubjectId(id);
    }

    @GetMapping("/get/{id}")
    public Mono<ResponseEntity<Subject>> getSubjectById(@PathVariable String id){
        return subjectRepository.findById(id)
                .map(subject -> ResponseEntity.ok(subject))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public Mono<Subject> createSubjects(@Valid @RequestBody Subject subject){
        logger.info("Creating new subject");
        return subjectRepository.save(subject);
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<Subject>> updateSubject(@PathVariable String id, @Valid @RequestBody Subject subject){
        return subjectRepository.findById(id) //call repository to find subject by its id
                .flatMap(existingSubject ->{
                    existingSubject.setTitle(subject.getTitle());
                    existingSubject.setDescription(subject.getDescription());
                    existingSubject.setColor(subject.getColor());
                    return subjectRepository.save(existingSubject); //then update fields, save it
                })
                .map(updateSubject -> new ResponseEntity<>(updateSubject,HttpStatus.OK)) //and map result into ResponseEntity
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // or say that subject was not found
    }

    @DeleteMapping(path = "/delete/{id}")
    public Mono<ResponseEntity<Void>> deleteSubject(@PathVariable String id){
        return subjectRepository.findById(id) //call repository to find subject
                .flatMap(existingSubject ->
                        subjectRepository.delete(existingSubject) // deleting subject, and returning 200 OK to show that delete was successful
                                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND)); //or return 404 Not Found to say that subject was not found
    }

}
