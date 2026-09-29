package com.example.ecommerceminiproject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends ViewModel {
    private final ProductRepository repository = ProductRepository.getInstance();
    private final MutableLiveData<Resource<List<Product>>> productsState = new MutableLiveData<>();
    private final MutableLiveData<List<Product>> filteredProducts = new MutableLiveData<>();
    private final MutableLiveData<List<Category>> categories = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> selectedCategory = new MutableLiveData<>(Constants.CATEGORY_ALL);
    private List<Product> allProducts = new ArrayList<>();
    private String searchQuery = "";
    public LiveData<Resource<List<Product>>> getProductsState() {
        return productsState;
    }

    public LiveData<List<Product>> getFilteredProducts() {
        return filteredProducts;
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public LiveData<String> getSelectedCategory() {
        return selectedCategory;
    }

    public void loadProducts(boolean forceRefresh) {
        Resource<List<Product>> current = productsState.getValue();
        if (!forceRefresh && current != null && current.status != Resource.Status.ERROR) return;

        productsState.setValue(Resource.loading());
        repository.getAllProducts(forceRefresh, new RepositoryCallback<List<Product>>() {
            @Override
            public void onSuccess(List<Product> products) {
                allProducts = products == null ? new ArrayList<>() : new ArrayList<>(products);
                categories.setValue(ProductFilter.extractCategories(allProducts));
                productsState.setValue(allProducts.isEmpty()
                        ? Resource.<List<Product>>empty()
                        : Resource.success(allProducts));
                applyFilters();
            }

            @Override
            public void onError(String message, int errorCode) {
                productsState.setValue(Resource.error(message, errorCode));
            }
        });
    }

    public void selectCategory(String slug) {
        String newSlug = slug == null ? Constants.CATEGORY_ALL : slug;
        if (newSlug.equals(selectedCategory.getValue())) return;
        selectedCategory.setValue(newSlug);
        applyFilters();
    }

    public void setSearchQuery(String query) {
        String newQuery = query == null ? "" : query;
        if (newQuery.equals(searchQuery)) return;
        searchQuery = newQuery;
        applyFilters();
    }

    private void applyFilters() {
        if (allProducts.isEmpty()) return;
        filteredProducts.setValue(ProductFilter.filter(allProducts, selectedCategory.getValue(), searchQuery));
    }
}