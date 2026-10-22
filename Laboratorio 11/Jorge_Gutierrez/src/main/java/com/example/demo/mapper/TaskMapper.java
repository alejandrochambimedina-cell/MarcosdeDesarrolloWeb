package com.example.demo.mapper;

import com.example.demo.dto.TaskDto;
import com.example.demo.model.Task;

public class TaskMapper {

    private TaskMapper() {
    }

    public static TaskDto toDto(Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.isCompleted()
        );
    }
}