package com.example.ecommerceminiproject;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.ecommerceminiproject.databinding.ActivityProductsBinding;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    private ActivityProductsBinding binding;
    private ProductAdapter adapter;
    private ProductViewModel viewModel;
    private CartViewModel cartViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        if (getSupportFragmentManager().findFragmentById(R.id.category_container) == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.category_container, new CategoryFragment())
                    .commit();
        }
        if (getSupportFragmentManager().findFragmentById(R.id.bottomnavigationcontainer) == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.bottomnavigationcontainer, new BottomNavigationFragment())
                    .commit();
        }

        setupProductList();
        setupSearch();
        binding.backarrow.setOnClickListener(v -> finish());
        binding.layoutError.btnRetry.setOnClickListener(v -> viewModel.loadProducts(true));
        observeData();
        handleIntent(getIntent());
        viewModel.loadProducts(false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent == null) return;
        String category = intent.getStringExtra(Constants.EXTRA_CATEGORY);
        if (category != null) viewModel.selectCategory(category);
        if (intent.getBooleanExtra(Constants.EXTRA_FOCUS_SEARCH, false)) {
            binding.searchbar.requestFocus();
            binding.searchbar.post(() -> {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) imm.showSoftInput(binding.searchbar, InputMethodManager.SHOW_IMPLICIT);
            });
        }
    }

    private void setupProductList() {
        adapter = new ProductAdapter(new ProductAdapter.OnProductActionListener() {
            @Override
            public void onProductClick(Product product) {
                Navigator.openProductDetail(ProductActivity.this, product.getId());
            }

            @Override
            public void onIncrease(Product product) {
                if (!cartViewModel.addToCart(product)) {
                    Toast.makeText(ProductActivity.this,
                            getString(R.string.stock_limit, product.getStock()),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDecrease(Product product) {
                cartViewModel.decreaseQuantity(product.getId());
            }
        });
        binding.productItems.setLayoutManager(new GridLayoutManager(this, 2));
        binding.productItems.setAdapter(adapter);
    }

    private void setupSearch() {
        binding.searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setSearchQuery(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void observeData() {
        viewModel.getProductsState().observe(this, state -> render());
        viewModel.getFilteredProducts().observe(this, products -> {
            adapter.submitList(products);
            render();
        });
        cartViewModel.getCartItems().observe(this,
                items -> adapter.setCartQuantities(CartViewModel.toQuantityMap(items)));
    }

    private void render() {
        Resource<List<Product>> state = viewModel.getProductsState().getValue();
        List<Product> products = viewModel.getFilteredProducts().getValue();
        boolean loading = state == null || state.status == Resource.Status.LOADING;
        boolean error = state != null && state.status == Resource.Status.ERROR;
        boolean hasItems = products != null && !products.isEmpty();

        binding.progressProducts.setVisibility(loading && !hasItems ? View.VISIBLE : View.GONE);
        binding.layoutError.getRoot().setVisibility(error && !hasItems ? View.VISIBLE : View.GONE);
        binding.productItems.setVisibility(hasItems ? View.VISIBLE : View.GONE);

        boolean showEmpty = !loading && !error && !hasItems;
        binding.layoutEmpty.getRoot().setVisibility(showEmpty ? View.VISIBLE : View.GONE);

        if (error) binding.layoutError.tvErrorMessage.setText(state.message);
        if (showEmpty) {
            binding.layoutEmpty.tvEmptyTitle.setText(R.string.empty_products_title);
            binding.layoutEmpty.tvEmptyMessage.setText(state.status == Resource.Status.EMPTY
                    ? R.string.empty_products_message
                    : R.string.empty_filter_message);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}