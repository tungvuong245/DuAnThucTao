package com.example.lasthope.Adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lasthope.base.OnClickCart;
import com.example.lasthope.databinding.LayoutItemDetailOderProductBinding;
import com.example.lasthope.model.Product;
import com.example.lasthope.model.ProductToOder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DetailProductToOderAdapter extends RecyclerView.Adapter<DetailProductToOderAdapter.ViewHolderProduct> {
    private ArrayList<ProductToOder> listProduct;
    private int soluong ;
    Context context;
    OnClickCart onClickCart;

    public DetailProductToOderAdapter(ArrayList<ProductToOder> listProduct , Context context,OnClickCart onClickCart) {
     this.listProduct= listProduct;
     this.context = context;
     this.onClickCart = onClickCart;
    }



    public void setFilterList(ArrayList<ProductToOder> filterList) {
        this.listProduct = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderProduct onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderProduct(LayoutItemDetailOderProductBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderProduct holder, int position) {
        ProductToOder product = listProduct.get(position);
        if (product == null) {
            return;
        } else {
            holder.initData(product, context,listProduct,position);
        }

    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class ViewHolderProduct extends RecyclerView.ViewHolder {
        ImageView imgProduct,imgAddToOder;
        ImageView btnThem,btnGiam;
        TextView tvName, tvDescribe, tvPrice,tv_soluong;
        ConstraintLayout layoutItem;
        public ViewHolderProduct(LayoutItemDetailOderProductBinding binding) {
            super(binding.getRoot());
            imgProduct = binding.imgProduct;
            tvName = binding.tvNameProduct;
            tvDescribe = binding.tvDescribeProduct;
            tvPrice = binding.tvPriceProduct;
            layoutItem = binding.layoutItem;
            tv_soluong = binding.tvSoLuong;
            btnThem = binding.btnThem;
            btnGiam = binding.btnGiam;

        }

        void initData(ProductToOder product, Context context,ArrayList<ProductToOder>list,int position) {
            DatabaseReference referenceProduct = FirebaseDatabase.getInstance().getReference("list_product");
            referenceProduct.child(product.getIdP()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Product product1 = snapshot.getValue(Product.class);
                    soluong= product.getSoLuong() ;
                    StorageReference reference = FirebaseStorage.getInstance().getReference().child("imgProducts");
                    reference.listAll().addOnSuccessListener(listResult -> {
                        for (StorageReference files: listResult.getItems()){
                            if(files.getName().equals(product.getIdP())){
                                files.getDownloadUrl().addOnSuccessListener(uri -> {
                                    Log.e("Load anh", "initData: " );
                                    Glide.with(context).load(uri).into(imgProduct);
                                });
                            }
                        }
                    });
                    tvName.setText(product.getNameProduct());
                    Locale locale = new Locale("en","EN");
                    NumberFormat numberFormat = NumberFormat.getInstance(locale);
                    Double price = product.getPrice()*product.getSoLuong();
                    String strPrice = numberFormat.format(price);
                    tvPrice.setText(strPrice +"đ");
                    tvDescribe.setText(product.getNote());
                    tv_soluong.setText(product.getSoLuong()+"");

                    layoutItem.setOnClickListener(v->{
                        onClickCart.onClick(product);
                    });
                    btnThem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(product1.getSoLuong()>soluong){
                                soluong++;
                                tv_soluong.setText(soluong+"");
                                tvPrice.setText((soluong*product.getPrice())+"");
                                ProductToOder product1 = new ProductToOder(product.getId(),product.getIdP(),product.getNameProduct(),product.getPrice(),soluong,product.getTypeProduct(),product.getNote(),product.getTime());
                                ChangeCart(product1);
                            }else{
                                Toast.makeText(context, "San pham "+product.getNameProduct()+" khong du", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    btnGiam.setOnClickListener(v->{
                        soluong--;
                        if(soluong>0){
                            tv_soluong.setText(soluong+"");
                            ProductToOder productToOder = new ProductToOder(product.getId(),product.getIdP(),product.getNameProduct(),product.getPrice(),soluong,product.getTypeProduct(),product.getNote(),product.getTime());
                            ChangeCart(productToOder);
                        }else{
                            list.remove(position);
                            String idU = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("list_oder/"+idU+"/"+product.getId());
                            reference1.removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    Log.e("TAG", "onComplete: " );
                                }
                            });
                            notifyDataSetChanged();
                        }

                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }

        private void ChangeCart(ProductToOder product ){
            String idU = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference mref = FirebaseDatabase.getInstance().getReference("list_oder/"+idU);
            mref.child(product.getId()).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.e("TAG", "onComplete: akaka " );
                }
            });

        }
    }

}
