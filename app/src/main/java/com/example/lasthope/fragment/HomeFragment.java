package com.example.lasthope.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.lasthope.Adapter.BannerAdapter;
import com.example.lasthope.R;
import com.example.lasthope.base.BaseFragment;
import com.example.lasthope.base.OnclickOptionMenu;
import com.example.lasthope.databinding.FragmentHomeBinding;
import com.example.lasthope.model.Product;
import com.example.lasthope.model.Receipt;
import com.example.lasthope.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class HomeFragment extends BaseFragment implements OnclickOptionMenu {
   private FragmentHomeBinding binding;
    private User user;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private List<Product> listProduct ;
    private FirebaseDatabase database;
    private BannerAdapter adapter ;
    private Receipt receipt;
    private Double doanhthu = Double.valueOf(0);
    private  ArrayList<Receipt> listReceipt;
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
        binding = FragmentHomeBinding.inflate(inflater,container,false);

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(ListOderFragment.newInstance());
            }
        }

        );
        binding.btnProduct.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("replace", "onClick: " );
                        replaceFragment(ProductFragment.newInstance());
                    }
                }
        );
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        StorageReference reference = FirebaseStorage.getInstance().getReference().child("avatars");
        reference.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference files: listResult.getItems()
            ) {
                if (files.getName().equals(firebaseUser.getUid())){
                    files.getDownloadUrl().addOnSuccessListener(uri -> {
                        if(getActivity() != null){
                            Glide.with(getActivity()).load(uri).into(binding.icUserSetting);
                        }

                    });
                }
            }
        }).addOnFailureListener(e -> {

        });
        loadData();
        listening();
        initObSever();
    }

    @Override
    public void loadData() {
        user = new User();
        firebaseUser = firebaseAuth.getCurrentUser();
        String userID = firebaseUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                if (firebaseUser != null) {
                    binding.tvName.setText(user.getName_user());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        Date toDay = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strToday = dateFormat.format(toDay);
        getAllProduct();

    }

    @Override
    public void listening() {
        binding.icCloseSlide.setOnClickListener(ic ->{
            binding.layoutSlide.setVisibility(View.GONE);

        });

        selectTabFragment();

    }

    @Override
    public void initObSever() {

    }

    @Override
    public void initView() {

        binding.tvTitleAll.setBackgroundColor(getContext().getColor(R.color.red_100));
        getAllProduct();
    }



    private void selectTabFragment(){

        binding.btnAllTable.setOnClickListener(btn ->{
            changeBgColorTextView(binding.tvTitleAll,getContext().getColor(R.color.red_100));
            getAllProduct();
        });
    }


    private void changeBgColorTextView( TextView tv ,int idColor){
        tv.setBackgroundColor(idColor);
    }


    private void getAllProduct(){
        listProduct = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("list_product");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listProduct.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    Product product = snapshot1.getValue(Product.class);
                    if(product.isHidden()){
                        listProduct.add(product);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new BannerAdapter(listProduct,HomeFragment.this,getContext());
        binding.revListTable.setAdapter(adapter);

    }




    @Override
    public void onClick(Product product) {
        replaceFragment(new AddToCartFragment(product));
    }



}