package com.example.ecommerceminiproject;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecommerceminiproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final int SPLASH_DELAY = 3000;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable navigationRunnable=()->{
        SessionManager sessionManager=new SessionManager(MainActivity.this);
        if(sessionManager.isLoggedIn()){
            Intent intent= new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra(Constants.EXTRA_MOBILE, sessionManager.getPhone());
            startActivity(intent);
        }
        else{
            Intent intent= new Intent(MainActivity.this, IntroActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.iconmain.setOnClickListener(v -> {
            Intent ite = new Intent(MainActivity.this, IntroActivity.class);
            startActivity(ite);
        });
        handler.postDelayed(navigationRunnable, SPLASH_DELAY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(navigationRunnable);
    }
}