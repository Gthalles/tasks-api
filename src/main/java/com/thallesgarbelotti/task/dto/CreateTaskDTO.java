package com.thallesgarbelotti.todo_list.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTaskDTO(
    @NotBlank String description
) {};
