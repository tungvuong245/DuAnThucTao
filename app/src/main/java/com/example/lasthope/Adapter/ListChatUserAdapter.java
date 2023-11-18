package com.example.lasthope.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lasthope.base.OnClickUserChat;
import com.example.lasthope.databinding.ItemUserChatBinding;
import com.example.lasthope.databinding.LayoutItemBannerBinding;
import com.example.lasthope.model.Chat;
import com.example.lasthope.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListChatUserAdapter extends RecyclerView.Adapter<ListChatUserAdapter.ViewHolder> {
    private ArrayList<User> list ;
    OnClickUserChat onClickUserChat ;
    private Chat chat ;

    public ListChatUserAdapter(ArrayList<User> list, OnClickUserChat onClickUserChat) {
        this.list = list;
        this.onClickUserChat = onClickUserChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListChatUserAdapter.ViewHolder(ItemUserChatBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = list.get(position);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chat/"+user.getId());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int noti = 0;
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Chat chat1 = snapshot1.getValue(Chat.class);
                    if(chat1.getSeen()==false){
                        noti++;
                    }
                }
                if(noti==0){

                }else{
                    holder.tvNoti.setVisibility(View.VISIBLE);
                    holder.tvNoti.setText(String.valueOf(noti));

                }
                holder.tvNameUser.setText(user.getName_user());
                holder.layout.setOnClickListener(v->{
                    onClickUserChat.onClickUserChat(user);
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNameUser,tvNoti ;
        LinearLayout layout ;
        public ViewHolder(ItemUserChatBinding binding) {
            super(binding.getRoot());
            tvNameUser = binding.tvNameUser;
            tvNoti = binding.tvNoti;
            layout = binding.layout;
        }
    }
}
