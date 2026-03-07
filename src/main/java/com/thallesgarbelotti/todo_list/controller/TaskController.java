package com.thallesgarbelotti.todo_list.controller;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.thallesgarbelotti.todo_list.entity.Task;
import com.thallesgarbelotti.todo_list.service.TaskService;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    void create(@RequestBody @Valid Task newTask) {
        this.service.create(newTask);
    }

    @GetMapping("/{id}")
    ResponseEntity<Task> listById(@PathVariable Long id) {
        try {
            var selectedTask = this.service.listById(id);
            return ResponseEntity.ok(selectedTask);
        } catch(NoSuchElementException err) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    List<Task> list() {
        return this.service.list();
    }

    @PatchMapping("/{id}")
    ResponseEntity<Task> update(@PathVariable Long id, @RequestBody @Valid Task updatedTask) {
        try {
            var savedTask = this.service.update(id, updatedTask);
            return ResponseEntity.ok(savedTask);
        } catch (NoSuchElementException err) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus
    ResponseEntity<List<Task>> delete(@PathVariable Long id) {
        try {
            var selectedTask = this.service.listById(id);
            if (selectedTask == null) return ResponseEntity.notFound().build();

           var updatedList = this.service.delete(id);
           return ResponseEntity.ok(updatedList);
        } catch (NoSuchElementException err) {
            return ResponseEntity.notFound().build();
        }
    }
}
