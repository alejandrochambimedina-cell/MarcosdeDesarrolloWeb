package com.example.demo.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("securityView")
public class SecurityView {

    public boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return auth != null
                && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getPrincipal());
    }

    public String getUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (isAuthenticated()) {
            return auth.getName();
        }

        return null;
    }
}
