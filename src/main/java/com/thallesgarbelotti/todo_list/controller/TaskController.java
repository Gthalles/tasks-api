package com.thallesgarbelotti.todo_list.controller;

import com.thallesgarbelotti.todo_list.dto.CreateTaskDTO;
import com.thallesgarbelotti.todo_list.dto.ResponseTaskDTO;
import com.thallesgarbelotti.todo_list.dto.UpdateTaskDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.thallesgarbelotti.todo_list.entity.Task;
import com.thallesgarbelotti.todo_list.service.TaskService;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService service;

    public TaskController(TaskService taskService) {
        this.service = taskService;
    }

    @PostMapping
    ResponseEntity<ResponseTaskDTO> create(@RequestBody @Valid CreateTaskDTO dto) {
        var newTask = new Task(dto.description());
        var savedTask = this.service.create(newTask);

        var response = new ResponseTaskDTO(
                savedTask.getId(),
                savedTask.getDescription(),
                savedTask.isFinished()
        );

        return ResponseEntity
                .created(URI.create("/tasks/" + savedTask.getId()))
                .body(response);
    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseTaskDTO> listById(@PathVariable Long id) {
        try {
            var selectedTask = this.service.listById(id);

            var response = new ResponseTaskDTO(
                    selectedTask.getId(),
                    selectedTask.getDescription(),
                    selectedTask.isFinished()
            );

            return ResponseEntity.ok(response);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    ResponseEntity<List<ResponseTaskDTO>> list() {
        var tasks = this.service.list();

        var response = tasks.stream()
                .map(task -> new ResponseTaskDTO(
                        task.getId(),
                        task.getDescription(),
                        task.isFinished()
                ))
                .toList();

        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{id}")
    ResponseEntity<ResponseTaskDTO> update(@PathVariable Long id, @RequestBody @Valid UpdateTaskDTO dto) {
        try {
            var savedTask = this.service.update(id, dto);

            var response = new ResponseTaskDTO(
                    savedTask.getId(),
                    savedTask.getDescription(),
                    savedTask.isFinished()
            );

            return ResponseEntity.ok(response);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            this.service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
