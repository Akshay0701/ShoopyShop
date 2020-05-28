package com.example.shoopyshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.shoopyshop.Adapter.AdapterOrder;
import com.example.shoopyshop.Adapter.AdapterOrderProduct;
import com.example.shoopyshop.Adapter.AdapterProduct;
import com.example.shoopyshop.Model.Foods;
import com.example.shoopyshop.Model.Order;
import com.example.shoopyshop.Model.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderDetails extends AppCompatActivity {


    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    List<Request>  requestList;
    //adapter need FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;
    AdapterOrderProduct adapterOrder;
    FirebaseDatabase database;
    DatabaseReference requests;
    String orderid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        //init Firebase
      //  database = FirebaseDatabase.getInstance();
       // requests = database.getReference("Requests");
        requestList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(OrderDetails.this);
        recyclerView.setLayoutManager(layoutManager);
        if(getIntent()!=null){
            orderid =getIntent().getStringExtra("orderid");

        }
        if(!orderid.isEmpty()&&orderid!=null){
            loadOrders(orderid);
        }


    }
    private void loadOrders(final String orderid) {
        //path
        Query ref = FirebaseDatabase.getInstance().getReference("Requests").orderByChild("name").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        //get all data from this ref
        requestList.clear();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Request request = ds.getValue(Request.class);

                    if (orderid.equals(request.getId())) {
                        Toast.makeText(OrderDetails.this, ""+request.getId(), Toast.LENGTH_SHORT).show();
                        // request.setId());
                        //adding each object
                        requestList.add(request);
                    }
                }
                //adapter

                adapterOrder = new AdapterOrderProduct(OrderDetails.this, requestList);

                recyclerView.setLayoutManager(new LinearLayoutManager(OrderDetails.this, LinearLayoutManager.VERTICAL, false));

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
