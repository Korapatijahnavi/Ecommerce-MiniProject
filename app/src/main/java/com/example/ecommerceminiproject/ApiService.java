package com.example.ecommerceminiproject;
import com.example.ecommerceminiproject.Product;
import com.example.ecommerceminiproject.ProductResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface ApiService {
    @GET("products")
    Call<ProductResponse> getProducts(@Query("limit") int limit);

    @GET("products/category/{category}")
    Call<ProductResponse> getProductsByCategory(@Path("category") String category,
                                                @Query("limit") int limit);

    @GET("products/{id}")
    Call<Product> getProductById(@Path("id") int id);
}
