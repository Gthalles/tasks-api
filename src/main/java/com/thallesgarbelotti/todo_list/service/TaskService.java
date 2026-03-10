package com.thallesgarbelotti.todo_list.service;
import org.jspecify.annotations.NonNull;
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

    public Task update(Long id, @NonNull Task task) {
        Task selectedTask = this.listById(id);

        String newDescription = task.getDescription();

        selectedTask.setDescription(newDescription);

        this.repository.save(selectedTask);
        return selectedTask;
    }

    public void delete(Long id) {
        this.repository.deleteById(id);
    }
}
