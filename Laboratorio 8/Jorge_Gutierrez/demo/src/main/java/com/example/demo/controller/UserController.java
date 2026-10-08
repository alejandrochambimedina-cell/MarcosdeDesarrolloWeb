package com.example.demo.controller;

import com.example.demo.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class UserController {

    @GetMapping("/users")
    public String users(Model model) {

        List<User> users = Arrays.asList(
                new User("Jorge", "jorge@gmail.com"),
                new User("Ana", "ana@gmail.com"),
                new User("Carlos", "carlos@gmail.com")
        );

        model.addAttribute("users", users);

        return "users";
    }
}