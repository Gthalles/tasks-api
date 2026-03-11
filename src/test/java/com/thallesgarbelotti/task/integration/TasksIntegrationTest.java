package com.thallesgarbelotti.todo_list.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import com.thallesgarbelotti.todo_list.entity.Task;
import org.springframework.test.context.ActiveProfiles;
import com.thallesgarbelotti.todo_list.dto.CreateTaskDTO;
import com.thallesgarbelotti.todo_list.dto.ResponseTaskDTO;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import com.thallesgarbelotti.todo_list.repository.TaskRepository;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

import java.util.List;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TasksIntegrationTest {
	private WebTestClient webTestClient;
	private final WebApplicationContext context;

	@Autowired
	private TaskRepository repository;

	TasksIntegrationTest(WebApplicationContext context) {
        this.context = context;
	}

	@BeforeEach
	void setUpWebTestClient() {
		this.webTestClient = MockMvcWebTestClient.bindToApplicationContext(this.context).build();
	}

	@BeforeEach
	void resetDatabase() { this.repository.deleteAll(); }

	@Test
	void createTaskWhenInputIsValidShouldReturnCreated() {
		var input = new CreateTaskDTO("New task");

		webTestClient.post()
			.uri("/tasks")
			.bodyValue(input)
			.exchange()
			.expectStatus()
			.isCreated()
			.expectBody()
			.jsonPath("$.description").isEqualTo(input.description())
			.jsonPath("$.finished").isEqualTo(false);
	}

	@Test
	void createTaskWhenInputIsInvalidShouldReturnBadRequest() {
		webTestClient.post()
			.uri("/tasks")
			.header("Content-Type", "application/json")
			.bodyValue(
			"""
				{
					"description": ""
				}
			"""
			)
			.exchange()
			.expectStatus()
			.isBadRequest();
	}

	@Test
	void listTaskByIdWhenTaskExistsShouldReturnTaskAndOk() {
		var task = new Task("New task");
		Task savedTask = repository.save(task);

		var taskDTO = new ResponseTaskDTO(
				task.getId(),
				task.getDescription(),
				task.isFinished()
		);

		webTestClient
			.get()
			.uri("/tasks/{id}", savedTask.getId())
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.id").isEqualTo(taskDTO.id())
			.jsonPath("$.description").isEqualTo(taskDTO.description())
			.jsonPath("$.finished").isEqualTo(taskDTO.finished());
	}

	@Test
	void listTaskByIdWhenTaskNotExistsShouldReturnNotFound() {
		webTestClient
			.get()
			.uri("/tasks/{id}", 9999)
			.exchange()
			.expectStatus().isNotFound();
	}

	@Test
	void listTasksWhenTasksExistsShouldReturnTasksListAndOk() {
		var firstTask = new Task("First task");
		var secondTask = new Task("Second task");

		repository.saveAll(List.of(firstTask, secondTask));

		webTestClient
			.get()
			.uri("/tasks")
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$").isArray()
			.jsonPath("$.length()").isEqualTo(2);
	}

	@Test
	void listTasksWhenThereAreNoTasksShouldReturnEmptyArrayAndOk() {
		webTestClient
			.get()
			.uri("/tasks")
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$").isArray()
			.jsonPath("$.length()").isEqualTo(0);
	}

	@Test
	void updateTasksWhenInputIsValidAndTaskExistsShouldReturnTaskAndOk() {
		var task = new Task("New task");
		var savedTask = repository.save(task);
		savedTask.setDescription("Updated description");

		var response = new ResponseTaskDTO(
			savedTask.getId(),
			savedTask.getDescription(),
			savedTask.isFinished()
		);

		webTestClient
			.patch()
			.uri("/tasks/{id}", savedTask.getId())
			.bodyValue(savedTask)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.id").isEqualTo(response.id())
			.jsonPath("$.description").isEqualTo(response.description())
			.jsonPath("$.finished").isEqualTo(response.finished());
	}

	@Test
	void updateTasksWhenInputIsInvalidShouldReturnBadRequest() {
		var task = new Task("New task");
		var savedTask = repository.save(task);

		webTestClient
			.patch()
			.uri("/tasks/{id}", savedTask.getId())
			.header("Content-Type", "application/json")
			.bodyValue(
					"""
						{description: ""}
					"""
			)
			.exchange()
			.expectStatus()
			.isBadRequest();
	}

	@Test
	void updateTasksWhenInputIsValidAndTaskNotExistsShouldReturnNotFound() {
		var task = new Task("New task");
		task.setDescription("Updated description");

		webTestClient
			.patch()
			.uri("/tasks/{id}", 999)
			.header("Content-Type","application/json")
			.bodyValue(task)
			.exchange()
			.expectStatus().isNotFound();
	}

	@Test
	void deleteTasksWhenTaskExistShouldReturnOk() {
		var task = new Task("New task");
		var savedTask = repository.save(task);

		webTestClient
			.delete()
			.uri("/tasks/{id}", savedTask.getId())
			.exchange()
			.expectStatus().isNoContent();
	}

	@Test
	void deleteTasksWhenTaskNotExistsShouldReturnNotFound() {
		webTestClient
			.delete()
			.uri("/tasks/{id}", 999)
			.exchange()
			.expectStatus().isNotFound();
	}
}
