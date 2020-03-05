package com.service.subjectservice.model;


import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document(collection = "subjects")
public class Subject {
    @Id
    private String id;
    private String title;
    private String description;
    private String color; // Not sure if we need color as String or some like Color.class

}
