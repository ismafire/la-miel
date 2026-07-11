package com.lamiel.store;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Product> items = new ArrayList<>();

    // Add an item to the cart
    public void addItem(Product product) {
        this.items.add(product);
    }

    // Get all items in the cart
    public List<Product> getItems() {
        return items;
    }

    // Calculate the total cost of everything in the cart
    public double getTotalCost() {
        return items.stream().mapToDouble(Product::getPrice).sum();
    }

    // Clear the cart when they checkout
    public void clear() {
        items.clear();
    }
}