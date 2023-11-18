package com.example.lasthope.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lasthope.base.OnclickOptionMenu;
import com.example.lasthope.databinding.LayoutItemBannerBinding;
import com.example.lasthope.model.Product;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolderTable> {
    private List<Product> list;
    private OnclickOptionMenu onclickOptionMenu;
    private Context context;
    private OnItemLongClickListener mOnItemLongClickListener;

    public interface OnItemLongClickListener{
        void onLongClickTable(Product product);
    }

    public BannerAdapter(List<Product> list, OnclickOptionMenu onclickOptionMenu, OnItemLongClickListener mOnItemLongClickListener, Context context) {
        this.list = list;
        this.onclickOptionMenu = onclickOptionMenu;
        this.mOnItemLongClickListener = mOnItemLongClickListener;
        this.context = context;

    }
    public BannerAdapter(List<Product> list, OnclickOptionMenu onclickOptionMenu, Context context) {
        this.list = list;
        this.onclickOptionMenu = onclickOptionMenu;
        this.context = context;

    }
    public void setFilterList(ArrayList<Product> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderTable onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderTable(LayoutItemBannerBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTable holder, int position) {
        Product product = list.get(position);
        if (product == null){
            return;
        } else {
            holder.initData(product,context);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolderTable extends RecyclerView.ViewHolder {
        TextView nameProduct ,tvPriceProduct;
        private ConstraintLayout layoutHeaderTable ;
        private LinearLayout layoutBodyTable;
        private ImageView logo,img_product;


        public ViewHolderTable(LayoutItemBannerBinding binding) {
            super(binding.getRoot());
            img_product = binding.imgProduct;
            logo = binding.icLogoTable;
            nameProduct = binding.tvNameProduct;
            tvPriceProduct = binding.tvPriceProduct;
            layoutHeaderTable = binding.layoutHeaderTable;
            layoutBodyTable = binding.layoutBodyTable;
        }

        void initData(Product product, Context context){
            nameProduct.setText(product.getNameProduct());
            tvPriceProduct.setText(product.getPrice()+"");
            itemView.setOnClickListener(v -> {
                onclickOptionMenu.onClick(product);
            });
            itemView.setOnLongClickListener(v ->{
                mOnItemLongClickListener.onLongClickTable(product);
                return true;
            });
            StorageReference reference = FirebaseStorage.getInstance().getReference().child("imgProducts");
            reference.listAll().addOnSuccessListener(listResult -> {
                for (StorageReference files: listResult.getItems()){
                    if(files.getName().equals(product.getId())){
                        files.getDownloadUrl().addOnSuccessListener(uri -> {
                            Glide.with(context).load(uri).into(img_product);
                        });
                    }
                }
            });

        }

    }
}
