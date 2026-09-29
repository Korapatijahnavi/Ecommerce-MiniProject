package com.example.ecommerceminiproject;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import com.example.ecommerceminiproject.databinding.ActivityDeliveryBinding;

public class DeliveryActivity extends AppCompatActivity {

    private ActivityDeliveryBinding binding;
    private CartViewModel cartViewModel;
    private String addressType;
    private String paymentMethod;
    private boolean orderPlaced;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeliveryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupKeyboardHandling();

        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        if (getSupportFragmentManager().findFragmentById(R.id.bottom_nav_container) == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.bottom_nav_container, new BottomNavigationFragment())
                    .commit();
        }

        binding.layoutTabs.tabCartItems.setSelected(false);
        binding.layoutTabs.tabDelivery.setSelected(true);
        binding.layoutTabs.tabCartItems.setOnClickListener(v -> backToCart());
        binding.btnBack.setOnClickListener(v -> backToCart());
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                backToCart();
            }
        });

        String savedPhone = new SessionManager(this).getPhone();
        if (savedPhone != null && !savedPhone.isEmpty()) binding.etMobile.setText(savedPhone);

        binding.btnTypeOffice.setOnClickListener(v -> selectAddressType(getString(R.string.address_office)));
        binding.btnTypeHome.setOnClickListener(v -> selectAddressType(getString(R.string.address_home)));
        selectAddressType(getString(R.string.address_home));

        binding.btnPayCash.setOnClickListener(v -> selectPayment(getString(R.string.pay_cash)));
        binding.btnPayUpi.setOnClickListener(v -> selectPayment(getString(R.string.pay_upi)));
        selectPayment(getString(R.string.pay_cash));

        binding.btnPlaceOrder.setOnClickListener(v -> placeOrder());

        observeCart();
    }

    private void setupKeyboardHandling() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (root, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            boolean keyboardOpen = insets.isVisible(WindowInsetsCompat.Type.ime());

            root.setPadding(bars.left, bars.top, bars.right, Math.max(bars.bottom, ime.bottom));
            binding.bottomNavContainer.setVisibility(keyboardOpen ? View.GONE : View.VISIBLE);

            if (keyboardOpen) {
                View focused = getCurrentFocus();
                if (focused != null) {
                    focused.post(() -> {
                        Rect rect = new Rect();
                        focused.getDrawingRect(rect);
                        focused.requestRectangleOnScreen(rect, false);
                    });
                }
            }
            return insets;
        });
    }

    private void observeCart() {
        cartViewModel.getCartItems().observe(this, items -> {
            if (orderPlaced) return;
            if (items == null || items.isEmpty()) {
                Toast.makeText(this, R.string.cart_empty_toast, Toast.LENGTH_SHORT).show();
                backToCart();
            }
        });

        cartViewModel.getCartTotal().observe(this, total -> {
            long value = total == null ? 0 : total;
            binding.tvDeliveryTotal.setText(CurrencyUtils.format(value));
            long remaining = Constants.FREE_DELIVERY_THRESHOLD_INR - value;
            if (remaining <= 0) {
                binding.tvDeliveryHint.setText(R.string.free_delivery_unlocked);
            } else {
                binding.tvDeliveryHint.setText(getString(R.string.free_delivery_remaining,
                        CurrencyUtils.format(remaining)));
            }
        });
    }

    private void selectAddressType(String type) {
        addressType = type;
        boolean office = type.equals(getString(R.string.address_office));
        binding.btnTypeOffice.setSelected(office);
        binding.btnTypeHome.setSelected(!office);
    }

    private void selectPayment(String method) {
        paymentMethod = method;
        boolean upi = method.equals(getString(R.string.pay_upi));
        binding.btnPayCash.setSelected(!upi);
        binding.btnPayUpi.setSelected(upi);
        binding.tvUpiLabel.setVisibility(upi ? View.VISIBLE : View.GONE);
        binding.llUpi.setVisibility(upi ? View.VISIBLE : View.GONE);
        if (!upi) binding.etUpi.setError(null);
    }

    private void placeOrder() {
        String name = textOf(binding.etName);
        String mobile = textOf(binding.etMobile);
        String address = textOf(binding.etAddress);
        String upiId = textOf(binding.etUpi);

        EditText firstInvalid = null;

        String nameError = validateName(name);
        binding.etName.setError(nameError);
        if (nameError != null) firstInvalid = binding.etName;

        String mobileError = validateMobile(mobile);
        binding.etMobile.setError(mobileError);
        if (mobileError != null && firstInvalid == null) firstInvalid = binding.etMobile;

        String addressError = validateAddress(address);
        binding.etAddress.setError(addressError);
        if (addressError != null && firstInvalid == null) firstInvalid = binding.etAddress;

        if (paymentMethod.equals(getString(R.string.pay_upi))) {
            String upiError = validateUpi(upiId);
            binding.etUpi.setError(upiError);
            if (upiError != null && firstInvalid == null) firstInvalid = binding.etUpi;
        }

        if (firstInvalid != null) {
            firstInvalid.requestFocus();
            Toast.makeText(this, R.string.error_form, Toast.LENGTH_SHORT).show();
            return;
        }

        if (cartViewModel.isCartEmpty()) {
            Toast.makeText(this, R.string.cart_empty_toast, Toast.LENGTH_SHORT).show();
            return;
        }

        long total = cartViewModel.getTotalNow();
        orderPlaced = true;
        cartViewModel.clearCart();
        Navigator.openPaymentSuccess(this, total, paymentMethod, name);
        finish();
    }

    private String validateName(String name) {
        if (name.isEmpty()) return getString(R.string.error_name_empty);
        if (!name.matches("[a-zA-Z ]+")) return getString(R.string.error_name_invalid);
        return null;
    }

    private String validateMobile(String mobile) {
        if (mobile.isEmpty()) return getString(R.string.error_mobile_empty);
        if (!mobile.matches("[6-9][0-9]{9}")) return getString(R.string.error_mobile_invalid);
        return null;
    }

    private String validateAddress(String address) {
        if (address.isEmpty()) return getString(R.string.error_address_empty);
        if (address.length() < 10) return getString(R.string.error_address_short);
        return null;
    }

    private String validateUpi(String upiId) {
        if (upiId.isEmpty()) return getString(R.string.error_upi_empty);
        if (!upiId.matches("[a-zA-Z0-9._-]{2,}@[a-zA-Z]{2,}")) return getString(R.string.error_upi_invalid);
        return null;
    }

    private String textOf(EditText field) {
        return field.getText() == null ? "" : field.getText().toString().trim();
    }

    private void backToCart() {
        Navigator.openCart(this);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}