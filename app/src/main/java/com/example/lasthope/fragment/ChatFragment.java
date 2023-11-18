package com.example.lasthope.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lasthope.Adapter.ChatAdapter;
import com.example.lasthope.Adapter.ChatToUserAdapter;
import com.example.lasthope.base.BaseFragment;
import com.example.lasthope.databinding.FragmentChatBinding;
import com.example.lasthope.model.Chat;
import com.example.lasthope.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatFragment extends BaseFragment {


    FragmentChatBinding binding = null;
    User dataUser ;
    private Chat chat;
    private ArrayList<Chat> listChat ;
    private ChatToUserAdapter adapter ;
    public ChatFragment (User dataUser){
        this.dataUser = dataUser ;
    }
    public ChatFragment newInstance() {
        return new ChatFragment(dataUser);
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
        binding = FragmentChatBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String idU = dataUser.getId();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chat/"+idU);
        String key = reference.push().getKey();
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        binding.btnSend.setOnClickListener(v->{
            chat = new Chat("",key,dataUser,false,strDate,binding.edChat.getText().toString());
            reference.child(key).setValue(chat, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    binding.edChat.setText("");
                    loadData();
                }
            });

        });
        getChat(idU);


    }

    @Override
    public void loadData() {

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
    private void getChat(String idU){
        listChat = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chat/"+idU);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listChat!=null){
                    listChat.clear();
                }
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    Chat chat1 = snapshot1.getValue(Chat.class);
                        listChat.add(chat1);
                    Log.e("TAG", "onDataChange: "+chat1 );
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new ChatToUserAdapter(listChat,getContext());
        binding.rcvChat.setAdapter(adapter);


    }
}
