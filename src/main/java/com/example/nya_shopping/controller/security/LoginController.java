package com.example.nya_shopping.controller.security;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(HttpSession session, Model model) {

        // CustomAuthenticationEntryPointで出たエラーを拾う
        String errorMessage = (String) session.getAttribute("errorMessage");
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            session.removeAttribute("errorMessage");
        }

        // CustomAuthenticationFailureHandlerで出たエラーを拾う
        List<String> errorMessages = (List<String>) session.getAttribute("errorMessages");
        if (errorMessages != null && !errorMessages.isEmpty()) {
            model.addAttribute("errorMessages", errorMessages);

            String email = (String) session.getAttribute("email");
            model.addAttribute("email", email);

            session.removeAttribute("errorMessages");
            session.removeAttribute("email");
        }

        return "login";
    }
}
