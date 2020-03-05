package com.service.taskservice.model;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Builder
@Document(collection = "notes")
public class Task {

    @Id
    private String id;

    @Size(max = 30)
    private String title;
    private String description;
    private Date date;

    private String subjectId;
}
