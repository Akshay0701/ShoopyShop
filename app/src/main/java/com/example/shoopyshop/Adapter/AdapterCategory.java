package com.example.shoopyshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shoopyshop.CateforyList;
import com.example.shoopyshop.Model.Category;
import com.example.shoopyshop.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterCategory  extends RecyclerView.Adapter<AdapterCategory.MyHolder>{

    Context context;
    List<Category> categoryList;

    public AdapterCategory(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_category,parent,false);
        return new MyHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int i) {
        final String nameCategory, imageCategoryURI;
        nameCategory = categoryList.get(i).getName();
        imageCategoryURI = categoryList.get(i).getImage();
        holder.txtMenuName.setText(nameCategory);

        //image set user dp
        try {
            Picasso.get().load(imageCategoryURI).placeholder(R.drawable.productimg).into(holder.imageView);

        } catch (Exception e) {


        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //:TODO send user to activty that will show menu under product

                    Intent foodList=new Intent(context, CateforyList.class);
                        //Because categoryId is key
                        foodList.putExtra("CategoryId",categoryList.get(i).getcId());

                        context.startActivity(foodList);

               // Toast.makeText(context, "hey its "+categoryList.get(i).getcId(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }



    class MyHolder extends RecyclerView.ViewHolder{

        //view  from row post
        public TextView txtMenuName;
        public ImageView imageView;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtMenuName=(TextView)itemView.findViewById(R.id.menu_name);
            imageView=(ImageView)itemView.findViewById(R.id.menu_image);

        }


    }

}
