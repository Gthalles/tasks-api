package com.thallesgarbelotti.todo_list.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean finished;

    protected Task() {}

    public Task(String description) {
        setDescription(description);
        this.finished = false;
    }

    public Long getId() {
        return this.id;
    }

    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be blank");
        }

        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void markAsFinished() {
        this.finished = true;
    }

    public void markAsPending() {
        this.finished = false;
    }

    public boolean isFinished() {
        return this.finished;
    }
}
