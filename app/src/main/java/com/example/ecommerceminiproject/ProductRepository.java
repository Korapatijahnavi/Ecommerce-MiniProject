package com.example.ecommerceminiproject;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {
    private static final String TAG = "ProductRepository";
    private static ProductRepository instance;
    private final ApiService apiservice;
    private List<Product> Cachedproducts;
    private ProductRepository() {
        apiservice = RetrofitClient.getApiservice();
    }
    public static synchronized ProductRepository getInstance() {

        if (instance == null) {
            instance = new ProductRepository();
        }

        return instance;
    }

    public void getAllProducts(
            boolean forceRefresh,
            RepositoryCallback<List<Product>> callback
    ) {

        if (!forceRefresh && Cachedproducts != null) {
            Log.d(
                    TAG,
                    "Returning products from cache: "
                            + Cachedproducts.size()
            );
            callback.onSuccess(Cachedproducts);
            return;
        }
        Log.d(
                TAG,
                "Calling DummyJSON /products API"
        );
        apiservice
                .getProductsByCategory(
                        Constants.SHOP_CATEGORY,
                        Constants.FETCH_ALL_PRODUCTS
                )
                .enqueue(new Callback<ProductResponse>() {

                    @Override
                    public void onResponse(
                            @NonNull Call<ProductResponse> call,
                            @NonNull Response<ProductResponse> response
                    ) {
                        Log.d(
                                TAG,
                                "HTTP response code = "
                                        + response.code()
                        );

                        if (response.isSuccessful()
                                && response.body() != null) {

                            List<Product> products =
                                    response.body().getProducts();

                            for (Product product : products) {
                                GroceryCategorizer.assignCategory(product);
                            }

                            Cachedproducts = products;

                            callback.onSuccess(Cachedproducts);

                        } else {

                            callback.onError(
                                    ApiErrorHandler.messageForHttpCode(
                                            response.code()
                                    ),
                                    response.code()
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            @NonNull Call<ProductResponse> call,
                            @NonNull Throwable t
                    ) {

                        Log.e(
                                TAG,
                                "Loading products failed",
                                t
                        );

                        callback.onError(
                                ApiErrorHandler.messageForThrowable(t),
                                ApiErrorHandler.NO_HTTP_CODE
                        );
                    }
                });
    }

    public void getProductById(
            int id,
            RepositoryCallback<Product> callback
    ) {

        apiservice
                .getProductById(id)
                .enqueue(new Callback<Product>() {

                    @Override
                    public void onResponse(
                            @NonNull Call<Product> call,
                            @NonNull Response<Product> response
                    ) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            Product product =
                                    response.body();

                            GroceryCategorizer.assignCategory(product);

                            callback.onSuccess(product);

                        } else {

                            callback.onError(
                                    ApiErrorHandler.messageForHttpCode(
                                            response.code()
                                    ),
                                    response.code()
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            @NonNull Call<Product> call,
                            @NonNull Throwable t
                    ) {

                        Log.e(
                                TAG,
                                "Loading product failed",
                                t
                        );

                        callback.onError(
                                ApiErrorHandler.messageForThrowable(t),
                                ApiErrorHandler.NO_HTTP_CODE
                        );
                    }
                });
    }
}