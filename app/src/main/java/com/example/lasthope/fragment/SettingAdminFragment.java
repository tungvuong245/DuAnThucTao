package com.example.lasthope.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lasthope.LoginActivity;
import com.example.lasthope.base.BaseFragment;
import com.example.lasthope.databinding.FragmentSettingAdminBinding;

public class SettingAdminFragment extends BaseFragment {
    FragmentSettingAdminBinding binding = null;

    public static SettingAdminFragment newInstance() {
        SettingAdminFragment fragment = new SettingAdminFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding =FragmentSettingAdminBinding.inflate(inflater,container,false);
        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void loadData() {
        binding.tvSalesReport.setOnClickListener(v -> {
            replaceFragment(DailySalesReportFragment.newInstance());
        });

        binding.icSalesReport.setOnClickListener(v -> {
            replaceFragment(DailySalesReportFragment.newInstance());
        });
        binding.icNextSalesReport.setOnClickListener(v -> {
            replaceFragment(DailySalesReportFragment.newInstance());
        });


        binding.tvOrderStatistics.setOnClickListener(v -> {
            replaceFragment(StatisticalFragment.newInstance());
        });

        binding.icOrderStatistics.setOnClickListener(v -> {
            replaceFragment(StatisticalFragment.newInstance());
        });
        binding.icNextOrderStatistics.setOnClickListener(v -> {
            replaceFragment(StatisticalFragment.newInstance());
        });
        binding.btnLogout.setOnClickListener(v->{
            startActivity(new Intent(getContext(), LoginActivity.class));
        });
        binding.btnChatToUser.setOnClickListener(v->{
            replaceFragment(FragmentListChatUser.newInstance());
        });

    }

    @Override
    public void listening() {

    }

    @Override
    public void initObSever() {

    }

    @Override
    public void initView() {

    }
}
