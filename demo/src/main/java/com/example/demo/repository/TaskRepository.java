package com.example.demo.repository;

import com.example.demo.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {
    // This method finds tasks where the 'name' field contains the search string
    List<Task> findByNameContaining(String name); // [cite: 74, 75]
}