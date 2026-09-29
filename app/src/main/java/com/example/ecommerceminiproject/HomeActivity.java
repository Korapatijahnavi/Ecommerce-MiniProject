package com.example.ecommerceminiproject;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.ecommerceminiproject.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private HomeViewModel homeViewModel;
    private CartViewModel cartViewModel;
    private BannerAdapter bannerAdapter;
    private HomeCategoryAdapter categoryAdapter;
    private ProductAdapter dealsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        if (getSupportFragmentManager().findFragmentById(R.id.bottomNavigation) == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.bottomNavigation, new BottomNavigationFragment())
                    .commit();
        }

        setupRecyclerViews();
        observeData();
        binding.etSearch.setOnClickListener(v -> openSearch());
        binding.tvViewAll.setOnClickListener(v ->
                Navigator.openProducts(this, Constants.CATEGORY_ALL, false));
        homeViewModel.loadHome();
    }

    private void setupRecyclerViews() {
        bannerAdapter = new BannerAdapter(product ->
                Navigator.openProductDetail(this, product.getId()));
        binding.rvBanners.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvBanners.setAdapter(bannerAdapter);

        categoryAdapter = new HomeCategoryAdapter(category ->
                Navigator.openProducts(this, category.getSlug(), false));
        binding.rvCategories.setLayoutManager(new GridLayoutManager(this, 4));
        binding.rvCategories.setNestedScrollingEnabled(false);
        binding.rvCategories.setAdapter(categoryAdapter);

        dealsAdapter = new ProductAdapter(new ProductAdapter.OnProductActionListener() {
            @Override
            public void onProductClick(Product product) {
                Navigator.openProductDetail(HomeActivity.this, product.getId());
            }
            @Override
            public void onIncrease(Product product) {
                if (!cartViewModel.addToCart(product)) {
                    Toast.makeText(HomeActivity.this,
                            getString(R.string.stock_limit, product.getStock()),
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onDecrease(Product product) {
                cartViewModel.decreaseQuantity(product.getId());
            }
        }, 160);
        binding.rvBestDeals.setLayoutManager(new GridLayoutManager(this,2));
        binding.rvBestDeals.setAdapter(dealsAdapter);
    }

    private void observeData() {
        homeViewModel.getBanners().observe(this, bannerAdapter::submitList);
        homeViewModel.getCategories().observe(this, categoryAdapter::submitList);
        homeViewModel.getDeals().observe(this, dealsAdapter::submitList);
        homeViewModel.getError().observe(this, message -> {
            if (message != null) Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        });
        cartViewModel.getCartItems().observe(this, items ->
                dealsAdapter.setCartQuantities(CartViewModel.toQuantityMap(items)));
    }

    private void openSearch() {
        Navigator.openProducts(this, Constants.CATEGORY_ALL, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}