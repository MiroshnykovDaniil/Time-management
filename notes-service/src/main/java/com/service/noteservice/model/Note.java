package com.service.noteservice.model;


import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collection = "notes")
public class Note {

    @Id
    private String id;

    private String title;
    private String description;
    private Date date;
}
