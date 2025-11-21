package com.williangoncalves.relogiodecabeceira;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.williangoncalves.relogiodecabeceira.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;
    private final BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            binding.textviewBatteryLevel.setText(getString(R.string.battery_level, batteryLevel));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        // Impede o bloqueio da tela automaticamente:
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // App ocupa toda a extensão da tela:
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setListeners();
        hideOptions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(batteryReceiver);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.checkbox_battery_level) {
            toggleBatteryLevel();
        } else if (view.getId() == R.id.imageview_settings) {

        } else if (view.getId() == R.id.imageview_close) {

        }
    }

    private void toggleBatteryLevel() {
        boolean isVisible = binding.textviewBatteryLevel.getVisibility() == View.VISIBLE;
        binding.textviewBatteryLevel.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    }

    private void setListeners() {
        binding.checkboxBatteryLevel.setOnClickListener(this);
        binding.imageviewSettings.setOnClickListener(this);
        binding.imageviewClose.setOnClickListener(this);
    }

    private void hideOptions() {

    }
}