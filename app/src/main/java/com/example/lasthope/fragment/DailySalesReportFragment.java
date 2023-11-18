package com.example.lasthope.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lasthope.Adapter.ListOderAdapter;
import com.example.lasthope.R;
import com.example.lasthope.base.BaseFragment;
import com.example.lasthope.databinding.FragmentSalesReportBinding;
import com.example.lasthope.model.Receipt;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DailySalesReportFragment extends BaseFragment implements ListOderAdapter.OnClickListener{

    private FragmentSalesReportBinding binding = null;
    private Receipt receipt;
    private Double doanhthu = Double.valueOf(0);
    private ArrayList<Receipt> listReceipt;
    private ListOderAdapter adapter;

    public DailySalesReportFragment() {
        // Required empty public constructor
    }

    public static DailySalesReportFragment newInstance() {
        DailySalesReportFragment fragment = new DailySalesReportFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSalesReportBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Window window = getActivity().getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(getActivity().getColor(R.color.white));

        listening();
        initObSever();
        loadData();
    }

    @Override
    public void loadData() {
        listReceipt = new ArrayList<>();
        Date toDay = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strToday = dateFormat.format(toDay);
        binding.tvDate.setText(strToday);
        getReceipt(strToday);
        adapter= new ListOderAdapter(listReceipt,DailySalesReportFragment.this);
        binding.recVListOder.setAdapter(adapter);
    }

    @Override
    public void listening() {
        binding.icBack.setOnClickListener(ic ->{
            backStack();
        });
    }

    @Override
    public void initObSever() {
    }

    @Override
    public void initView() {


    }
    private void getReceipt(String date){
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
                    String strDay = receipt.getTime().substring(0, receipt.getTime().lastIndexOf(" "));

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date dayOder = dateFormat.parse(strDay);
                        Date toDay = dateFormat.parse(date);
                        if (toDay.getTime() == dayOder.getTime()) {
                            doanhthu= doanhthu+receipt.getMonney();


                            listReceipt.add(receipt);

                        }
                    }catch (ParseException e){
                        e.printStackTrace();
                    }

                }
                binding.tvTotalOderValue.setText(doanhthu+"");
                binding.tvOrderNumber.setText(listReceipt.size()+"");
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onClickListener(Receipt receipt) {
        replaceFragment(new DetailReceiptFragment(receipt));
    }
}