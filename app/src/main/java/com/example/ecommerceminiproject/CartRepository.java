package com.example.ecommerceminiproject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class CartRepository {
    private static CartRepository instance;
    private final LinkedHashMap<Integer, CartItem> items = new LinkedHashMap<>();
    private final MutableLiveData<List<CartItem>> cartItems = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Integer> cartCount = new MutableLiveData<>(0);
    private final MutableLiveData<Long> cartTotal = new MutableLiveData<>(0L);
    private CartRepository() {
    }

    public static synchronized CartRepository getInstance() {
        if (instance == null) instance = new CartRepository();
        return instance;
    }

    public LiveData<List<CartItem>> getCartItems() { return cartItems; }
    public LiveData<Integer> getCartCount() { return cartCount; }
    public LiveData<Long> getCartTotal() { return cartTotal; }

    public boolean addToCart(Product product) {
        CartItem existing = items.get(product.getId());
        int currentQty = existing == null ? 0 : existing.getQuantity();

        if (product.getStock() > 0 && currentQty >= product.getStock()) {
            return false;
        }

        if (existing == null) {
            items.put(product.getId(), new CartItem(product, 1));
        } else {
            existing.setQuantity(currentQty + 1);
        }
        publish();
        return true;
    }
    public void decreaseQuantity(int productId) {
        CartItem existing = items.get(productId);
        if (existing == null) return;

        if (existing.getQuantity() <= 1) {
            items.remove(productId);
        } else {
            existing.setQuantity(existing.getQuantity() - 1);
        }
        publish();
    }

    public void removeFromCart(int productId) {
        if (items.remove(productId) != null) publish();
    }

    public int getQuantity(int productId) {
        CartItem item = items.get(productId);
        return item == null ? 0 : item.getQuantity();
    }

    public void clear() {
        items.clear();
        publish();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public long getTotalNow() {
        long total = 0;
        for (CartItem item : items.values()) total += item.getSubtotal();
        return total;
    }
    private void publish() {
        List<CartItem> snapshot = new ArrayList<>();
        long total = 0;
        int count = 0;
        for (CartItem item : items.values()) {
            snapshot.add(item.copy());
            total += item.getSubtotal();
            count += item.getQuantity();
        }
        cartItems.setValue(snapshot);
        cartCount.setValue(count);
        cartTotal.setValue(total);
    }
}