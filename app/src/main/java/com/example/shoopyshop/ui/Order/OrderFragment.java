package com.example.shoopyshop.ui.Order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoopyshop.Adapter.AdapterCategory;
import com.example.shoopyshop.Adapter.AdapterOrder;
import com.example.shoopyshop.Model.Category;
import com.example.shoopyshop.Model.Request;
import com.example.shoopyshop.R;
import com.google.android.gms.common.internal.service.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {



    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    List<Request>  requestList;
   //adapter need FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;
    AdapterOrder adapterOrder;
    FirebaseDatabase database;
    DatabaseReference requests;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_order, container, false);
        //init Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");
        requestList= new ArrayList<>();
        recyclerView = (RecyclerView) root.findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        loadOrders(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        return root;
    }

    private void loadOrders(String phoneNumber) {
        //path
        Query ref = FirebaseDatabase.getInstance().getReference("Requests").orderByChild("phoneno").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        //get all data from this ref
        requestList.clear();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Request request = ds.getValue(Request.class);
                   // request.setId());
                    //adding each object
                    requestList.add(request);
                }
                //adapter

                adapterOrder = new AdapterOrder(getActivity(), requestList);

                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                //set adapter to recycle
                recyclerView.setAdapter(adapterOrder);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //     Toast.makeText(getActivity(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

}
