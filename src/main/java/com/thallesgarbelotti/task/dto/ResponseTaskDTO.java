package com.thallesgarbelotti.todo_list.dto;

public record ResponseTaskDTO(
        Long id,
        String description,
        boolean finished
) {};
