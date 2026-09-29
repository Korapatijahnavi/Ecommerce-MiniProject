package com.example.ecommerceminiproject;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.ecommerceminiproject.databinding.ItemBestDealBinding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ProductAdapter extends
        RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    public interface OnProductActionListener {
        void onProductClick(Product product);
        void onIncrease(Product product);
        void onDecrease(Product product);
    }

    private final List<Product> productList = new ArrayList<>();
    private final Set<Integer> favourites = new HashSet<>();
    private final OnProductActionListener listener;
    private final int fixedItemWidthDp;
    private Map<Integer, Integer> cartQuantities = new HashMap<>();
    public ProductAdapter(OnProductActionListener listener)
    {
        this(listener, 0);
    }

    public ProductAdapter(OnProductActionListener listener, int fixedItemWidthDp) {
        this.listener = listener;
        this.fixedItemWidthDp = fixedItemWidthDp;
    }

    public void submitList(List<Product> products) {
        productList.clear();
        if (products != null) productList.addAll(products);
        notifyDataSetChanged();
    }

    public void setCartQuantities(Map<Integer, Integer> quantities) {
        cartQuantities = quantities == null ?
                new HashMap<>() : new HashMap<>(quantities);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBestDealBinding binding = ItemBestDealBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        ItemBestDealBinding b = holder.binding;

        Glide.with(b.imgProduct.getContext())
                .load(product.getMainImage())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(b.imgProduct);

        b.tvProductName.setText(product.getTitle());
        b.tvPrice.setText(CurrencyUtils.format(product.getPriceInr()));

        if (product.hasDiscount()) {
            String discount = String.format(Locale.US, "%.0f%%", product.getDiscountPercentage());
            b.tvDiscount.setText(discount + "\nOff");
            b.tvOff.setText(discount + " Off");
            b.tvOldPrice.setText(CurrencyUtils.format(product.getOriginalPriceInr()));
            b.tvOldPrice.setPaintFlags(b.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            b.tvDiscount.setVisibility(View.VISIBLE);
            b.tvOldPrice.setVisibility(View.VISIBLE);
            b.tvOff.setVisibility(View.VISIBLE);
        } else {
            b.tvDiscount.setVisibility(View.GONE);
            b.tvOldPrice.setVisibility(View.GONE);
            b.tvOff.setVisibility(View.GONE);
        }

        Integer qty = cartQuantities.get(product.getId());
        boolean inCart = qty != null && qty > 0;
        b.btnAdd.setVisibility(inCart ? View.GONE : View.VISIBLE);
        b.llStepper.setVisibility(inCart ? View.VISIBLE : View.GONE);
        if (inCart) {
            b.tvQty.setText(b.tvQty.getContext().getString(R.string.qty_pcs, qty));
        }

        b.imgFavourite.setImageResource(favourites.contains(product.getId())
                ? R.drawable.ic_favourite
                : R.drawable.ic_favourite_border);

        b.btnAdd.setOnClickListener(v -> {
            if (listener != null) listener.onIncrease(product);
        });
        b.btnPlus.setOnClickListener(v -> {
            if (listener != null) listener.onIncrease(product);
        });
        b.btnMinus.setOnClickListener(v -> {
            if (listener != null) listener.onDecrease(product);
        });
        b.imgFavourite.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;
            if (!favourites.remove(product.getId())) favourites.add(product.getId());
            notifyItemChanged(pos);
        });
        b.getRoot().setOnClickListener(v -> {
            if (listener != null) listener.onProductClick(product);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        final ItemBestDealBinding binding;
        ProductViewHolder(@NonNull ItemBestDealBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}