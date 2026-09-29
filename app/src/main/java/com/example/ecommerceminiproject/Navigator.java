package com.example.ecommerceminiproject;
import android.content.Context;
import android.content.Intent;
public class Navigator {
    private Navigator() {
    }

    public static void openHome(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);
    }

    public static void openHomeClearingBackStack(Context context, String mobile) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra(Constants.EXTRA_MOBILE, mobile);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void openProducts(Context context, String categorySlug, boolean focusSearch) {
        Intent intent = new Intent(context, ProductActivity.class);
        if (categorySlug != null) intent.putExtra(Constants.EXTRA_CATEGORY, categorySlug);
        intent.putExtra(Constants.EXTRA_FOCUS_SEARCH, focusSearch);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);
    }

    public static void openProductDetail(Context context, int productId) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra(Constants.EXTRA_PRODUCT_ID, productId);
        context.startActivity(intent);
    }

    public static void openCart(Context context) {
        Intent intent = new Intent(context, CartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);
    }

    public static void openDelivery(Context context)
    {
        Intent intent= new Intent(context,DeliveryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);
    }
    public static void openPaymentSuccess(Context context, long amount,
                                           String paymentmethod, String customername)
    {
        Intent intent= new Intent(context, PaymentSuccessActivity.class);
        intent.putExtra(Constants.EXTRA_ORDER_AMOUNT,amount);
        intent.putExtra(Constants.EXTRA_PAYMENT_METHOD,paymentmethod);
        intent.putExtra(Constants.EXTRA_CUSTOMER_NAME,customername);
        context.startActivity(intent);
    }
}