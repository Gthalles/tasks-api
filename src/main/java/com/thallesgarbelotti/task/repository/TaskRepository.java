package com.thallesgarbelotti.todo_list.repository;

import com.thallesgarbelotti.todo_list.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
