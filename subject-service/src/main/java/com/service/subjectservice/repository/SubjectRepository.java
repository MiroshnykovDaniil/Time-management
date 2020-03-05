package com.service.subjectservice.repository;

import com.service.subjectservice.model.Subject;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SubjectRepository extends ReactiveMongoRepository<Subject,String> {
}
