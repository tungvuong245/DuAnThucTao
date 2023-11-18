package com.example.lasthope;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lasthope.databinding.ActivityLoginAdminBinding;

public class LoginAdminActivity extends AppCompatActivity {
    private ActivityLoginAdminBinding binding = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityLoginAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Window window = getWindow();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(getColor(R.color.brown_120));

        binding.icBack.setOnClickListener(v->{
            startActivity(new Intent(this,LoginActivity.class));
        });


        binding.btnLogin.setOnClickListener(v->{
            startActivity(new Intent(this, AdminActivity.class));
            if(binding.email.getText().toString().equals("admin245")&& binding.password.getText().toString().equals("tung2452003")){
//                startActivity(new Intent(this, AdminActivity.class));
                Toast.makeText(this, "login", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "sai thong tin", Toast.LENGTH_SHORT).show();
            }
        });

    }
}