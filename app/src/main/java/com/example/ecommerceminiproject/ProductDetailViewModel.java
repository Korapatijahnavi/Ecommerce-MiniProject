package com.example.ecommerceminiproject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProductDetailViewModel extends ViewModel {
    private final ProductRepository repository =
            ProductRepository.getInstance();
    private final MutableLiveData<Resource<Product>> productState =
            new MutableLiveData<>();
    private int loadedProductId = -1;
    public LiveData<Resource<Product>> getProductState() {
        return productState;
    }
    public void loadProduct(int productId, boolean forceRefresh) {
        Resource<Product> current = productState.getValue();
        boolean alreadyLoaded =
                productId == loadedProductId
                        && current != null
                        && current.status != Resource.Status.ERROR;

        if (!forceRefresh && alreadyLoaded) {
            return;
        }

        loadedProductId = productId;
        productState.setValue(
                Resource.loading());

        repository.getProductById(
                productId,
                new RepositoryCallback<Product>() {
                    @Override
                    public void onSuccess(Product product) {
                        productState.setValue(
                                Resource.success(product)
                        );
                    }

                    @Override
                    public void onError(
                            String message,
                            int errorCode) {
                        productState.setValue(
                                Resource.error(
                                        message,
                                        errorCode
                                )
                        );
                    }
                }
        );
    }
}