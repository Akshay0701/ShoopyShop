package com.example.shoopyshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shoopyshop.Model.Category;
import com.example.shoopyshop.Model.Order;
import com.example.shoopyshop.Model.Request;
import com.example.shoopyshop.OrderDetails;
import com.example.shoopyshop.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterOrder  extends RecyclerView.Adapter<AdapterOrder.MyHolder>{



    Context context;
    List<Request> requestList;
    List<Order> orderlist;
    public AdapterOrder(Context context, List<Request> requestList) {
        this.context = context;
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.order_layout,parent,false);
        return new AdapterOrder.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int i) {
        holder.txt_Order_id.setText("Order ID :"+requestList.get(i).getId());
       String order=  requestList.get(i).getStatus();
       if (order.equals("0")) {
           holder.txt_Order_Status.setText("Order : Order Placed");
       }
       else if (order.equals("1")){
           holder.txt_Order_Status.setText("Order : On The Way");
       }
       else if(order.equals("2"))
       {
           holder.txt_Order_Status.setText("Order : Order Deleverd");
       }
       else {
           holder.txt_Order_Status.setText("Order : order placed");
       }
       holder.txt_Order_Address.setText(requestList.get(i).getAddress());
        holder.txt_Order_Phone.setText(requestList.get(i).getPhoneno());
     //   orderlist=requestList.get(i).getFoods();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //goto orderDetails
                Intent intent=new Intent(context, OrderDetails.class);
                intent.putExtra("orderid",requestList.get(i).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        //view  from row post
        public TextView txt_Order_id,txt_Order_Status,txt_Order_Phone,txt_Order_Address;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txt_Order_id=(TextView)itemView.findViewById(R.id.order_id);
            txt_Order_Status=(TextView)itemView.findViewById(R.id.order_status);
            txt_Order_Phone=(TextView)itemView.findViewById(R.id.order_phone);
            txt_Order_Address=(TextView)itemView.findViewById(R.id.order_address);

        }


    }
}
