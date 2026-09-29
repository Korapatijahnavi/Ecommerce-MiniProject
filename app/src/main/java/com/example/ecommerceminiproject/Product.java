package com.example.ecommerceminiproject;
import com.example.ecommerceminiproject.CurrencyUtils;
import com.example.ecommerceminiproject.ProductFilter;

import java.util.ArrayList;
import java.util.List;


public class Product {
    private int id;
    private String title;
    private String category;
    private double price;
    private double discountPercentage;
    private double rating;
    private int stock;
    private String brand;
    private String thumbnail;
    private List<String> images;
    private String availabilityStatus;
    private String warrantyInformation;
    private String shippingInformation;
    private String description;
    private String returnPolicy;

    public Product(){
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public double getDiscountPercentage() { return discountPercentage; }
    public double getRating() { return rating; }
    public int getStock() { return stock; }
    public String getThumbnail() { return thumbnail; }
    public String getAvailabilityStatus() { return availabilityStatus; }
    public String getWarrantyInformation() { return warrantyInformation; }
    public String getShippingInformation() { return shippingInformation; }
    public String getReturnPolicy() { return returnPolicy; }

    public String getDescription(){
        return description;
    }
    public String getBrand() {
        return brand == null ? "" : brand;
    }

    public List<String> getImages() {
        return images == null ? new ArrayList<>() : images;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public long getPriceInr() {
        return CurrencyUtils.usdToInr(price);
    }
    public long getOriginalPriceInr() {
        if (discountPercentage <= 0 || discountPercentage >= 100) return getPriceInr();
        return CurrencyUtils.usdToInr(price / (1 - discountPercentage / 100.0));
    }
    public boolean hasDiscount() {
        return discountPercentage >= 1;
    }
    public String getCategoryDisplayName() {
        return ProductFilter.toDisplayName(category);
    }
    public String getMainImage() {
        if (thumbnail != null && !thumbnail.isEmpty()) return thumbnail;
        List<String> list = getImages();
        return list.isEmpty() ? null : list.get(0);
    }
}