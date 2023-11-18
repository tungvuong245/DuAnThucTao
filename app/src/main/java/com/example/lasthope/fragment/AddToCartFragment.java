package com.example.lasthope.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.lasthope.base.BaseFragment;
import com.example.lasthope.databinding.FragmentAddProductBinding;
import com.example.lasthope.databinding.FragmentAddToCartBinding;
import com.example.lasthope.model.Product;
import com.example.lasthope.model.ProductToOder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddToCartFragment extends BaseFragment {


    FragmentAddToCartBinding binding = null;
    private Product dataProduct = null;
    private int soluong = 1 ;
    public  AddToCartFragment (Product product){
        this.dataProduct = product;

    }
    public AddToCartFragment newInstance() {

        return new AddToCartFragment(dataProduct);
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
        binding = FragmentAddToCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnBack.setOnClickListener(v->{
            backStack();
        });

    }

    @Override
    public void loadData() {
        StorageReference reference = FirebaseStorage.getInstance().getReference().child("imgProducts");
        reference.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference files: listResult.getItems()){
                if(files.getName().equals(dataProduct.getId())){
                    files.getDownloadUrl().addOnSuccessListener(uri -> {
                        Glide.with(getContext()).load(uri).into(binding.imgProduct);
                    });
                }
            }
        });
        binding.tvNameProduct.setText(dataProduct.getNameProduct());
        binding.tvDescription.setText(dataProduct.getDescribe());
        binding.tvSoluong.setText(soluong+"");
        binding.tvPrice.setText(dataProduct.getPrice()+"");
        if(soluong>=0){
            binding.btnGiam.setOnClickListener(v->{
                soluong--;
                if(soluong>=0){
                    binding.tvSoluong.setText(soluong+"");
                    double price = soluong*dataProduct.getPrice();
                    binding.tvPrice.setText(price+"");
                }else{
                    soluong=0;
                }

            });
        }
        binding.btnThem.setOnClickListener(v->{
            if(dataProduct.getSoLuong()>soluong){
                soluong++;
                binding.tvSoluong.setText(soluong+"");
                double price = soluong*dataProduct.getPrice();
                binding.tvPrice.setText(price+"");
            }else {
                Toast.makeText(getContext(), "San pham " +dataProduct.getNameProduct()+" khong du so luong", Toast.LENGTH_SHORT).show();
            }

        });
        String idU = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        binding.btnAddToCart.setOnClickListener(v->{
            DatabaseReference mref = FirebaseDatabase.getInstance().getReference("list_oder/"+idU);
            String key =  mref.push().getKey();
            ProductToOder product = new ProductToOder(key,dataProduct.getId(),dataProduct.getNameProduct(),dataProduct.getPrice(),soluong,dataProduct.getTypeProduct(),dataProduct.getNote(),strDate);
            mref.child(key).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    replaceFragment(HomeFragment.newInstance());
                    Log.e("TAG", "onComplete: it ok" );
                }
            });
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
