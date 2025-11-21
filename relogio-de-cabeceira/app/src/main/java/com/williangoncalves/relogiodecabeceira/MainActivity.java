package com.williangoncalves.relogiodecabeceira;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.williangoncalves.relogiodecabeceira.databinding.ActivityMainBinding;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;
    private final String TAG = "MainActivity";
    private final Handler handler = new Handler();
    private Runnable runnable;
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

        setFlags();

        setListeners();
        hideOptions();
        startBedsideCLock();
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
            showOptions();
        } else if (view.getId() == R.id.imageview_close) {
            hideOptions();
        }
    }

    private void startBedsideCLock() {

        runnable = new Runnable() {
            @Override
            public void run() {

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);

                String hourMinuteFormat = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                String secondFormat = String.format(Locale.getDefault(), "%02d", second);

                binding.textviewHourMinute.setText(hourMinuteFormat);
                binding.textviewSeconds.setText(secondFormat);

                long now = SystemClock.uptimeMillis();
                Log.d(TAG, "Now: " + now);
                handler.postAtTime(runnable, now + (1000 - (now % 1000)));
                Log.d(TAG, "Now calculo: " + (now + (1000 - (now % 1000))));
            }
        };
        runnable.run();
    }

    private void toggleBatteryLevel() {
        boolean isVisible = binding.textviewBatteryLevel.getVisibility() == View.VISIBLE;
        binding.textviewBatteryLevel.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    }

    private void setFlags() {
        // Impede o bloqueio da tela automaticamente:
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // App ocupa toda a extensão da tela:
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void setListeners() {
        binding.checkboxBatteryLevel.setOnClickListener(this);
        binding.imageviewSettings.setOnClickListener(this);
        binding.imageviewClose.setOnClickListener(this);
    }

    private void hideOptions() {
        int duration = 400;

        binding.checkboxBatteryLevel.post(new Runnable() {
            @Override
            public void run() {
                int heightCheckbox = binding.checkboxBatteryLevel.getHeight();
                binding.checkboxBatteryLevel.animate().translationY(heightCheckbox).setDuration(duration);
            }
        });

        binding.imageviewClose.post(new Runnable() {
            @Override
            public void run() {
                int heightImageClose = binding.imageviewClose.getHeight();
                binding.imageviewClose.animate().translationY(heightImageClose).setDuration(duration);
            }
        });
    }

    private void showOptions() {
        int duration = 400;
        binding.checkboxBatteryLevel.animate().translationY(0).setDuration(duration);
        binding.imageviewClose.animate().translationY(0).setDuration(duration);
    }
}