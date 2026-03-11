package com.thallesgarbelotti.todo_list.service;

import com.thallesgarbelotti.todo_list.entity.Task;
import com.thallesgarbelotti.todo_list.repository.TaskRepository;
import com.thallesgarbelotti.todo_list.dto.UpdateTaskDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService service;

    @Test
    void shouldCreateTask() {
        Task task = new Task("New task");
        when(repository.save(task)).thenReturn(task);

        Task result = service.create(task);

        assertSame(task, result);
        verify(repository).save(task);
    }

    @Test
    void shouldListTasksOrderedByDescription() {
        List<Task> tasks = List.of(new Task("A"), new Task("B"));
        when(repository.findAll(any(Sort.class))).thenReturn(tasks);

        List<Task> result = service.list();

        assertEquals(tasks, result);

        ArgumentCaptor<Sort> captor = ArgumentCaptor.forClass(Sort.class);
        verify(repository).findAll(captor.capture());
        Sort sortUsed = captor.getValue();
        assertNotNull(sortUsed.getOrderFor("description"));
        assertTrue(sortUsed.getOrderFor("description").isAscending());
    }

    @Test
    void shouldReturnTaskByIdWhenTaskExists() {
        Task task = new Task("New task");
        when(repository.findById(1L)).thenReturn(Optional.of(task));

        Task result = service.listById(1L);

        assertSame(task, result);
        verify(repository).findById(1L);
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenTaskDoesNotExist() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.listById(99L));
        verify(repository).findById(99L);
    }

    @Test
    void shouldUpdateTaskWhenTaskExists() {
        Task task = new Task("Old description");
        when(repository.findById(1L)).thenReturn(Optional.of(task));
        when(repository.save(task)).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateTaskDTO dto = new UpdateTaskDTO("Updated description", true);

        Task result = service.update(1L, dto);

        assertEquals("Updated description", result.getDescription());
        assertTrue(result.isFinished());
        verify(repository).findById(1L);
        verify(repository).save(task);
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenUpdatingTaskThatDoesNotExist() {
        UpdateTaskDTO dto = new UpdateTaskDTO("Updated description", true);
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.update(1L, dto));
        verify(repository).findById(1L);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldDeleteTaskById() {
        Task task = new Task("Task");
        when(repository.findById(5L)).thenReturn(Optional.of(task));
        doNothing().when(repository).deleteById(5L);

        service.delete(5L);

        verify(repository).findById(5L);
        verify(repository).deleteById(5L);
    }
}
