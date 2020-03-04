package com.service.noteservice.repository;

import com.service.noteservice.model.Note;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends ReactiveMongoRepository<Note,String> {
}
