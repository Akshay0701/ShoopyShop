package com.example.shoopyshop.ui.Cart;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.hoang8f.widget.FButton;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoopyshop.Adapter.CartAdapter;
import com.example.shoopyshop.Database.Database;
import com.example.shoopyshop.Model.Order;
import com.example.shoopyshop.Model.Request;
import com.example.shoopyshop.R;
import com.google.android.gms.common.internal.service.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    FButton btnPlace;

    List<Order> carts = new ArrayList<>();

    CartAdapter adapter;

    int total=0;
    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //inti
        recyclerView = (RecyclerView) root.findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView) root.findViewById(R.id.total);
        btnPlace = (FButton) root.findViewById(R.id.btn_PlaceOrder);

        loadListFood();

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlertDialog();

            }
        });

        return root;
    }

    private void showAlertDialog() {
        @SuppressLint("ResourceType") AlertDialog.Builder  alertDialog=new AlertDialog.Builder(getContext(),5);
        alertDialog.setTitle("One more step!");
        alertDialog.setMessage("Enter your Address:");

        final EditText edtAddress=new EditText(getActivity());
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edtAddress.setLayoutParams(lp);
        alertDialog.setView(edtAddress);
        alertDialog.setIcon(R.drawable.ic_cart_logo);
        final String id=String.valueOf(System.currentTimeMillis());
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request=new Request(
                        FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                        FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        edtAddress.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        "0",
                        id,
                        carts
                );
//summit to Firebase
                requests.child(id).setValue(request);

                //deleteCart

                new Database(getContext()).cleanCart();
                Toast.makeText(getContext(),"Thank you ,Order Place",Toast.LENGTH_SHORT).show();
               // finish();

            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();


    }

    private void loadListFood() {
        carts=new Database(getContext()).getCarts();
        adapter=new CartAdapter(carts,getContext());
        recyclerView.setAdapter(adapter);

//Calculator
         total=0;
        for(Order order:carts)
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));

        Locale locale=new Locale("en,US");
        NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));

    }
}
