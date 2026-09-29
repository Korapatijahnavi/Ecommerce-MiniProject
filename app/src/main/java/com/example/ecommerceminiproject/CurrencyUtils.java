package com.example.ecommerceminiproject;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtils {
    private static final Locale INDIA = new Locale("en", "IN");
    private CurrencyUtils() {
    }
    public static long usdToInr(double usd) {
        return Math.round(usd * Constants.USD_TO_INR);
    }
    public static String format(long rupees) {
        return NumberFormat.getNumberInstance(INDIA).format(rupees);
    }
}