package com.example.ecommerceminiproject;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.ecommerceminiproject.databinding.FragmentCategoryBinding;
import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {
    private FragmentCategoryBinding binding;
    private ProductCategoryAdapter adapter;
    private ProductViewModel viewmodel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewmodel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
        setUpRecyclerView();
        observeCategories();
        viewmodel.loadProducts(false);
    }

    private void setUpRecyclerView() {
        binding.categories.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new ProductCategoryAdapter(categorySlug -> viewmodel.selectCategory(categorySlug));
        binding.categories.setAdapter(adapter);
    }

    private void observeCategories() {
        viewmodel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories == null) return;
            List<Category> list = new ArrayList<>();
            list.add(new Category(getString(R.string.category_all),Constants.CATEGORY_ALL));
            list.addAll(categories);
            adapter.submitCategories(list);
        });

        viewmodel.getSelectedCategory().observe(getViewLifecycleOwner(), slug -> {
            adapter.setSelected(slug);
            int position = adapter.positionOf(slug);
            if (position >= 0) binding.categories.smoothScrollToPosition(position);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}