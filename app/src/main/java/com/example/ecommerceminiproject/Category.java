package com.example.ecommerceminiproject;
public class Category {
    private String name;
    private String slug;
    public Category(String name, String slug) {
        this.name = name;
        this.slug = slug;

    }
    public String getName() {
        return name;
    }
    public String getSlug() {
        return slug;
    }

}