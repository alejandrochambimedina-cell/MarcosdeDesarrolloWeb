package com.example.demo.controller;

import com.example.demo.dto.TaskDto;
import com.example.demo.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public String tasks(Model model) {
        List<TaskDto> tasks = taskService.findAll();
        model.addAttribute("tasks", tasks);
        return "tasks";
    }
}