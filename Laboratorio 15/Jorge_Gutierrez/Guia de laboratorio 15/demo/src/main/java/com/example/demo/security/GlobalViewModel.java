package com.example.demo.security;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalViewModel {

    private final SecurityView securityView;

    public GlobalViewModel(SecurityView securityView) {
        this.securityView = securityView;
    }

    @ModelAttribute
    public void addSecurityAttributes(Model model) {
        model.addAttribute("isAuthenticated", securityView.isAuthenticated());
        model.addAttribute("currentUsername", securityView.getUsername());
    }
}
