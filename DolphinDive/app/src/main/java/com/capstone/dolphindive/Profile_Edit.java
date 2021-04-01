package com.capstone.dolphindive;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.capstone.dolphindive.utility.CircleTransform;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class Profile_Edit extends AppCompatActivity {
    EditText et_name, et_email, et_DiverId, et_phone, et_address;
    Button btn;
    ProgressBar progressBar;
    ImageView imageView;
    TextView comment;
    private Uri imageUri;
    private static final int PICK_IMAGE=1;
    UploadTask uploadTask;
    FirebaseStorage firebaseStorage;
    FirebaseUser user;
    StorageReference storageReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    String mode;
    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        et_name = findViewById(R.id.name_profileEdit);
        et_email = findViewById(R.id.email_profileEdit);
        et_DiverId = findViewById(R.id.diverID_profileEdit);
        et_phone = findViewById(R.id.phone_profileEdit);
        et_address = findViewById(R.id.address_profileEdit);
        btn = findViewById(R.id.save_profile_profileEdit);
        progressBar = findViewById(R.id.progressbar_profileEdit);
        imageView = findViewById(R.id.image_view_profileEdit);
        comment = findViewById(R.id.comment_profileEdit);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        documentReference = db.collection("Users").document(uid);
        storageReference = firebaseStorage.getInstance().getReference("Profile Images");

        //et_email.setText(user.getEmail());

        Bundle bundle = getIntent().getExtras();
        mode = bundle.getString("mode");
        if(mode.equals("edit")){
            comment.setText("Add new photo or use the older one");
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode.equals("create")){
                    UploadData();
                }else if(mode.equals("edit")){
                    EditData();
                }else{
                    Toast.makeText(Profile_Edit.this, "System error:No Mode specified",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void EditData() {
        String name = et_name.getText().toString();
        String email = et_email.getText().toString();
        String phone = et_phone.getText().toString();
        String address = et_address.getText().toString();
        String diverId = et_DiverId.getText().toString();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email)){
            if(imageUri!=null ){

                progressBar.setVisibility(View.VISIBLE);
                final StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+
                        getFileExt(imageUri));
                uploadTask = reference.putFile(imageUri);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        return reference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Uri downloadUri = task.getResult();

                            final DocumentReference sfDocRef = db.collection("Users").document(uid);

                            db.runTransaction(new Transaction.Function<Void>() {
                                @Override
                                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                    DocumentSnapshot snapshot = transaction.get(sfDocRef);

                                    //transaction.update(sfDocRef, "population", newPopulation);
                                    transaction.update(sfDocRef, "name", name);
                                    transaction.update(sfDocRef, "email", email);
                                    transaction.update(sfDocRef, "phone", phone);
                                    transaction.update(sfDocRef, "address", address);
                                    transaction.update(sfDocRef, "diverId", diverId);
                                    transaction.update(sfDocRef, "url", downloadUri.toString());

                                    // Success
                                    return null;
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(Profile_Edit.this, "Profile Updated",Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(Profile_Edit.this, Profile_Show.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Profile_Edit.this, "failed",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
            }else{

                final DocumentReference sfDocRef = db.collection("Users").document(uid);

                db.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(sfDocRef);

                        //transaction.update(sfDocRef, "population", newPopulation);
                        transaction.update(sfDocRef, "name", name);
                        transaction.update(sfDocRef, "email", email);
                        transaction.update(sfDocRef, "phone", phone);
                        transaction.update(sfDocRef, "address", address);
                        transaction.update(sfDocRef, "diverId", diverId);
                        //transaction.update(sfDocRef, "url", downloadUri.toString());

                        // Success
                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(Profile_Edit.this, "Profile Updated",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Profile_Edit.this, Profile_Show.class);
                        startActivity(intent);
                        finish();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Profile_Edit.this, "failed",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }else{
            Toast.makeText(Profile_Edit.this, "Name and email are Required",Toast.LENGTH_SHORT).show();
        }
    }



    private void UploadData() {
        String name = et_name.getText().toString();
        String email = et_email.getText().toString();
        String phone = et_phone.getText().toString();
        String address = et_address.getText().toString();
        String diverId = et_DiverId.getText().toString();

        if(  !TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) ) {
            if(imageUri!=null){
                progressBar.setVisibility(View.VISIBLE);
                final StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+
                        getFileExt(imageUri));
                uploadTask = reference.putFile(imageUri);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        return reference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Uri downloadUri = task.getResult();

                            Map<String, String> profile = new HashMap<>();
                            profile.put("name", name);
                            profile.put("email", email);
                            profile.put("phone", phone);
                            profile.put("address", address);
                            profile.put("diverId", diverId);
                            profile.put("url", downloadUri.toString());


                            documentReference.set(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(Profile_Edit.this, "Profile Created",Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(Profile_Edit.this, Profile_Show.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Profile_Edit.this, "failed",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
            }else{
                Map<String, String> profile = new HashMap<>();
                profile.put("name", name);
                profile.put("email", email);
                profile.put("phone", phone);
                profile.put("address", address);
                profile.put("diverId", diverId);
                profile.put("url", "");


                documentReference.set(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(Profile_Edit.this, "Profile Created",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Profile_Edit.this, Profile_Show.class);
                        startActivity(intent);
                        finish();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Profile_Edit.this, "failed",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }else{
            Toast.makeText(Profile_Edit.this, "Name and email are Required",Toast.LENGTH_SHORT).show();
        }
    }

    public void ChooseImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE || resultCode == RESULT_OK ||
                data!=null || data.getData()!=null){
            imageUri = data.getData();
            Picasso.get().load(imageUri).transform(new CircleTransform()).centerCrop().fit().into(imageView);
        }
    }

    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onStart() {
        super.onStart();

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    String name = task.getResult().getString("name");
                    String email = task.getResult().getString("email");
                    String phone = task.getResult().getString("phone");
                    String address = task.getResult().getString("address");
                    String diverId = task.getResult().getString("diverId");
                    String url = task.getResult().getString("url");

                    if(mode.equals("create")){
                        Picasso.get().load(url).transform(new CircleTransform()).centerCrop().fit().into(imageView);
                    }
                    et_name.setText(name);
                    et_email.setText(email);
                    et_phone.setText(phone);
                    et_address.setText(address);
                    et_DiverId.setText(diverId);

                }else{
                    //Toast.makeText(Profile_Edit.this, "No Profile Exist",Toast.LENGTH_SHORT).show();
                }

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }
}