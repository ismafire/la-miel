package com.lamiel.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Controller
@SessionAttributes("cart") // This keeps the cart saved for the user while they browse!
public class StoreController {

    @Autowired
    private ProductRepository productRepository;

    // This creates a brand new cart automatically for anyone who visits your store website
    @ModelAttribute("cart")
    public Cart createCart() {
        return new Cart();
    }

    @PostConstruct
    public void loadInitialProducts() {
        if (productRepository.count() == 0) {
            productRepository.save(new Product("Mocasines", "shoes", 89.99, Arrays.asList("shoes-brown.jpg", "shoes-black.jpg")));
            productRepository.save(new Product("Elegant Office Dresses", "fashion", 120.00, Arrays.asList("dress-cream.jpg", "dress-navy.jpg", "dress-grey.jpg")));
            productRepository.save(new Product("Organic Herbal Tea Blend", "tea", 15.50, Arrays.asList("tea-blend.jpg")));
            productRepository.save(new Product("Artisanal Honey Soaps", "wellness", 8.99, Arrays.asList("soap-1.jpg", "soap-2.jpg", "soap-3.jpg")));
            productRepository.save(new Product("Premium Kitchen Utilities", "home", 45.00, Arrays.asList("kitchen-1.jpg", "kitchen-2.jpg", "kitchen-3.jpg", "kitchen-4.jpg", "kitchen-5.jpg", "kitchen-6.jpg", "kitchen-7.jpg")));
            productRepository.save(new Product("Minimalist Fans & Lamps", "home", 34.99, Arrays.asList("home-fan.jpg", "home-lamp.jpg")));
        }
    }

    // Shows the store layout and passes the current shopping cart info to the HTML page
    @GetMapping("/")
    public String viewStorefront(Model model, @ModelAttribute("cart") Cart cart) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("cartItemsCount", cart.getItems().size());
        model.addAttribute("cartTotal", cart.getTotalCost());
        return "index"; 
    }

    // Handles the action when someone clicks "Add to Cart"
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("productId") Long productId, @ModelAttribute("cart") Cart cart) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            cart.addItem(product); // Put it in our Java session cart!
        }
        return "redirect:/"; // Instantly reload the page to update counts
    }
}