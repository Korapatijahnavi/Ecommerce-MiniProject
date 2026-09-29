package com.example.ecommerceminiproject;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecommerceminiproject.databinding.ActivityPaymentSuccessfullBinding;
import java.util.Random;

public class PaymentSuccessActivity extends AppCompatActivity {
    private ActivityPaymentSuccessfullBinding binding;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable goHome = this::returnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentSuccessfullBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportFragmentManager().findFragmentById(R.id.bottom_nav_container) == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.bottom_nav_container, new BottomNavigationFragment())
                    .commit();
        }

        long amount = getIntent().getLongExtra(Constants.EXTRA_ORDER_AMOUNT, 0L);
        String method = getIntent().getStringExtra(Constants.EXTRA_PAYMENT_METHOD);
        if (method == null) method = getString(R.string.pay_cash);
        String name = getIntent().getStringExtra(Constants.EXTRA_CUSTOMER_NAME);

        if (name != null && !name.isEmpty()) {
            binding.tvSuccessMessage.setText(getString(R.string.payment_thanks_name, name));
        }

        String orderId = "#" + (100000 + new Random().nextInt(900000));
        binding.tvOrderSummary.setText(getString(R.string.order_summary,
                orderId, CurrencyUtils.format(amount), method));

        binding.flSuccessIcon.setScaleX(0f);
        binding.flSuccessIcon.setScaleY(0f);
        binding.flSuccessIcon.animate().scaleX(1f).scaleY(1f).setDuration(450).start();

        binding.btnBack.setOnClickListener(v -> returnHome());
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                returnHome();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.postDelayed(goHome, Constants.PAYMENT_SUCCESS_DELAY_MS);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(goHome);
    }

    private void returnHome() {
        handler.removeCallbacks(goHome);
        SessionManager session = new SessionManager(this);
        Navigator.openHomeClearingBackStack(this, session.getPhone());
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(goHome);
        binding = null;
    }
}