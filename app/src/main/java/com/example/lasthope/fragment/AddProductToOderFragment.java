package com.example.lasthope.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import com.example.lasthope.Adapter.DetailProductToOderAdapter;
import com.example.lasthope.Adapter.ProductToOderAdapter;
import com.example.lasthope.R;
import com.example.lasthope.base.BaseFragment;
import com.example.lasthope.base.OnClickCart;
import com.example.lasthope.databinding.FragmentAddProductToOderBinding;
import com.example.lasthope.model.Product;
import com.example.lasthope.model.ProductToOder;
import com.example.lasthope.model.Receipt;
import com.example.lasthope.model.TypeProduct;
import com.example.lasthope.model.User;
import com.example.lasthope.setting.UpdateUserFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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


public class AddProductToOderFragment extends BaseFragment implements OnClickCart {
    private FragmentAddProductToOderBinding binding = null;
    private ArrayList<Product> listProduct;
    private TypeProduct typeProduct;
    private ProductToOder productToOder;
    DetailProductToOderAdapter adapter ;
    private ArrayList<ProductToOder> toOderArrayList;
    private Receipt receipt;
    private User user ;


    public AddProductToOderFragment() {
        // Required empty public constructor
    }

    public static AddProductToOderFragment newInstance() {
        AddProductToOderFragment fragment = new AddProductToOderFragment();
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
        binding = FragmentAddProductToOderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDataUser();
        listProduct = new ArrayList<>();
        Bundle bundle =getArguments();
        binding.icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backStack();
            }
        });
        toOderArrayList = new ArrayList<>();
        GetCart();
        binding.icEditAddress.setOnClickListener(v->{
            replaceFragment(new UpdateUserFragment().newInstance(user));
        });

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
    private void GetCart(){
        String idU = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("list_oder/"+idU);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(toOderArrayList!=null){
                    toOderArrayList.clear();
                }
                for(DataSnapshot dataSnapshot :snapshot.getChildren()){
                    ProductToOder productToOder = dataSnapshot.getValue(ProductToOder.class);
                    toOderArrayList.add(productToOder);
                    Log.e("TAG", "onComplete: "+toOderArrayList.size() );
                }
                adapter.notifyDataSetChanged();
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("list_bill");
                String key = reference1.push().getKey();
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String strDate = dateFormat.format(date);
                Double monney  = Double.valueOf(0);
                for (int i = 0;i<toOderArrayList.size();i++){
                    monney = monney+(toOderArrayList.get(i).getPrice()*toOderArrayList.get(i).getSoLuong());
                }
                binding.tvPrice.setText(monney+" d");
                receipt = new Receipt(key,toOderArrayList,monney,strDate,idU);
                binding.btnAdd.setOnClickListener(v->{
                    if(!binding.tvAddress.getText().toString().equals("")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Xác Nhận Thanh Toán ");
                        builder.setIcon(getContext().getDrawable(R.drawable.ic_cart));
                        builder.setMessage("Bạn chắc chắn muốn thanh toán!");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                reference1.child(key).setValue(receipt, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        Log.e("TAG", "onComplete: them vao bill " );
                                        reference.removeValue(new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                Log.e("TAG", "onComplete: remove  " );
                                                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getContext(), "Da huy", Toast.LENGTH_SHORT).show();
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog sh = builder.create();
                        sh.show();

                    }else{
                        Toast.makeText(getContext(), "Bạn chưa có địa chỉ giao hàng", Toast.LENGTH_SHORT).show();
                    }

                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new DetailProductToOderAdapter(toOderArrayList,getContext(),AddProductToOderFragment.this);
        binding.listProductToOder.setAdapter(adapter);
    }


    public void backStack() {
        getParentFragmentManager().popBackStack();
    }


    private void getDataUser(){
        String idU = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/"+idU);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                binding.tvAddress.setText(user.getAddress());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void ClearOder() {
        String idU = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("list_oder/"+idU);
        ref.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
            }
        });

    }


    @Override
    public void onClick(ProductToOder product) {

    }

}