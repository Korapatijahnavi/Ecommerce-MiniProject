package com.example.ecommerceminiproject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.ecommerceminiproject.R;
import com.example.ecommerceminiproject.databinding.ItemCartBinding;
import com.example.ecommerceminiproject.CartItem;
import com.example.ecommerceminiproject.CurrencyUtils;
import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends
        RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface OnCartActionListener {
        void onIncrease(CartItem item);
        void onDecrease(CartItem item);
        void onItemClick(CartItem item);
    }

    private final List<CartItem> items = new ArrayList<>();
    private final OnCartActionListener listener;
    public CartAdapter(OnCartActionListener listener) {
        this.listener = listener;
    }
    public void submitList(List<CartItem> newList) {
        items.clear();
        if (newList != null) items.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(ItemCartBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        private final ItemCartBinding binding;

        CartViewHolder(ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(CartItem item) {
            Context context = binding.getRoot().getContext();

            Glide.with(context)
                    .load(item.getProduct().getMainImage())
                    .into(binding.ivCartProduct);

            binding.tvCartTitle.setText(item.getProduct().getTitle());
            binding.tvCartPrice.setText(context.getString(
                    R.string.price_per_pc,
                    CurrencyUtils.format(item.getUnitPrice())
            ));
            binding.tvCartSubtotal.setText(context.getString(
                    R.string.subtotal_format,
                    CurrencyUtils.format(item.getSubtotal())
            ));
            binding.tvCartQty.setText(context.getString(
                    R.string.qty_pcs,
                    item.getQuantity()
            ));

            binding.btnCartPlus.setOnClickListener(v -> listener.onIncrease(item));
            binding.btnCartMinus.setOnClickListener(v -> listener.onDecrease(item));
            binding.getRoot().setOnClickListener(v -> listener.onItemClick(item));
        }
    }
}
