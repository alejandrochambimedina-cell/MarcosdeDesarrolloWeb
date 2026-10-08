package com.utp.marcos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TaskController {

    private List<String> tasks = new ArrayList<>();

    @GetMapping("/tasks")
    public String viewTasks(Model model) {
        model.addAttribute("tasks", tasks);
        return "tasks";
    }

    @PostMapping("/add-task")
    public String addTask(@RequestParam("task") String task) {
        if (task != null && !task.trim().isEmpty()) {
            tasks.add(task);
        }
        return "redirect:/tasks";
    }
}
