package com.example.lasthope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.example.lasthope.databinding.ActivityMainBinding;
import com.example.lasthope.fragment.AddProductToOderFragment;
import com.example.lasthope.fragment.HomeFragment;
import com.example.lasthope.fragment.MarketFragment;
import com.example.lasthope.fragment.ProductFragment;
import com.example.lasthope.setting.SettingFragment;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
                Window window = getWindow();
                getSupportFragmentManager().beginTransaction().add(R.id.fade_control, HomeFragment.newInstance()).commit();
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                window.setStatusBarColor(getColor(R.color.white));
            binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.home_fragment) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fade_control, HomeFragment.newInstance()).commit();
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                        window.setStatusBarColor(getColor(R.color.white));
                    } else if (id == R.id.maket_fragment) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fade_control, AddProductToOderFragment.newInstance()).commit();
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                        window.setStatusBarColor(getColor(R.color.white));
                    }
//                    else if (id == R.id.menu_fragment) {
//                        getSupportFragmentManager().beginTransaction().replace(R.id.fade_control, ProductFragment.newInstance()).commit();
//                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                        window.setStatusBarColor(getColor(R.color.white));
//                    }
                    else if (id == R.id.setting_fragment) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fade_control, SettingFragment.newInstance()).commit();
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                        window.setStatusBarColor(getColor(R.color.brown_120));
                    }
                    return true;
                }
            });
        }
    }