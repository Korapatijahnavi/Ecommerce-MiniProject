package com.example.ecommerceminiproject;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.example.ecommerceminiproject.databinding.ActivityProductDetailBinding;
import com.example.ecommerceminiproject.databinding.ItemInfoTileBinding;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {
    private ActivityProductDetailBinding binding;
    private ProductDetailViewModel detailViewModel;
    private CartViewModel cartViewModel;
    private ThumbnailAdapter thumbnailAdapter;
    private Product product;
    private int productId;
    private boolean isFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        productId = getIntent().getIntExtra(Constants.EXTRA_PRODUCT_ID, -1);
        if (productId == -1) {
            Toast.makeText(this, R.string.invalid_product, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        detailViewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        if (getSupportFragmentManager().findFragmentById(R.id.bottom_nav_container) == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.bottom_nav_container, new BottomNavigationFragment())
                    .commit();
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                goToProducts();
            }
        });

        thumbnailAdapter = new ThumbnailAdapter(this::showImage);
        binding.rvThumbnails.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rvThumbnails.setAdapter(thumbnailAdapter);

        setupClickListeners();

        detailViewModel.getProductState().observe(this, this::renderState);
        cartViewModel.getCartItems().observe(this, items -> updateCartControls());
        detailViewModel.loadProduct(productId, false);
    }

    private void goToProducts() {
        String category = product == null ? Constants.CATEGORY_ALL : product.getCategory();
        Navigator.openProducts(this, category, false);
        finish();
    }

    private void setupClickListeners() {
        binding.btnBack.setOnClickListener(v -> goToProducts());
        binding.btnFavorite.setOnClickListener(v -> toggleFavourite());
        binding.layoutError.btnRetry.setOnClickListener(v -> detailViewModel.loadProduct(productId, true));

        binding.btnAddToCart.setOnClickListener(v -> {
            if (addOne()) Toast.makeText(this, R.string.added_to_cart, Toast.LENGTH_SHORT).show();
        });
        binding.btnDetailPlus.setOnClickListener(v -> addOne());
        binding.btnDetailMinus.setOnClickListener(v -> {
            if (product != null) cartViewModel.decreaseQuantity(product.getId());
        });
    }

    private void renderState(Resource<Product> state) {
        if (state == null) return;
        boolean success = state.status == Resource.Status.SUCCESS && state.data != null;
        boolean error = state.status == Resource.Status.ERROR;

        binding.progressDetail.setVisibility(state.status == Resource.Status.LOADING ? View.VISIBLE : View.GONE);
        binding.layoutError.getRoot().setVisibility(error ? View.VISIBLE : View.GONE);
        binding.scrollDetail.setVisibility(success ? View.VISIBLE : View.INVISIBLE);
        binding.bottomBar.setVisibility(success ? View.VISIBLE : View.GONE);

        if (error) binding.layoutError.tvErrorMessage.setText(state.message);
        if (success) bindProduct(state.data);
    }

    private void bindProduct(Product p) {
        product = p;

        showImage(p.getMainImage());
        thumbnailAdapter.submitList(p.getImages());
        binding.rvThumbnails.setVisibility(p.getImages().size() > 1 ? View.VISIBLE : View.GONE);

        binding.tvTitle.setText(p.getTitle());
        binding.tvPrice.setText(CurrencyUtils.format(p.getPriceInr()));

        if (p.hasDiscount()) {
            binding.tvOriginalPrice.setVisibility(View.VISIBLE);
            binding.tvOriginalPrice.setText(CurrencyUtils.format(p.getOriginalPriceInr()));
            binding.tvOriginalPrice.setPaintFlags(
                    binding.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            binding.tvDiscountBadge.setVisibility(View.VISIBLE);
            binding.tvDiscountBadge.setText(getString(R.string.discount_off,
                    (int) Math.round(p.getDiscountPercentage())));
        } else {
            binding.tvOriginalPrice.setVisibility(View.GONE);
            binding.tvDiscountBadge.setVisibility(View.GONE);
        }

        binding.tvCategory.setText(p.getCategoryDisplayName());
        binding.tvBrand.setText(p.getBrand());
        binding.tvBrand.setVisibility(p.getBrand().isEmpty() ? View.GONE : View.VISIBLE);
        binding.tvDescription.setText(p.getDescription());

        bindTile(binding.tileRating, R.drawable.ic_star,
                String.format(Locale.US, "%.1f / 5", p.getRating()), R.string.tile_rating_label);
        bindTile(binding.tileStock, R.drawable.ic_bag,
                getString(R.string.stock_value, p.getStock()), R.string.tile_stock_label);
        bindTile(binding.tileWarranty, R.drawable.ic_verified,
                orNA(p.getWarrantyInformation()), R.string.tile_warranty_label);
        bindTile(binding.tileShipping, R.drawable.ic_shipping,
                orNA(p.getShippingInformation()), R.string.tile_shipping_label);

        if (TextUtils.isEmpty(p.getReturnPolicy())) {
            binding.tvReturnPolicy.setVisibility(View.GONE);
        } else {
            binding.tvReturnPolicy.setVisibility(View.VISIBLE);
            binding.tvReturnPolicy.setText(getString(R.string.return_policy_format, p.getReturnPolicy()));
        }

        updateCartControls();
    }

    private void showImage(String url) {
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(binding.ivMainImage);
    }

    private void bindTile(ItemInfoTileBinding tile, int iconRes, String value, int labelRes) {
        tile.ivTileIcon.setImageResource(iconRes);
        tile.tvTileValue.setText(value);
        tile.tvTileLabel.setText(labelRes);
    }

    private String orNA(String value) {
        return TextUtils.isEmpty(value) ? getString(R.string.not_available) : value;
    }

    private boolean addOne() {
        if (product == null) return false;
        boolean added = cartViewModel.addToCart(product);
        if (!added) {
            Toast.makeText(this, getString(R.string.stock_limit, product.getStock()),
                    Toast.LENGTH_SHORT).show();
        }
        return added;
    }

    private void updateCartControls() {
        if (product == null) return;
        int qty = cartViewModel.getQuantity(product.getId());
        binding.btnAddToCart.setVisibility(View.VISIBLE);
        binding.llDetailStepper.setVisibility(qty == 0 ? View.GONE : View.VISIBLE);
        binding.tvDetailQty.setText(getString(R.string.qty_pcs, qty));
        binding.tvTotalPrice.setText(CurrencyUtils.format(product.getPriceInr() * Math.max(qty, 1)));
    }

    private void toggleFavourite() {
        isFavourite = !isFavourite;
        binding.btnFavorite.setImageResource(isFavourite
                ? R.drawable.ic_favourite
                : R.drawable.ic_favourite_border);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}