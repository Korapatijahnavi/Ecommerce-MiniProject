package com.example.ecommerceminiproject;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecommerceminiproject.databinding.ActivityVerificationBinding;

public class VerificationActivity extends AppCompatActivity {
    private ActivityVerificationBinding binding;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        phone = getIntent().getStringExtra(Constants.EXTRA_MOBILE);
        setupOtpFields();
        setupClickListeners();
        binding.edt1.requestFocus();
    }

    private void setupOtpFields() {
        EditText[] otpFields = {
                binding.edt1,
                binding.edt2,
                binding.edt3,
                binding.edt4
        };

        for (int i = 0; i < otpFields.length; i++) {
            EditText currentField = otpFields[i];
            final int currentIndex = i;
            currentField.setOnKeyListener(
                    (v, keyCode, event) -> {
                        if (keyCode ==
                                KeyEvent.KEYCODE_DEL
                                && event.getAction() ==
                                KeyEvent.ACTION_DOWN) {

                            if (currentField
                                    .getText()
                                    .toString()
                                    .isEmpty()
                                    && currentIndex > 0) {

                                otpFields[currentIndex - 1].requestFocus();

                                otpFields[currentIndex - 1].setSelection(
                                        otpFields[currentIndex - 1].length()
                                );
                            }
                        }
                        return false;
                    }
            );

            currentField.addTextChangedListener(
                    new android.text.TextWatcher() {
                        @Override
                        public void beforeTextChanged(
                                CharSequence s,
                                int start,
                                int count,
                                int after) {
                        }
                        @Override
                        public void onTextChanged(
                                CharSequence s,
                                int start,
                                int before,
                                int count) {

                            if (s.length() == 1 && currentIndex <
                                    otpFields.length - 1) {
                                otpFields[currentIndex + 1].requestFocus();
                            }
                        }
                        @Override
                        public void afterTextChanged(
                                android.text.Editable s) {
                        }
                    }
            );
        }
    }

    private void setupClickListeners() {
        binding.backarrow.setOnClickListener(v -> {
            finish();
        });
        binding.btnlogin.setOnClickListener(v -> {
            String otp1 =
                    binding.edt1.getText().toString().trim();

            String otp2 =
                    binding.edt2.getText().toString().trim();

            String otp3 =
                    binding.edt3.getText().toString().trim();

            String otp4 =
                    binding.edt4.getText().toString().trim();

            String enteredOtp = otp1 + otp2 + otp3 + otp4;

            if (enteredOtp.isEmpty()) {
                Toast.makeText(
                        VerificationActivity.this,
                        "Please enter the OTP",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            if (enteredOtp.length() != 4) {
                Toast.makeText(
                        VerificationActivity.this,
                        "Please enter the 4-digit OTP",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            if (!Constants.STATIC_OTP.equals(
                    enteredOtp)) {
                Toast.makeText(
                        VerificationActivity.this,
                        "Incorrect OTP. Please try again.",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            SessionManager sessionManager = new SessionManager(VerificationActivity.this);
            sessionManager.saveLogin(phone);
            Toast.makeText(this, "Otp verified Successfully",Toast.LENGTH_SHORT).show();
            Intent intent =
                    new Intent(
                            VerificationActivity.this,
                            HomeActivity.class);
            intent.putExtra(
                    Constants.EXTRA_MOBILE,
                    phone);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
