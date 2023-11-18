package com.example.lasthope.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lasthope.Adapter.ListOderAdapter;
import com.example.lasthope.base.BaseFragment;
import com.example.lasthope.databinding.FragmentHistoryBinding;
import com.example.lasthope.model.Receipt;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryFragment extends BaseFragment implements ListOderAdapter.OnClickListener {
    private FragmentHistoryBinding binding = null ;
    private Receipt receipt;
    private ListOderAdapter adapter;
    private Double doanhthu = Double.valueOf(0);
    private  ArrayList<Receipt> listReceipt;
    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void loadData() {
        getReceipt();
        adapter= new ListOderAdapter(listReceipt,HistoryFragment.this);
        binding.rcvHistory.setAdapter(adapter);
    }

    @Override
    public void listening() {
    binding.icBack.setOnClickListener(v->backStack());
    }

    @Override
    public void initObSever() {

    }

    @Override
    public void initView() {

    }
    private void getReceipt(){
        String idU = FirebaseAuth.getInstance().getCurrentUser().getUid();
        listReceipt = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("list_bill");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listReceipt!=null){
                    listReceipt.clear();
                }

                for(DataSnapshot dataSnapshot :snapshot.getChildren()){
                    Receipt receipt = dataSnapshot.getValue(Receipt.class);
                    if(receipt.getIdU().equals(idU)){
                        listReceipt.add(receipt);
                    }
                    doanhthu = doanhthu+receipt.getMonney();
                }
                binding.tvPrice.setText(doanhthu+"");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClickListener(Receipt receipt) {

    }
}
