package com.example.shoopyshop.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.shoopyshop.Model.Order;
import com.example.shoopyshop.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

    public TextView txt_cart_name,txt_price;
    public ImageView img_cart_count;



    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_cart_name=(TextView)itemView.findViewById(R.id.cart_item_name);
        txt_price=(TextView)itemView.findViewById(R.id.cart_item_Price);
        img_cart_count=(ImageView)itemView.findViewById(R.id.cart_item_count);

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> listData=new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }


    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater= LayoutInflater.from(context);
        View itemView=inflater.inflate(R.layout.cartlayout,parent,false);
        return new CartViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        TextDrawable drawable=TextDrawable.builder().buildRound(""+listData.get(position).getQuantity(), Color.RED);
        holder.img_cart_count.setImageDrawable(drawable);

        Locale locale=new Locale("en,US");
        NumberFormat fmt= NumberFormat.getCurrencyInstance(locale);
        int price=(Integer.parseInt(listData.get(position).getPrice()))*(Integer.parseInt(listData.get(position).getQuantity()));
        holder.txt_price.setText(fmt.format(price));
        holder.txt_cart_name.setText(listData.get(position).getProductName());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
