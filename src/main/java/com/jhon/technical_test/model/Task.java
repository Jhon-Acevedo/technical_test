package com.jhon.technical_test.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "tasks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    private String id;

    @NotNull(message = "Title is mandatory")
    @NotBlank(message = "Title not be blank")
    private String title;

    @NotNull(message = "Description is mandatory")
    @NotBlank(message = "Description not be blank")
    private String description;


    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Priority is mandatory")
    private Date creationDate;

    private Priority priority = Priority.LOW;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expirationDate;
    private boolean completed;
    private String assignedTo;
}