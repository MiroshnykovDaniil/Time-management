package com.service.noteservice.controller;

import com.service.noteservice.model.Note;
import com.service.noteservice.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/")
public class NoteController {
    Logger logger = java.util.logging.Logger.getLogger(NoteController.class.getName());

    private final Environment env;
    private final NoteRepository noteRepository;

    public NoteController(Environment env, NoteRepository noteRepository) {
        this.env = env;
        this.noteRepository = noteRepository;
    }

    // Just some sort of info about service
    @RequestMapping("/")
    public String home(){
        String home = "Note-Service running at port: " + env.getProperty("local.server.port");
        logger.info(home);
        return home;
    }

    // @todo Need to have 2 same methods: for feirn & rest
    @GetMapping(path = "/show")
    public Flux<Note> getAllNotesList(){
        logger.info("Getting all of the notes from DB ");
        return noteRepository.findAll();
    }

    @PostMapping(path = "/create")
    public Mono<Note> createNote(@Valid @RequestBody Note note){
        logger.info("creating new note with id: "+ note.getId());
        return noteRepository.save(note);
    }

    @GetMapping(path = "/get/{id}")
    public Mono<ResponseEntity<Note>> getNoteById(@PathVariable String id){
        return noteRepository.findById(id) // We find the Note
                .map(saveNote -> ResponseEntity.ok(saveNote)) //then call map on this to wrap it in a ReponseEntity
                .defaultIfEmpty(ResponseEntity.notFound().build()); // if ResponceEntity is empty we call default to build it
    }

    @PutMapping(path = "/update/{id}")
    public Mono<ResponseEntity<Note>> updateNote(@PathVariable String id, @Valid @RequestBody Note note){
        return noteRepository.findById(id) // call repository to find note
                .flatMap(existingNote ->{ //then call flatmap to update existingNote to Note
                    existingNote.setDescription(note.getDescription());
                    existingNote.setDate(note.getDate());
                    existingNote.setTitle(note.getTitle());
                    return noteRepository.save(existingNote);
                })
                .map(updateNote -> new ResponseEntity<>(updateNote, HttpStatus.OK)) //then save it to database and wrap in ResponceEntity
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/delete/{id}")
    public Mono<ResponseEntity<Void>> deleteNote(@PathVariable String id){
        return noteRepository.findById(id) //call repository to find note
                .flatMap(existingNote ->
                    noteRepository.delete(existingNote) // deleting note, and returning 200 OK to show that delete was successful
                            .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND)); //or return 404 Not Found to say that Note was not found
    }

    // @todo Delete all
    // @todo Get all as Server sent events
    // @todo Get default value every second
    // @todo Get all from database, one record per second
    // @todo Examples of handling exceptions (Duplicate id or something..)
    // @todo NotFoundException
}
