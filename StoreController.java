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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes("cart") // This keeps the cart saved for the user while they browse!
public class StoreController {

    @Autowired
    private ProductRepository productRepository;

    // The category order here controls the order sections appear on the page
    private static final List<String> CATEGORY_ORDER = Arrays.asList(
            "clothing", "shoes", "soap", "kitchen", "tea", "home"
    );

    // This creates a brand new cart automatically for anyone who visits your store website
    @ModelAttribute("cart")
    public Cart createCart() {
        return new Cart();
    }

    @PostConstruct
    public void loadInitialProducts() {
        if (productRepository.count() == 0) {
            productRepository.save(new Product("Mocasines", "shoes", 89.99, Arrays.asList("shoes-brown.jpg", "shoes-black.jpg")));
            productRepository.save(new Product("Elegant Office Dresses", "clothing", 120.00, Arrays.asList("dress-cream.jpg", "dress-navy.jpg", "dress-grey.jpg")));
            productRepository.save(new Product("Organic Herbal Tea Blend", "tea", 15.50, Arrays.asList("tea-blend.jpg")));
            productRepository.save(new Product("Artisanal Honey Soaps", "soap", 8.99, Arrays.asList("soap-1.jpg", "soap-2.jpg", "soap-3.jpg")));
            productRepository.save(new Product("Kitchen Item A", "kitchen", 15.00, Arrays.asList("kitchen-1.jpg")));
            productRepository.save(new Product("Kitchen Item B", "kitchen", 15.00, Arrays.asList("kitchen-2.jpg")));
            productRepository.save(new Product("Kitchen Item C", "kitchen", 15.00, Arrays.asList("kitchen-3.jpg")));
            productRepository.save(new Product("Kitchen Item D", "kitchen", 15.00, Arrays.asList("kitchen-4.jpg")));
            productRepository.save(new Product("Kitchen Item E", "kitchen", 15.00, Arrays.asList("kitchen-5.jpg")));
            productRepository.save(new Product("Kitchen Item F", "kitchen", 15.00, Arrays.asList("kitchen-6.jpg")));
            productRepository.save(new Product("Kitchen Item G", "kitchen", 15.00, Arrays.asList("kitchen-7.jpg")));
            productRepository.save(new Product("Minimalist Fans & Lamps", "home", 34.99, Arrays.asList("home-fan.jpg", "home-lamp.jpg")));
        }
    }

    // Shows the store layout, grouped into sections by category
    @GetMapping("/")
    public String viewStorefront(Model model, @ModelAttribute("cart") Cart cart) {
        Map<String, List<Product>> categorizedProducts = new LinkedHashMap<>();
        for (String category : CATEGORY_ORDER) {
            categorizedProducts.put(category, productRepository.findByCategory(category));
        }
        model.addAttribute("categorizedProducts", categorizedProducts);
        model.addAttribute("cartItemsCount", cart.getItems().size());
        model.addAttribute("cartTotal", cart.getTotalCost());
        return "index";
    }

    // Handles the action when someone clicks "Add"
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("productId") Long productId, @ModelAttribute("cart") Cart cart) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            cart.addItem(product); // Put it in our Java session cart!
        }
        return "redirect:/"; // Instantly reload the page to update counts
    }
}
