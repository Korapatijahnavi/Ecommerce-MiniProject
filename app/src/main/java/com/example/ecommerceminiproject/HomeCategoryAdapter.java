package com.example.ecommerceminiproject;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommerceminiproject.databinding.ItemCategoryHomeBinding;
import com.example.ecommerceminiproject.Category;
import java.util.ArrayList;
import java.util.List;

public class HomeCategoryAdapter
        extends RecyclerView.Adapter<HomeCategoryAdapter.CategoryViewHolder> {
    private final List<Category> categoryList = new ArrayList<>();
    private final OnCategoryClickListener listener;
    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public HomeCategoryAdapter(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<Category> categories) {
        categoryList.clear();
        if (categories != null) {
            categoryList.addAll(categories);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
            int viewType) {
        ItemCategoryHomeBinding binding =
                ItemCategoryHomeBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(
            @NonNull CategoryViewHolder holder,
            int position) {
        Category category = categoryList.get(position);
        holder.binding.tvCategoryName.setText(category.getName());
        holder.binding.ivCategory.setImageResource(GroceryCategorizer.iconFor(category.getSlug()));
        holder.binding.getRoot().setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategoryClick(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    static class CategoryViewHolder
            extends RecyclerView.ViewHolder {
        ItemCategoryHomeBinding binding;
        public CategoryViewHolder(@NonNull ItemCategoryHomeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}