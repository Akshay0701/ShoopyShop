package com.example.shoopyshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.shoopyshop.Adapter.TableAcessAdapter;
import com.example.shoopyshop.Model.ModelUser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    ActionBarDrawerToggle mDrawerToggle;
    FirebaseAuth firebaseAuth;
    Toolbar toolbar;
    Drawable drawable;
    String mUID;
    FirebaseUser user;
    DrawerLayout drawer;




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        //this code is imp
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
         drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);



        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_cart, R.id.nav_backet, R.id.nav_profile)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        //from this my code

    }




    @Override
    protected void onStart() {
        super.onStart();
        checkforuserlogin();
    }

    public void checkforuserlogin() {
        firebaseAuth= FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if (user != null) {
          //  token=FirebaseInstanceId.getInstance().getToken();
            mUID=user.getUid();

            SharedPreferences sp=getSharedPreferences("SP_User",MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            editor.putString("Current_USERID",mUID);
            editor.apply();
            Query query= FirebaseDatabase.getInstance().getReference("Users").orderByChild("email").equalTo(user.getEmail());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){

                        String bimg=""+ds.child("backimg").getValue();
                        String email=""+ds.child("email").getValue();
                        String  image=""+ds.child("image").getValue();
                        String  phone=""+ds.child("phone").getValue();
                        String name=""+ds.child("name").getValue();

                        ModelUser user=new ModelUser();
                     //   user.setBackimg(bimg);
                        user.setImage(image);
                        user.setEmail(email);
                        user.setName(name);
                        user.setPhone(phone);
                        user.setUid(user.getUid());

                        //change menu items set name
                        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                        View hView =  navigationView.getHeaderView(0);
                        TextView nav_user = (TextView)hView.findViewById(R.id.name_in_nav);
                        ImageView profileimg;
                        profileimg=hView.findViewById(R.id.profile_in_nav);
                       // backimg=hView.findViewById(R.id.backimg_in_nav);
                        TextView email_in_nav=hView.findViewById(R.id.email_in_nav);
                        email_in_nav.setText(email);
                        nav_user.setText(name);

                        //namet.setText(name);
                       // phonet.setText(phone);
                       // emailt.setText(email);

                        try{
                           Picasso.get().load(image).into(profileimg);
                         //   Picasso.get().load(bimg).into(backimg);

                        }catch (Exception e){
                           // Picasso.get().load(R.drawable.shoppylogo).into(backimg);
                            //Picasso.get().load(R.drawable.buttondesign).into(backimg);

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            //updatetoken
            //noinspection deprecation
           // updateToken(FirebaseInstanceId.getInstance().getToken());
        }
        else{
            startActivity(new Intent(MainActivity.this,Register.class));
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
