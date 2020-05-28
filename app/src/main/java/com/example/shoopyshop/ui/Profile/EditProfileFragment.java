package com.example.shoopyshop.ui.Profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.shoopyshop.MainActivity;
import com.example.shoopyshop.R;
import com.example.shoopyshop.Register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class EditProfileFragment extends Fragment {




    //database components
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;

    Button fab_edit;
    //addd story
    FloatingActionButton fab_add_Story;


    //saved
    SharedPreferences.Editor editor;

    ProgressDialog pd;

    //firebase storage
    StorageReference storageReference;
    String storagePath="Users_Profile_Cover_Imgs";


    //recycle view
    RecyclerView postsRecycleView;


    //refresh button
    SwipeRefreshLayout refresh;


    //permission
    private static final int CAMERA_REQUEST_CODE=100;
    private static final int STORAGE_REQUEST_CODE=200;
    private static final int IMAGE_PICK_GALLERY_CODE=300;
    private static final int IMAGE_PICK_CAMERA_CODE=400;
    private static final int IMAGE_PICK_GALLERY_CODE_FOR_STORY=500;

    //SET OF PERMISSION
    String camerapermission[];
    String storagepermission[];

    String uid;


    //uri of pick image
    Uri uri_image;


    //profile
    ImageView avatarIv,backimgaa,storyimg;
    EditText namet,emailt,phonet;
    TextView recovertext;

    String profileORcoverphoto;

    String bimg;
    String email;
    String image;
    String phone;
    String name;

    Button updateBtn;
    Switch switchCompat;
    //for setting dark mode
    Switch dark_light_mode;


    private SlideshowViewModel slideshowViewModel;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel = ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_editprofile, container, false);

        //here is my code

        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");
        storageReference = getInstance().getReference();
        checkforuserlogin();
        //init progress dialog
        pd=new ProgressDialog(getContext(),4);

        editor= PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        avatarIv=root.findViewById(R.id.profileIv);
        namet=root.findViewById(R.id.nameEt);
        fab_edit=root.findViewById(R.id.fab_edit);
        updateBtn=root.findViewById(R.id.updateBtn);
        emailt=root.findViewById(R.id.emailEt);
        phonet=root.findViewById(R.id.phonesEt);
        backimgaa=root.findViewById(R.id.backimg);
        recovertext=root.findViewById(R.id.recover_pass);
