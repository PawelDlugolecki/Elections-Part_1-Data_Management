package dlugolecki.pawel.controllers;

import dlugolecki.pawel.model.security.Role;
import dlugolecki.pawel.model.security.User;
import dlugolecki.pawel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String registerUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return "user/register";
    }

    @PostMapping("/register")
    public String registerUserPost(@ModelAttribute User user, Model model) {
        userService.registerNewUser(user);
        return "redirect:/";
    }
}
