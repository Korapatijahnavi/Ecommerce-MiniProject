package com.example.ecommerceminiproject;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
public class ApiErrorHandler {
    public static final int NO_HTTP_CODE=-1;
    private ApiErrorHandler(){
    }
    public static String messageForHttpCode(int code){
        switch (code){
            case 400:
                return "Bad request(400). The app sent an Invalid request";
            case 401:
                return "Unauthorized(401). Please log in Again";
            case 403:
                return "Forbidden(403). You dont have access to this data";
            case 404:
                return "Not found(404). This Product may no longer exist.";
            case 408:
                return "Request timed Out(408). Please check your connection.";
            case 429:
                return "Too many request(429). Please wait a moment and try again.";
            case 500:
                return "Server Error(500). Please try again later.";
            case 502:
                return "Bad gateway(502). the server is temporarily unreachable.";
            case 503:
                return "Service Unavailable(503). The server is busy, try again soon.";
            case 504:
                return "Gateway timeout(504). The server took too long to respond.";
            default:
                if(code>=500) return "Server Error(" + code +")";
                if(code>=400) return "request Failed (" + code + ")";
                return "Unexpected Response (" + code + ")";
        }
    }
    public static String messageForThrowable(Throwable t) {
        if (t instanceof SocketTimeoutException) return messageForHttpCode(408);
        if (t instanceof UnknownHostException || t instanceof ConnectException) {
            return "No internet connection. Please check your network and retry.";
        }
        if (t instanceof JsonParseException) return "We received data we couldn't read.";
        if (t instanceof IOException) return "Network error. Please retry.";
        return "Something unexpected happened: " + t.getClass().getSimpleName();
    }
}