//        dark_light_mode=findViewById(R.id.siwtchh);


        //data base
        Query query=databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    bimg=""+ds.child("backimg").getValue();
                    email=""+ds.child("email").getValue();
                    image=""+ds.child("image").getValue();
                    phone=""+ds.child("phone").getValue();
                    name=""+ds.child("name").getValue();


                    namet.setText(name);
                    phonet.setText(phone);
                    emailt.setText(email);

                    try{
                        Picasso.get().load(image).into(avatarIv);
                        Picasso.get().load(bimg).into(backimgaa);

                    }catch (Exception e){
                        Picasso.get().load(R.drawable.profileicon).into(avatarIv);
                        Picasso.get().load(R.drawable.buttondesign).into(backimgaa);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showeditDialog();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //update values with edit text

                String name,email,phone;
                name=namet.getText().toString();
                email=emailt.getText().toString();
                phone=phonet.getText().toString();

                updateProfileinfo(name,email,phone);

            }
        });
        recovertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showrecoveryAlreatbox();
            }
        });

        return root;
    }



    private void showrecoveryAlreatbox() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Recover Password");

        //set layout  linear layout
        LinearLayout linearLayout= new LinearLayout(getContext());

        final EditText emailEt=new EditText(getContext());
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailEt);
        emailEt.setMinEms(16);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email=emailEt.getText().toString().trim();

                beginrecover(email);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd.dismiss();
            }
        });

        builder.create().show();

    }

    private void beginrecover(String email) {
        pd.setMessage("Logging...");
        pd.show();
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(), "email sent", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "failed..", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }



    private void updateProfileinfo(String name, String email, String phone) {

        HashMap<String, Object> result=new HashMap<>();
        result.put("name",name);
        result.put("email",email);
        result.put("phone",phone);

        databaseReference.child(user.getUid()).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //  pd.dismiss();
                startActivity(new Intent(getContext(), MainActivity.class));
                Toast.makeText(getActivity(), "updated", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //  pd.dismiss();
                Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    private boolean checkStoragePermission(){
        boolean result= ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);

        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission(){
        requestPermissions(storagepermission,STORAGE_REQUEST_CODE);
    }


    private boolean checkCameraPermission(){
        boolean result= ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);

        boolean result1= ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);

        return result&&result1;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission(){
        requestPermissions(camerapermission,CAMERA_REQUEST_CODE);

    }



    //update info
    private void showeditDialog() {
        String options[]={"Edit Profile Picture","Edit Cover Image"};
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext(),4);

        builder.setTitle("Choose Action");


        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    pd.setMessage("updating picture");
                    profileORcoverphoto="image";
                    showimagediaplog();
                }else if(which==1){
                    profileORcoverphoto="backimg";
                    showimagediaplog();
                    pd.setMessage("updating Background Image");
                }
            }
        });


        builder.create().show();
    }


    private void showimagediaplog() {
        String options[]={"Camera","Gallery"};
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());

        builder.setTitle("Choose Action");


        builder.setItems(options, new DialogInterface.OnClickListener() {
            @SuppressLint("NewApi")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else {
                        pickFromCamera();
                    }

                }else if(which==1) {
                    if (!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else {
                        pickFromGallery();
                    }
                }
            }
        });


        builder.create().show();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted= grantResults[0]== PackageManager.PERMISSION_GRANTED;
                    boolean wirtewstorageaccepted= grantResults[1]== PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted&&wirtewstorageaccepted){
                        pickFromCamera();
                    }
                    else {
                        Toast.makeText(getContext(), "please allow permission", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean wirtewstorageaccepted= grantResults[0]== PackageManager.PERMISSION_GRANTED;
                    if(wirtewstorageaccepted){
                        pickFromGallery();
                    }
                    else {
                        Toast.makeText(getContext(), "please allow permission", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;

        }

        //    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {


                uri_image = data.getData();
                uploadprofilecoverphoto(uri_image);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {

                uri_image = data.getData();
                uploadprofilecoverphoto(uri_image);
            }
        }
    }





    private void uploadprofilecoverphoto(Uri uri_image) {
        pd.show();

        String filePathAndName = storagePath+"_"+profileORcoverphoto+"_"+user.getUid();

        StorageReference storageReference2nd =storageReference.child(filePathAndName);
        storageReference2nd.putFile(uri_image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // image uploading
                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();


                while(!uriTask.isSuccessful());

                if(uriTask.isSuccessful()){
                    final String downloadUri=uriTask.getResult().toString();

                    //   @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") HashMap<String,Object>results=new HashMap<>();
                    // results.put(profileORcoverphoto, Objects.requireNonNull(downloadUri).toString());


                    //   Toast.makeText(getActivity(), "image"+downloadUri.toString(), Toast.LENGTH_SHORT).show();


                    databaseReference.child(user.getUid()).child(profileORcoverphoto).setValue(downloadUri)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Image uploaded", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getActivity(), "error occured", Toast.LENGTH_SHORT).show();
                        }
                    });


                    //if user edit his name ,also change  it from hist post
                    if(profileORcoverphoto.equals("image")){
                        DatabaseReference ref =FirebaseDatabase.getInstance().getReference("Posts");
                        Query query=ref.orderByChild("uid").equalTo(uid);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot ds:dataSnapshot.getChildren()){
                                    String child=ds.getKey();
                                    dataSnapshot.getRef().child(child).child("uDp").setValue(downloadUri);
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        //update user Image in current userr comment on psots
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot ds:dataSnapshot.getChildren()){
                                    String child=ds.getKey();
                                    if(dataSnapshot.child(child).hasChild("Comments")) {
                                        String child1 =""+dataSnapshot.child(child).getKey();
                                        Query child2=FirebaseDatabase.getInstance().getReference("Posts").child(child1).child("Comments").orderByChild("uid").equalTo(uid);
                                        child2.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot ds:dataSnapshot.getChildren()){
                                                    String child=ds.getKey();
                                                    dataSnapshot.getRef().child(child).child("uDp").setValue(downloadUri);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }
                else {
                    // pd.dismiss();
                    //  Toast.makeText(getActivity(), "some error occured", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void pickFromGallery() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image"),IMAGE_PICK_GALLERY_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void pickFromCamera() {
        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");
        //put image uri
        uri_image= requireActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri_image);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void checkforuserlogin() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

            uid=user.getUid();


        }
        else{
            startActivity(new Intent(getContext(), Register.class));

            try {
                getActivity().finish();
            }catch (NullPointerException ignored){

            }
        }
    }







}
