package com.example.ecommerceminiproject;
import java.util.ArrayList;
import java.util.List;
public class ProductResponse {
    private List<Product> products;
    private int total;
    private int skip;
    private int limit;

    public List<Product> getProducts() {
        return products == null ? new ArrayList<>() : products;
    }
    public int getTotal() { return total; }
    public int getSkip() { return skip; }
    public int getLimit() { return limit; }
}
