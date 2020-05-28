package com.example.shoopyshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoopyshop.Model.Order;
import com.example.shoopyshop.Model.Request;
import com.example.shoopyshop.OrderDetails;
import com.example.shoopyshop.ProductDetails;
import com.example.shoopyshop.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterOrderProduct  extends RecyclerView.Adapter<AdapterOrderProduct.MyHolder>{


    Context context;
    List<Request> requestList;
    List<Order> orderlist;
    public AdapterOrderProduct(Context context, List<Request> requestList) {
        this.context = context;
        this.requestList = requestList;

    }

    @NonNull
    @Override
    public AdapterOrderProduct.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_product,parent,false);
        return new AdapterOrderProduct.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterOrderProduct.MyHolder holder, final int i) {

        final String nameFood, imageFoodURI,productprice,productdesc;
        nameFood = orderlist.get(i).getProductName();
       // imageFoodURI = orderlist.get(i).getProductImage();
        productprice = orderlist.get(i).getPrice();
        productdesc = orderlist.get(i).getProductId();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Foods");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                   if( ds.getKey().equals(orderlist.get(i).getProductId())){
                       try {
                              Picasso.get().load( ds.child("image").getValue().toString()).placeholder(R.drawable.productimg).into(holder.productimg);

                       } catch (Exception e) {


                       }

                   }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //setting data
        holder.productDescription.setText(productdesc);
        holder.productPrice.setText("$ "+productprice);
        holder.productName.setText(nameFood);

        //image set user dp

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //:TODO send user to activty that will show product details

                Intent foodList=new Intent(context, ProductDetails.class);
                //Because categoryId is key
                foodList.putExtra("ProductId",orderlist.get(i).getProductId());

                context.startActivity(foodList);

                //  Toast.makeText(context, "hey its "+foodsList.get(i).getFoodid(), Toast.LENGTH_SHORT).show();
            }
        });;
    }

    @Override
    public int getItemCount() {
        orderlist=requestList.get(0).getFoods();
        return requestList.get(0).getFoods().size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        //view  from row post
        public TextView productDescription,productPrice,productName;
        public ImageView productimg,add_cartBtn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            productDescription=(TextView)itemView.findViewById(R.id.productDescription);
            productPrice=(TextView)itemView.findViewById(R.id.productPrice);
            productName=(TextView)itemView.findViewById(R.id.productName);
            productimg=(ImageView)itemView.findViewById(R.id.productimg);
            add_cartBtn=(ImageView)itemView.findViewById(R.id.add_cartBtn);

        }


    }
}

