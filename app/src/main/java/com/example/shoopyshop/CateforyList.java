package com.example.shoopyshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoopyshop.Adapter.AdapterProduct;
import com.example.shoopyshop.Model.Foods;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CateforyList extends AppCompatActivity {


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TextView txtFullName;

    String categoryid = "";

    //rebaseRecyclerAdapter<Foods, FoodViewHolder> adapter;

    //search item
    //rebaseRecyclerAdapter<Foods, FoodViewHolder> searchAdapter;
    List<String> suggestlist = new ArrayList<>();
    //aterialSearchBar materialSearchBar;



    List<Foods> foodsList;
    AdapterProduct adapterProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catefory_list);
        foodsList = new ArrayList<>();
        recyclerView=findViewById(R.id.recycler_food);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Foods");
        if(getIntent()!=null){
            categoryid =getIntent().getStringExtra("CategoryId");

        }
        if(!categoryid.isEmpty()&&categoryid!=null){
            loadProduct();
        }

    }
    private void loadProduct(){
        //path
        Query ref = FirebaseDatabase.getInstance().getReference("Foods").orderByChild("menuId").equalTo(categoryid);
        //get all data from this ref
        foodsList.clear();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Foods foods = ds.getValue(Foods.class);
                    foods.setFoodid(ds.getKey());
                    //adding each object
                    foodsList.add(foods);
                }
                //adapter

                adapterProduct = new AdapterProduct(CateforyList.this,foodsList,"row_product2");

                GridLayoutManager manager = new GridLayoutManager(CateforyList.this, 2, GridLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(manager);
                //  recyclerView.setLayoutManager(new LinearLayoutManager(CateforyList.this, LinearLayoutManager.VERTICAL, false));
                //set adapter to recycle
                recyclerView.setAdapter(adapterProduct);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CateforyList.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
}
