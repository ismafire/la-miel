package com.lamiel.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductRepository productRepository;

    // Read from environment variables set on Render (Environment tab).
    // Falls back to these defaults only when running locally without env vars set.
    @Value("${ADMIN_USERNAME:admin}")
    private String adminUsername;

    @Value("${ADMIN_PASSWORD:changeme}")
    private String adminPassword;

    // --- Login page ---
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Incorrect username or password.");
        }
        return "admin-login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username, @RequestParam String password, HttpSession session) {
        if (adminUsername.equals(username) && adminPassword.equals(password)) {
            session.setAttribute("isAdmin", true);
            return "redirect:/admin";
        }
        return "redirect:/admin/login?error=true";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }

    // --- Dashboard: view + manage products ---
    @GetMapping("")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("isAdmin") == null) {
            return "redirect:/admin/login";
        }
        model.addAttribute("products", productRepository.findAll());
        return "admin-dashboard";
    }

    // --- Add a new product ---
    @PostMapping("/products/add")
    public String addProduct(HttpSession session,
                              @RequestParam String name,
                              @RequestParam String category,
                              @RequestParam double price,
                              @RequestParam String imageNames) {
        if (session.getAttribute("isAdmin") == null) {
            return "redirect:/admin/login";
        }
        List<String> images = Arrays.asList(imageNames.split("\\s*,\\s*"));
        productRepository.save(new Product(name, category, price, images));
        return "redirect:/admin";
    }

    // --- Delete a product ---
    @PostMapping("/products/delete/{id}")
    public String deleteProduct(HttpSession session, @PathVariable Long id) {
        if (session.getAttribute("isAdmin") == null) {
            return "redirect:/admin/login";
        }
        productRepository.deleteById(id);
        return "redirect:/admin";
    }
}
