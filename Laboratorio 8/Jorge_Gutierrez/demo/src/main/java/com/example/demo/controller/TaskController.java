package com.example.demo.controller;

import com.example.demo.model.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class TaskController {

    @GetMapping("/tasks")
    public String tasks(Model model) {

        List<Task> tasks = Arrays.asList(
                new Task("Estudiar Spring Boot", false),
                new Task("Completar la guía de laboratorio", true),
                new Task("Practicar Thymeleaf", false)
        );

        model.addAttribute("tasks", tasks);

        return "tasks";
    }
}