package com.example.shoopyshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shoopyshop.Model.Foods;
import com.example.shoopyshop.ProductDetails;
import com.example.shoopyshop.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.MyHolder>{

    Context context;
    List<Foods> foodsList;
    String type="row_product";

    public AdapterProduct(Context context, List<Foods> foodsList, String type) {
        this.context = context;
        this.foodsList = foodsList;
        this.type = type;
    }

    @NonNull
    @Override
    public AdapterProduct.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (type.equals("row_product")){

            view= LayoutInflater.from(context).inflate(R.layout.row_product,parent,false);
        }
        else if (type.equals("row_product2")){
            view= LayoutInflater.from(context).inflate(R.layout.row_product2,parent,false);

        }
        else if (type.equals("row_product3")){
            view= LayoutInflater.from(context).inflate(R.layout.row_product3,parent,false);
        }
        else {
            view= LayoutInflater.from(context).inflate(R.layout.row_product,parent,false);
        }
        return new AdapterProduct.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProduct.MyHolder holder, final int i) {
        final String nameFood, imageFoodURI,productprice,productdesc;
        nameFood = foodsList.get(i).getName();
        imageFoodURI = foodsList.get(i).getImage();
        productprice = foodsList.get(i).getPrice();
        productdesc = foodsList.get(i).getDescription();

        //setting data
        holder.productDescription.setText(productdesc);
        holder.productPrice.setText("$ "+productprice);
        holder.productName.setText(nameFood);

        //image set user dp
        try {
            Picasso.get().load(imageFoodURI).placeholder(R.drawable.productimg).into(holder.productimg);

        } catch (Exception e) {


        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //:TODO send user to activty that will show product details

               Intent foodList=new Intent(context, ProductDetails.class);
                //Because categoryId is key
                foodList.putExtra("ProductId",foodsList.get(i).getFoodid());

               context.startActivity(foodList);

               //  Toast.makeText(context, "hey its "+foodsList.get(i).getFoodid(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodsList.size();
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
