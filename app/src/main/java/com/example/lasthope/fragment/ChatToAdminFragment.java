package com.example.lasthope.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lasthope.Adapter.ChatAdapter;
import com.example.lasthope.base.BaseFragment;
import com.example.lasthope.databinding.FragmentChatToAdminBinding;
import com.example.lasthope.model.Chat;
import com.example.lasthope.model.Product;
import com.example.lasthope.model.User;
import com.example.lasthope.setting.SettingFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class ChatToAdminFragment extends BaseFragment {
    FragmentChatToAdminBinding binding = null;
    private User user ;
    private Chat chat ;
    private ArrayList<Chat> listChat ;
    private ChatAdapter adapter ;


    public static ChatToAdminFragment newInstance() {
        ChatToAdminFragment fragment = new ChatToAdminFragment();
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
        binding = FragmentChatToAdminBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void loadData() {
        getDataUser();
        String idU = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chat/"+idU);
        String key = reference.push().getKey();
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        binding.btnSend.setOnClickListener(v->{
            chat = new Chat(binding.edContent.getText().toString(),key,user,false,strDate,"");
            reference.child(key).setValue(chat, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    binding.edContent.setText("");
                    loadData();
                }
            });

        });
        getChat();

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

    private void getChat(){
        listChat = new ArrayList<>();
        String idU = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
        adapter = new ChatAdapter(listChat,getContext());
        binding.rcvChat.setAdapter(adapter);


    }

    private void getDataUser() {
        String idU = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/"+idU);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
