package com.example.shoopyshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLogin extends AppCompatActivity {


    EditText log_username,log_passwordEt;
    Button login_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        login_submit=findViewById(R.id.login_submit);
        log_username=findViewById(R.id.log_username);
        log_passwordEt=findViewById(R.id.log_passwordEt);
        //first check is edittext empty if not pass its value
        login_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection EqualsBetweenInconvertibleTypes
                if (log_username.equals("")&&log_passwordEt.equals("")){
                    Toast.makeText(AdminLogin.this, "plz enter value", Toast.LENGTH_SHORT).show();
                }
                else {
                    loginstart(log_username.getText().toString(),log_passwordEt.getText().toString());
                }
            }
        });

    }

    private void loginstart(final String username, final String password) {
        DatabaseReference database=FirebaseDatabase.getInstance().getReference("Admin");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String data_username =dataSnapshot.child("UserName").getValue().toString();
                String data_password =dataSnapshot.child("Password").getValue().toString();
                if (username.equals(data_username)&&password.equals(data_password)){
                    //allow to enter
                    Toast.makeText(AdminLogin.this, "logined", Toast.LENGTH_SHORT).show();
                  //  startActivity(new Intent(AdminLogin.this,AdminDashBorad.class));
                }
                else {
                    Toast.makeText(AdminLogin.this, "incorrect pass", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
