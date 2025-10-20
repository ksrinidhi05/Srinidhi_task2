package com.example.demo.controller;

import com.example.demo.model.Task;
import com.example.demo.repository.TaskRepository;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodSpec;
import io.kubernetes.client.util.ClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;
    private CoreV1Api api;

    // Constructor to initialize the Kubernetes API client
    public TaskController() {
        try {
            ApiClient client = ClientBuilder.cluster().build();
            Configuration.setDefaultApiClient(client);
            this.api = new CoreV1Api();
        } catch (Exception e) {
            System.out.println("Failed to load in-cluster K8s config, falling back to default. Error: " + e.getMessage());
            try {
                ApiClient client = ClientBuilder.defaultClient();
                Configuration.setDefaultApiClient(client);
                this.api = new CoreV1Api();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // --- (Your existing endpoints remain the same) ---
    
    @PutMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task savedTask = taskRepository.save(task);
        return ResponseEntity.ok(savedTask);
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable String id) {
        return taskRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/findByName/{name}")
    public ResponseEntity<List<Task>> getTasksByName(@PathVariable String name) {
        List<Task> tasks = taskRepository.findByNameContaining(name);
        if (tasks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(tasks);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable String id) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
        return ResponseEntity.ok("Task deleted successfully with id: " + id);
    }

    // --- FINAL, SIMPLIFIED EXECUTE ENDPOINT ---
    @PutMapping("/{id}/execute")
    public ResponseEntity<Task> executeTask(@PathVariable String id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Task task = optionalTask.get();
        String podName = "task-runner-" + task.getId() + "-" + System.currentTimeMillis();

        try {
            V1Pod pod = new V1Pod()
                .apiVersion("v1")
                .kind("Pod")
                .metadata(new V1ObjectMeta().name(podName))
                .spec(new V1PodSpec()
                    .addContainersItem(new V1Container()
                        .name("task-runner-container")
                        .image("busybox")
                        .command(Arrays.asList("/bin/sh", "-c", task.getCommand())))
                    .restartPolicy("Never"));

            // This is the correct method call for library version 15.0.1
            api.createNamespacedPod("default", pod, null, null, null);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        // Return a simple OK response. This fulfills the requirement.
        return ResponseEntity.ok(task);
    }
}