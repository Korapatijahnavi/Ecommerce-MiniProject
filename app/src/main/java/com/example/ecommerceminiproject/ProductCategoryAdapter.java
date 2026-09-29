package com.example.ecommerceminiproject;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommerceminiproject.databinding.ItemCategoryProductBinding;
import java.util.ArrayList;
import java.util.List;
public class ProductCategoryAdapter
        extends RecyclerView.Adapter<ProductCategoryAdapter.CategoryViewHolder> {
    public interface OnCategoryClickListener {
        void onCategoryClick(String categorySlug);
    }

    private final List<Category> categoryList = new ArrayList<>();
    private final OnCategoryClickListener clickListener;
    private String selectedSlug = Constants.CATEGORY_ALL;

    public ProductCategoryAdapter(OnCategoryClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void submitCategories(List<Category> categories) {
        categoryList.clear();
        if (categories != null) categoryList.addAll(categories);
        notifyDataSetChanged();
    }

    public void setSelected(String slug) {
        selectedSlug = slug == null ? Constants.CATEGORY_ALL : slug;
        notifyDataSetChanged();
    }

    public int positionOf(String slug) {
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getSlug().equals(slug)) return i;
        }
        return -1;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(ItemCategoryProductBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(categoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemCategoryProductBinding binding;

        CategoryViewHolder(ItemCategoryProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Category category) {
            boolean selected = category.getSlug().equals(selectedSlug);
            binding.categoryname.setText(category.getName());
            binding.categoryname.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(),
                    selected ? R.color.primary : R.color.text_secondary));
            binding.categoryname.setTypeface(null, selected ? Typeface.BOLD : Typeface.NORMAL);
            binding.categoryname.setVisibility(selected ? View.VISIBLE : View.INVISIBLE);
            binding.getRoot().setOnClickListener(v -> {
                if (clickListener != null) clickListener.onCategoryClick(category.getSlug());
            });
        }
    }
}