package gr.aueb.cf.recipeapp.controller;

import gr.aueb.cf.recipeapp.model.entity.User;
import gr.aueb.cf.recipeapp.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SignupController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupController(UserRepository userRepository,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupSubmit(@ModelAttribute User user, Model model) {

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("error", "Username already exists");
            return "signup";
        }

        String rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                List.of()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        return "redirect:/fridge";
    }
}
