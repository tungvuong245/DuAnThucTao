package com.example.lasthope.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lasthope.databinding.ItemChatLeftBinding;
import com.example.lasthope.databinding.ItemChatRightBinding;
import com.example.lasthope.databinding.LayoutItemBannerBinding;
import com.example.lasthope.model.Chat;
import com.example.lasthope.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ChatToUserAdapter extends RecyclerView.Adapter<ChatToUserAdapter.ViewHolderLeft> {
    private ArrayList<Chat> list;
    private Context context;

    private User user;

    public ChatToUserAdapter(ArrayList<Chat> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolderLeft onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatToUserAdapter.ViewHolderLeft(ItemChatLeftBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderLeft holder, int position) {
        Chat chat = list.get(position);
        Chat chat1 = new Chat(chat.getContent(),chat.getId(),chat.getUser(),true,chat.getTime(),chat.getAdminRep());
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("chat/"+chat.getUser().getId());
        reference1.child(chat.getId()).setValue(chat1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.e("TAG", "onComplete: " );
            }
        });
        if(chat.getAdminRep().equals("")){
            holder.layout.setGravity(Gravity.RIGHT);
            holder.tvContent.setText(chat.getContent());
            holder.tvTime.setText(chat.getTime());
            holder.layout2.setBackgroundColor(0xFF2BB7F4);
            StorageReference reference = FirebaseStorage.getInstance().getReference().child("avatars");
            reference.listAll().addOnSuccessListener(listResult -> {
                for (StorageReference files: listResult.getItems()){
                    if(files.getName().equals(chat.getUser().getId())){
                        files.getDownloadUrl().addOnSuccessListener(uri -> {
                            Glide.with(context).load(uri).into(holder.imgUser);
                        });
                    }
                }
            });
        }else{
            holder.layout.setGravity(Gravity.LEFT);
            holder.tvContent.setText(chat.getAdminRep());
            holder.tvTime.setText(chat.getTime());
        }


    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolderLeft extends RecyclerView.ViewHolder {
        TextView tvContent, tvTime;
        ImageView imgUser;
        LinearLayout layout ,layout2 ;
        public ViewHolderLeft(ItemChatLeftBinding binding) {
            super(binding.getRoot());
            tvContent = binding.tvChat;
            imgUser = binding.imgUser;
            tvTime = binding.tvTime;
            layout = binding.layout1;
            layout2 = binding.layout2;
        }
    }
}


