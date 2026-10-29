package com.utp.marcos.controller;

import com.utp.marcos.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    private List<User> users = new ArrayList<>();

    @GetMapping("/users")
    public String viewUsers(Model model) {
        model.addAttribute("users", users);
        return "users";
    }

    @PostMapping("/add-user")
    public String addUser(@RequestParam("name") String name,
                          @RequestParam("email") String email) {
        if (name != null && !name.trim().isEmpty() && email != null && !email.trim().isEmpty()) {
            users.add(new User(name, email));
        }
        return "redirect:/users";
    }
}
