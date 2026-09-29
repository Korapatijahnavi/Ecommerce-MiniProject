package com.example.ecommerceminiproject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private static final int BANNER_COUNT = 4;
    private static final int CATEGORY_COUNT = 8;
    private static final int DEAL_COUNT = 14;
    private final ProductRepository repository = ProductRepository.getInstance();
    private final MutableLiveData<List<Product>> banners = new MutableLiveData<>();
    private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private final MutableLiveData<List<Product>> deals = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<List<Product>> getBanners() {
        return banners;
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public LiveData<List<Product>> getDeals() {
        return deals;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loadHome() {
        repository.getAllProducts(false, new RepositoryCallback<List<Product>>() {
            @Override
            public void onSuccess(List<Product> products) {
                if (products == null || products.isEmpty()) {
                    clearAll();
                    return;
                }
                banners.setValue(new ArrayList<>(products.subList(0, Math.min(BANNER_COUNT, products.size()))));

                List<Category> all = ProductFilter.extractCategories(products);
                categories.setValue(new ArrayList<>(all.subList(0, Math.min(CATEGORY_COUNT, all.size()))));

                deals.setValue(ProductFilter.topDeals(products, DEAL_COUNT));
                error.setValue(null);
            }
            @Override
            public void onError(String message, int errorCode) {
                clearAll();
                error.setValue(message);
            }
        });
    }

    private void clearAll() {
        banners.setValue(new ArrayList<>());
        categories.setValue(new ArrayList<>());
        deals.setValue(new ArrayList<>());
    }
}