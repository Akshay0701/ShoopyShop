package com.example.shoopyshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.shoopyshop.Adapter.AdapterProduct;
import com.example.shoopyshop.Database.Database;
import com.example.shoopyshop.Model.Foods;
import com.example.shoopyshop.Model.Order;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductDetails extends AppCompatActivity {


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TextView productName,productName2,productPrice,productDescription;
    ImageView productimg,add_cartBtn;
    CollapsingToolbarLayout collapsingToolbarLayout;
    String categoryid = "";
    String Productimageuri="";
    List<String> suggestlist = new ArrayList<>();

    List<Foods> foodsList;
    AdapterProduct adapterProduct;

    String productid = "";
    String discoumt="";
    String price="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        foodsList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_food);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Foods");

        //setting view componentes
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);
        productName = findViewById(R.id.productName);
        productName2 = findViewById(R.id.productName2);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);
        productimg = findViewById(R.id.productimg);

        if (getIntent() != null) {
            productid = getIntent().getStringExtra("ProductId");

        }
        if (!productid.isEmpty() && productid != null) {
            loadProduct();
        }

        //onlicked add cart button
        //init view
        final ElegantNumberButton numberButton = (ElegantNumberButton) findViewById(R.id.number_button);
        add_cartBtn = (ImageView) findViewById(R.id.add_cartBtn);
        add_cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        productid,
                        productName.getText().toString(),
                        numberButton.getNumber(),
                        price,
                        discoumt
                ));
                Toast.makeText(ProductDetails.this, "Added TO Cart", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void loadProduct(){
        //path
        Query ref = FirebaseDatabase.getInstance().getReference("Foods");
        //get all data from this ref
       final List<String>foodsids=new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                   if (ds.getKey().equals(productid)){
                       //set all data
                       Foods foodsdetails=ds.getValue(Foods.class);
                       //setting data
                       productName.setText(foodsdetails.getName());
                       productName2.setText(foodsdetails.getName());
                       price=foodsdetails.getPrice();
                       productPrice.setText("$ "+foodsdetails.getPrice());
                       productDescription.setText(foodsdetails.getDescription());
                       discoumt=foodsdetails.getDiscount();
                       Productimageuri=foodsdetails.getImage();

                       //image set user dp
                       try {
                           Picasso.get().load(foodsdetails.getImage()).placeholder(R.drawable.productimg).into(productimg);

                       } catch (Exception e) {


                       }


                   }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProductDetails.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }
}
