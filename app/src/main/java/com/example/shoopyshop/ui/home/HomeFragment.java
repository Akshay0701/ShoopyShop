package com.example.shoopyshop.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.shoopyshop.Adapter.AdapterCategory;
import com.example.shoopyshop.Adapter.AdapterProduct;
import com.example.shoopyshop.MainActivity;
import com.example.shoopyshop.Model.Category;
import com.example.shoopyshop.Model.Foods;
import com.example.shoopyshop.Model.ModelUser;
import com.example.shoopyshop.R;
import com.example.shoopyshop.Register;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    //sliding

    ViewPager viewPager;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager  layoutManager;
    TextView txtFullName;
    RecyclerView recyclerViewTrendProduct;

    List<Category> categoryList;
    List<Foods> trendList;
    AdapterCategory adapterCategory;
    AdapterProduct adapterTrendProduct;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        checkforuserlogin();
        categoryList = new ArrayList<>();
        trendList=new ArrayList<>();
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        //toolbar.setTitle("menu");

        //getContext.setS(toolbar);
        final int images[]={R.drawable.banner1,R.drawable.banner2,R.drawable.banner3,R.drawable.banner4};

        //sliding view
        viewPager=(ViewPager)root.findViewById(R.id.viewpager);
       SlideView adpater= new SlideView(getContext());
        viewPager.setAdapter(adpater);

        //init firebase
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Category");

//userName


        //    RecyclerView recyclerView;
        //    RecyclerView.LayoutManager  layoutManager;
      /*  DrawerLayout drawer = (DrawerLayout) root.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

       */


        //name for User in menu

        //load trend product
        recyclerViewTrendProduct=root.findViewById(R.id.recyclerView_trend);
        recyclerViewTrendProduct.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerViewTrendProduct.setLayoutManager(layoutManager);

        //load menu
        recyclerView =(RecyclerView)root.findViewById(R.id.recyclerView_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        loadmenu();
        loadTrend();

        return root;
    }

    private void loadTrend() {
        //path
        Query ref = FirebaseDatabase.getInstance().getReference("Category").child("ProductTrend");
        //get all data from this ref
        categoryList.clear();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Foods foods = ds.getValue(Foods.class);
                    foods.setFoodid(ds.getKey());
                    //adding each object
                    trendList.add(foods);
                }
                //adapter

                adapterTrendProduct = new AdapterProduct(getActivity(), trendList,"row_product3");
                GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                recyclerViewTrendProduct.setLayoutManager(manager);

                //set adapter to recycle
                recyclerViewTrendProduct.setAdapter(adapterTrendProduct);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //     Toast.makeText(getActivity(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    public void checkforuserlogin() {
        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            //  token=FirebaseInstanceId.getInstance().getToken();
            String mUID = user.getUid();
        }
    }



    private void loadmenu(){
    //path
    Query ref = FirebaseDatabase.getInstance().getReference("Category");
    //get all data from this ref
        categoryList.clear();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Category category = ds.getValue(Category.class);
                    category.setcId(ds.getKey());
                    //adding each object
                            categoryList.add(category);
                    }
                //adapter

                adapterCategory = new AdapterCategory(getActivity(), categoryList);

                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

                //set adapter to recycle
                recyclerView.setAdapter(adapterCategory);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
           //     Toast.makeText(getActivity(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

}
