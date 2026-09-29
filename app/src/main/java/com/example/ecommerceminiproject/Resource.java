package com.example.ecommerceminiproject;

public class Resource<T> {
    public enum Status {
        LOADING,
        SUCCESS,
        ERROR,
        EMPTY
    }

    public Status status;
    public T data;
    public String message;
    public int errorCode;

    private Resource(
            Status status,
            T data,
            String message, int errorCode) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.errorCode=errorCode;
    }

    public static <T> Resource<T> loading() {

        return new Resource<>(
                Status.LOADING,
                null,
                null, 0
        );
    }

    public static <T> Resource<T> success(T data) {

        return new Resource<>(
                Status.SUCCESS,
                data,
                null,0
        );
    }

    public static <T> Resource<T> error(String message, int errorCode) {

        return new Resource<>(
                Status.ERROR,
                null,
                message,errorCode
        );
    }

    public static <T> Resource<T> empty() {
        return new Resource<>(
                Status.EMPTY,
                null,
                null,0
        );
    }
}