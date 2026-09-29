package com.example.ecommerceminiproject;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.ecommerceminiproject.databinding.ActivityCartBinding;

public class CartActivity extends AppCompatActivity {
    private ActivityCartBinding binding;
    private CartViewModel cartViewModel;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.bottom_nav_container, new BottomNavigationFragment())
                    .commit();
        }

        binding.layoutTabs.tabCartItems.setSelected(true);
        binding.layoutTabs.tabDelivery.setSelected(false);
        binding.btnBack.setOnClickListener(v -> goToAllProducts());
        setupEmptyState();
        setupCartList();
        observeCart();
        binding.btnCheckout.setOnClickListener(v-> gotToDelivery());
    }
    private void setupEmptyState() {
        binding.layoutEmpty.tvEmptyTitle.setText(R.string.cart_empty_title);
        binding.layoutEmpty.tvEmptyMessage.setText(R.string.cart_empty_message);
        binding.layoutEmpty.btnEmptyAction.setVisibility(View.VISIBLE);
        binding.layoutEmpty.btnEmptyAction.setText(R.string.start_shopping);
        binding.layoutEmpty.btnEmptyAction.setOnClickListener(v ->
                Navigator.openProducts(this, Constants.CATEGORY_ALL, false));
    }
    private void setupCartList() {
        cartAdapter = new CartAdapter(new CartAdapter.OnCartActionListener() {
            @Override
            public void onIncrease(CartItem item) {
                if (!cartViewModel.addToCart(item.getProduct())) {
                    Toast.makeText(CartActivity.this,
                            getString(R.string.stock_limit, item.getProduct().getStock()),
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onDecrease(CartItem item) {
                cartViewModel.decreaseQuantity(item.getProductId());
            }
            @Override
            public void onItemClick(CartItem item) {
                Navigator.openProductDetail(CartActivity.this, item.getProductId());
            }
        });
        binding.rvCart.setLayoutManager(new LinearLayoutManager(this));
        binding.rvCart.setAdapter(cartAdapter);
    }

    private void observeCart() {
        cartViewModel.getCartItems().observe(this, items -> {
            cartAdapter.submitList(items);
            boolean empty = items == null || items.isEmpty();
            binding.layoutEmpty.getRoot().setVisibility(empty ? View.VISIBLE : View.GONE);
            binding.rvCart.setVisibility(empty ? View.GONE : View.VISIBLE);
            binding.llCartBottom.setVisibility(empty ? View.GONE : View.VISIBLE);
        });

        cartViewModel.getCartTotal().observe(this, total -> {
            long value = total == null ? 0 : total;
            binding.tvCartTotal.setText(CurrencyUtils.format(value));
            long remaining = Constants.FREE_DELIVERY_THRESHOLD_INR - value;
            if (remaining <= 0) {
                binding.tvDeliveryHint.setText(R.string.free_delivery_unlocked);
            } else {
                binding.tvDeliveryHint.setText(getString(R.string.free_delivery_remaining,
                        CurrencyUtils.format(remaining)));
            }
        });
    }
    public void gotToDelivery(){
        if(cartViewModel.isCartEmpty()){
            Toast.makeText(this, R.string.cart_empty_toast,Toast.LENGTH_SHORT).show();
            return;
        }
        Navigator.openDelivery(this);
    }
    public void goToAllProducts(){
        Navigator.openProducts(this, Constants.CATEGORY_ALL,false);
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}