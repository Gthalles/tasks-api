package com.thallesgarbelotti.todo_list.dto;

public record UpdateTaskDTO(
    String description,
    Boolean finished
) {};
