package springbootapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springbootapplication.model.User;
import springbootapplication.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "")
    public String printAllUsers(Model model) {
        model.addAttribute("usersList", userService.getAllUsers());
        model.addAttribute("currentUser", userService.findByEmail
                (SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("newUser", new User());
        return "admin";
    }

    @PostMapping(value = "/saveUser")
    public String saveUser(User user, @RequestParam(value = "AddRoles", required = false) String[] roles) {
        userService.encodePassword(user);
        userService.save(user, roles);
        return "redirect:/admin";
    }

    @DeleteMapping("user-delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @PutMapping("/user-update/{id}")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "roles", required = false) String[] roles) {
        userService.update(user, roles);
        return "redirect:/admin";
    }

}
