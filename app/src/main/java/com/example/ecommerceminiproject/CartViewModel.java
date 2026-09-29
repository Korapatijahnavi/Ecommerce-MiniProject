package com.example.ecommerceminiproject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartViewModel extends ViewModel {
    private final CartRepository repository=CartRepository.getInstance();
    public LiveData<List<CartItem>> getCartItems(){
        return repository.getCartItems();
    }
    public LiveData<Integer> getCartCount(){
        return repository.getCartCount();
    }
    public LiveData<Long> getCartTotal() {
        return repository.getCartTotal();
    }
    public boolean addToCart(Product product) { return repository.addToCart(product); }
    public void decreaseQuantity(int productId) { repository.decreaseQuantity(productId); }
    public void removeFromCart(int productId) { repository.removeFromCart(productId); }
    public int getQuantity(int productId) { return repository.getQuantity(productId); }
    public void clearCart() { repository.clear(); }
    public boolean isCartEmpty() { return repository.isEmpty(); }
    public long getTotalNow() { return repository.getTotalNow(); }
    public static Map<Integer, Integer> toQuantityMap(List<CartItem> items) {
        Map<Integer, Integer> map = new HashMap<>();
        if (items == null) return map;
        for (CartItem item : items) map.put(item.getProductId(), item.getQuantity());
        return map;
    }
}