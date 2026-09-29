package com.example.ecommerceminiproject;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecommerceminiproject.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private static final int Valid = 0;
    private static final int numberempty = 1;
    private static final int validNumber = 2;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionManager = new SessionManager(this);
        String msg = getString(R.string.login_2);
        binding.tvterms.setText(msg);
        binding.etmobile.setInputType(InputType.TYPE_CLASS_PHONE);
        binding.btnlogin.setOnClickListener(v -> {
            String number =
                    binding.etmobile.getText()
                            .toString()
                            .trim();
            binding.etmobile.setError(null);
            switch (ValidateInput(number)) {
                case numberempty:
                    binding.etmobile.setError("Enter the number");
                    break;

                case validNumber:
                    binding.etmobile.setError("Enter a valid number");
                    break;

                case Valid:
                    Intent intent =
                            new Intent(
                                    LoginActivity.this,
                                    VerificationActivity.class);
                    intent.putExtra(Constants.EXTRA_MOBILE, number);
                    startActivity(intent);
                    finish();
                    break;
            }
        });
    }

    private int ValidateInput(String number) {
        if (number.isEmpty()) {
            return numberempty;
        }
        if (number.length() < 10) {
            return validNumber;
        }
        return Valid;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
