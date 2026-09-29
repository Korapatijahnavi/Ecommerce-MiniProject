package com.example.ecommerceminiproject;

public interface RepositoryCallback<T> {
    void onSuccess(T data);
    void onError(String message, int errorCode);
}
