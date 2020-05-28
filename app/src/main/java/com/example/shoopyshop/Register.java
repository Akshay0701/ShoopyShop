package com.example.shoopyshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {


    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    EditText mEmailtext,mpasswordtext,nameEt;
    TextView malreadytext,are_u_Admin;
    Button bregister;
    CheckBox rememberPass;

    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedPreferences=getSharedPreferences("Remember",MODE_PRIVATE);
        editor= PreferenceManager.getDefaultSharedPreferences(this).edit();

        bregister=findViewById(R.id.register_submit);
        nameEt=findViewById(R.id.nameEt);
        mEmailtext=findViewById(R.id.emailEt);
        are_u_Admin=findViewById(R.id.are_u_Admin);
        mpasswordtext=findViewById(R.id.passwordEt);
        malreadytext=findViewById(R.id.already_account);
        progressDialog=new ProgressDialog(this,4);

        mAuth = FirebaseAuth.getInstance();

        progressDialog.setMessage("Registering user...");





        //noinspection deprecation
        bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=mEmailtext.getText().toString().trim();
                String password=mpasswordtext.getText().toString().trim();


                    editor.putString("username",email.trim());
                    editor.putString("password",password.trim());
                    editor.apply();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmailtext.setError("Invalided Email");
                    mEmailtext.setFocusable(true);

                }
                else if(password.length()<6){
                    mpasswordtext.setError("Password length at least 6 characters");
                    mpasswordtext.setFocusable(true);
                }
                else {
                    registerUser(email,password,nameEt.getText().toString());

                }
            }
        });

        malreadytext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,Login.class));
                finish();
            }
        });

        //goto to admin  login
        are_u_Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this,AdminLogin.class));
            }
        });

    }
    private void registerUser(final String email, String password, final String nameEt) {

        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();

                            String email= user.getEmail();
                            String uid=user.getUid();
                            HashMap<Object, String> hashMap=new HashMap<>();

                            hashMap.put("email",email);
                            hashMap.put("uid",uid);
                            hashMap.put("name",nameEt);
                            hashMap.put("phone","");
                            hashMap.put("image","https://www.biowritingservice.com/wp-content/themes/tuborg/images/Executive%20Bio%20Sample%20Photo.png");
                            hashMap.put("backimg","https://miro.medium.com/max/3000/1*MI686k5sDQrISBM6L8pf5A.jpeg");
                            FirebaseDatabase database=FirebaseDatabase.getInstance();

                            DatabaseReference reference=database.getReference("Users");
                            reference.child(uid).setValue(hashMap);

                            Toast.makeText(Register.this, "Registered with "+user.getEmail(), Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(Register.this, MainActivity.class));
                            finish();
                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Register.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        //check if user already register

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String username=prefs.getString("username","");
        String pass=prefs.getString("password","");

        if(username.equals("")&&pass.equals("")) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog.setMessage("Logging...");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(username, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                // Sign in success, update UI with the signed-in user's information

                                FirebaseUser user = mAuth.getCurrentUser();
                                startActivity(new Intent(Register.this, MainActivity.class));
                             //   overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                finish();

                            } else {
                                // If sign in fails, display a message to the user.
                                progressDialog.dismiss();
                                Toast.makeText(Register.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(Register.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        super.onStart();
    }

    public boolean onSupportNavigateUp(){

        onBackPressed();//go baack

        return super.onSupportNavigateUp();
    }
}
