package com.example.ecommerceminiproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerceminiproject.databinding.ActivityIntroBinding;

public class IntroActivity extends AppCompatActivity {
    private ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityIntroBinding.inflate(
                getLayoutInflater());
        setContentView(binding.getRoot());
        String msg =
                getString(R.string.intro_1);
        binding.tvintro1.setText(msg);
        String msg2 =
                getString(R.string.intro_2);

        binding.tvintro2.setText(msg2);
        binding.introshopnow.setOnClickListener(v -> {
            Intent intent =
                    new Intent(
                            IntroActivity.this,
                            LoginActivity.class);
            startActivity(intent);
        });
    }
}