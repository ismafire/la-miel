package com.lamiel.store;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ElementCollection;
import java.util.List;

@Entity
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String category;
    private double price;
    
    @ElementCollection
    private List<String> imageNames; // Holds your image filenames (e.g., ["shoes-brown.jpg", "shoes-black.jpg"])

    // Empty constructor required for database operations
    public Product() {}

    public Product(String name, String category, double price, List<String> imageNames) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.imageNames = imageNames;
    }

    // Getters and Setters (so other files can access the data)
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public List<String> getImageNames() { return imageNames; }
}