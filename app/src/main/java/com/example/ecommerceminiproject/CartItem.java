package com.example.ecommerceminiproject;

public class CartItem {
    private final Product product;
    private int quantity;
    public CartItem(Product product, int quantity)
    {
        this.product=product;
        this.quantity=quantity;
    }
    public Product getProduct(){ return product;}
    public int getProductId(){ return product.getId();}
    public int getQuantity(){ return quantity;}
    public void setQuantity(int quantity){
        this.quantity=quantity;
    }
    public long getUnitPrice(){
        return product.getPriceInr();
    }
    public long getSubtotal(){
        return product.getPriceInr()*quantity;
    }
    public CartItem copy(){
        return new CartItem(product,quantity);
    }
}
