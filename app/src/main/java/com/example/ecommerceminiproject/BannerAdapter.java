package com.example.ecommerceminiproject;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.ecommerceminiproject.databinding.ItemBannerBinding;
import com.example.ecommerceminiproject.Product;
import java.util.ArrayList;
import java.util.List;

public class BannerAdapter
        extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {
    private final List<Product> bannerList = new ArrayList<>();
    private final OnBannerClickListener listener;
    public interface OnBannerClickListener {
        void onBannerClick(Product product);
    }
    public BannerAdapter(OnBannerClickListener listener) {
        this.listener = listener;
    }
    public void submitList(List<Product> products) {
        bannerList.clear();
        if (products != null) {
            bannerList.addAll(products);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
            int viewType) {
        ItemBannerBinding binding =
                ItemBannerBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false);
        return new BannerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder,
            int position) {
        Product product = bannerList.get(position);
        Glide.with(holder.binding.ivBannerImage.getContext())
                .load(product.getMainImage())
               .placeholder(R.drawable.ic_launcher_background)
                       .error(R.drawable.ic_launcher_background)
                               .into(holder.binding.ivBannerImage);
        holder.binding.tvBannerTitle.setText(product.getTitle());
        holder.binding.tvBannerCta.setText("Shop Now");
        holder.binding.getRoot().setOnClickListener(v -> {
            if (listener != null) {
                listener.onBannerClick(product);
            }
        });
    }
    @Override
    public int getItemCount() {
        return bannerList.size();
    }
    static class BannerViewHolder extends RecyclerView.ViewHolder {
        ItemBannerBinding binding;
        public BannerViewHolder(@NonNull ItemBannerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}