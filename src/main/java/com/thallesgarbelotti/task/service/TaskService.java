package com.thallesgarbelotti.todo_list.service;
import com.thallesgarbelotti.todo_list.dto.UpdateTaskDTO;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.thallesgarbelotti.todo_list.entity.Task;
import com.thallesgarbelotti.todo_list.repository.TaskRepository;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository taskRepository) {
        this.repository = taskRepository;
    }

    public Task create(Task task) {
        return this.repository.save(task);
    }

    public List<Task> list() {
        Sort sort = Sort.by("description").ascending();
        return this.repository.findAll(sort);
    }

    public Task listById(Long id) {
        return this.repository.findById(id).get();
    }

    public Task update(Long id, UpdateTaskDTO dto) {
        Task selectedTask = this.listById(id);
        selectedTask.setDescription(dto.description());

        if (dto.finished()) {
            selectedTask.markAsFinished();
        } else {
            selectedTask.markAsPending();
        }

        return this.repository.save(selectedTask);
    }

    public void delete(Long id) {
        this.listById(id);
        this.repository.deleteById(id);
    }
}
