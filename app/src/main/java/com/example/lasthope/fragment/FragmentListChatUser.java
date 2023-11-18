package com.example.lasthope.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lasthope.Adapter.ListChatUserAdapter;
import com.example.lasthope.base.BaseFragment;
import com.example.lasthope.base.OnClickUserChat;
import com.example.lasthope.databinding.FragmentListUserChatBinding;
import com.example.lasthope.model.Chat;
import com.example.lasthope.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentListChatUser extends BaseFragment implements OnClickUserChat {
    private ArrayList<User> listUser ;
    private ArrayList<Integer> listNoti;
    private ArrayList<Chat> listChat ;
    private FragmentListUserChatBinding binding = null;
    private ListChatUserAdapter adapter ;

    public static FragmentListChatUser newInstance() {
        FragmentListChatUser fragment = new FragmentListChatUser();
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
        binding = FragmentListUserChatBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();

    }

    @Override
    public void loadData() {
        getData();
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
    private void getData() {
       listUser = new ArrayList<>();
       listNoti = new ArrayList<>();
       listChat = new ArrayList<>();
       DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("users");
       mRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(listUser!=null){
                   listUser.clear();
               }
               for( DataSnapshot snapshot1 : snapshot.getChildren()){
                   User user = snapshot1.getValue(User.class);
                   listUser.add(user);
               }
               adapter.notifyDataSetChanged();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
       adapter = new ListChatUserAdapter(listUser,FragmentListChatUser.this);
       binding.rcvUserChat.setAdapter(adapter);

    }


    @Override
    public void onClickUserChat(User user) {
        replaceFragment(new ChatFragment(user));
    }
}
