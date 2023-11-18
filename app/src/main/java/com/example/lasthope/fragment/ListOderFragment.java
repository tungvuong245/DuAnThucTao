package com.example.lasthope.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.lasthope.Adapter.ListOderAdapter;
import com.example.lasthope.base.BaseFragment;
import com.example.lasthope.databinding.FragmentListOderBinding;
import com.example.lasthope.model.Receipt;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListOderFragment extends BaseFragment implements ListOderAdapter.OnClickListener {
    private FragmentListOderBinding binding = null;
    private ArrayList<Receipt> listCancelReceipt;
    private ArrayList<Receipt> listReceipt;
    private ListOderAdapter adapter ;
    private int isChangedLayout = 0;
    public ListOderFragment(){

    }
    public static ListOderFragment newInstance() {
        ListOderFragment fragment = new ListOderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding = FragmentListOderBinding.inflate(inflater,container,false);
       return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listening();
    }

    @Override
    public void loadData() {
        listReceipt = new ArrayList<>();
        listCancelReceipt = new ArrayList<>();
        getAllReceipt();
        adapter= new ListOderAdapter(listReceipt,ListOderFragment.this,isChangedLayout);
        LinearLayoutManager layoutManager  = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        binding.recListBill.setLayoutManager(layoutManager);
        binding.recListBill.setAdapter(adapter);

    }

    @Override
    public void listening() {

        binding.searchViewOder.clearFocus();
        binding.searchViewOder.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });

        binding.btnChangeLayoutHorizontal.setOnClickListener(btn ->{
            binding.btnChangeLayoutHorizontal.setVisibility(View.GONE);
            binding.btnChangeLayoutVertical.setVisibility(View.VISIBLE);
            isChangedLayout = 1;
            getAllReceipt();
            adapter= new ListOderAdapter(listReceipt,ListOderFragment.this,isChangedLayout);
            binding.recListBill.setAdapter(adapter);

        });
        binding.btnChangeLayoutVertical.setOnClickListener(btn ->{
            binding.btnChangeLayoutVertical.setVisibility(View.GONE);
            binding.btnChangeLayoutHorizontal.setVisibility(View.VISIBLE);
            isChangedLayout = 0;
            getAllReceipt();
            adapter= new ListOderAdapter(listReceipt,ListOderFragment.this,isChangedLayout);
            binding.recListBill.setAdapter(adapter);


        });

        binding.swiperRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllReceipt();
                binding.recListBill.setAdapter(adapter);
                binding.swiperRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public void initObSever() {

    }

    @Override
    public void initView() {

    }



    private void getAllReceipt(){
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("list_bill");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listReceipt.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Receipt receipt = dataSnapshot.getValue(Receipt.class);
                    listReceipt.add(receipt);
                }
                binding.tvNumberOfOder.setText(listReceipt.size()+" bill");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        DatabaseReference mRef2 = FirebaseDatabase.getInstance().getReference("_");
//        mRef2.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                listCancelReceipt.clear();
//                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
//                    Receipt receipt = dataSnapshot.getValue(Receipt.class);
//                    listCancelReceipt.add(receipt);
//                }
//                listReceipt.addAll(listCancelReceipt);
//                binding.tvNumberOfOder.setText(listReceipt.size()+" đơn");
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }




    @Override
    public void onClickListener(Receipt receipt) {


       replaceFragment(new DetailReceiptFragment(receipt));


    }
}
