package com.thallesgarbelotti.todo_list;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import com.thallesgarbelotti.todo_list.entity.Task;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import com.thallesgarbelotti.todo_list.repository.TaskRepository;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

import java.util.List;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TodoListApplicationTests {
	private WebTestClient webTestClient;
	private final WebApplicationContext context;

	@Autowired
	private TaskRepository repository;

	TodoListApplicationTests(WebApplicationContext context) {
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
		var task = new Task("new task");

			webTestClient.post()
					.uri("/tasks")
					.bodyValue(task)
					.exchange()
					.expectStatus()
					.isCreated();
	}

	@Test
	void createTaskWhenInputIsInvalidShouldReturnBadRequest() {
		var task = new Task("");

		webTestClient.post().uri("/tasks")
				.bodyValue(task)
				.exchange()
				.expectStatus()
				.isBadRequest();
	}

	@Test
	void listTaskByIdWhenTaskExistShouldReturnTaskAndOk() {
		var task = new Task("new task");
		Task savedTask = repository.save(task);

		webTestClient
				.get()
				.uri("/tasks/{id}", savedTask.getId())
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").isEqualTo(savedTask.getId())
				.jsonPath("$.description").isEqualTo(savedTask.getDescription())
				.jsonPath("$.finished").isEqualTo(savedTask.isFinished());
	}

	@Test
	void listTaskByIdWhenTaskNotExistShouldReturnNotFound() {
		webTestClient
				.get()
				.uri("/tasks/{id}", 9999)
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	void listTasksWhenTasksExistsShouldReturnTasksAndOk() {
		var firstTask = new Task("first task");
		var secondTask = new Task("second task");

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
	void updateTasksWhenInputIsValidAndTaskExistShouldReturnTaskAndOk() {
		var task = new Task("new task");
		var savedTask = repository.save(task);
		savedTask.setDescription("Updated description");

		webTestClient
				.patch()
				.uri("/tasks/{id}", savedTask.getId())
				.bodyValue(savedTask)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").isEqualTo(savedTask.getId())
				.jsonPath("$.description").isEqualTo(savedTask.getDescription())
				.jsonPath("$.finished").isEqualTo(savedTask.isFinished());
	}

	@Test
	void updateTasksWhenInputIsInvalidShouldReturnBadRequest() {
		var task = new Task("new task");
		var savedTask = this.repository.save(task);
		var blankTask = new Task("");

		webTestClient
				.patch()
				.uri("/tasks/{id}", savedTask.getId())
				.bodyValue(blankTask)
				.exchange()
				.expectStatus().isBadRequest();
	}

	@Test
	void updateTasksWhenInputIsValidAndTaskNotExistShouldReturnNotFound() {
		var task = new Task("new task");
		task.setDescription("Description");

		webTestClient
				.patch()
				.uri("/tasks/{id}", 999)
				.bodyValue(task)
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	void deleteTasksWhenTaskExistShouldReturnOk() {
		var task = new Task("new task");
		var savedTask = this.repository.save(task);

		webTestClient
				.delete()
				.uri("/tasks/{id}", savedTask.getId())
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$").isEmpty();
	}

	@Test
	void deleteTasksWhenTaskNotExistShouldReturnNotFound() {
		var task = new Task("new task");

		this.repository.save(task);

		webTestClient
				.delete()
				.uri("/tasks/{id}", 999)
				.exchange()
				.expectStatus().isNotFound();
	}
}
