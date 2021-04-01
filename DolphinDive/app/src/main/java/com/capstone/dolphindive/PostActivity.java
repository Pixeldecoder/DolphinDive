package com.capstone.dolphindive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class PostActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ProgressDialog loadingBar;
    private ImageButton SelectImg;
    private Button PostButton;
    private EditText PostDescription;

    private static final int Gallery_Pick = 1;
    private Uri ImageUri;
    private String Description;

    private StorageReference PostImageReference;
    private DatabaseReference PostsRef;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private CollectionReference UserRef;
    private FirebaseAuth mAuth;
    private UploadTask uploadTask;

    private String saveCurrentDate, saveCurrentTime, postRandomName, downloadUrl, current_user_id;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Users").document(current_user_id).collection("posts");
        UserRef = db.collection("Users");

        PostImageReference = FirebaseStorage.getInstance().getReference();
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        SelectImg = (ImageButton) findViewById(R.id.select_image);
        PostButton = (Button) findViewById(R.id.submit);
        PostDescription = (EditText) findViewById(R.id.editText);
        mToolbar = (Toolbar) findViewById(R.id.update_post_tool_bar);
        loadingBar = new ProgressDialog(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("POST");

        SelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        PostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePostInfo();
            }
        });
    }

    private void validatePostInfo() {
        Description = PostDescription.getText().toString();
        if(ImageUri == null){
            Toast.makeText(this,"Please select image first...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Description)){
            Toast.makeText(this,"Please say something about the photo...", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Add new post");
            loadingBar.setMessage("Uploading your new post...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            StoringImageToFirebaseStorage();

        }
    }

    private void StoringImageToFirebaseStorage() {
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        postRandomName = saveCurrentDate + saveCurrentTime;

        String path = ImageUri.getPath();
        String imgName = path.substring(path.lastIndexOf('/') + 1);
        if(imgName.lastIndexOf('.') != -1){
            imgName = imgName.substring(0,imgName.lastIndexOf('.'));
        }

        StorageReference filePath = PostImageReference.child("Post Images").child(imgName + postRandomName + ".jpg");
        uploadTask = filePath.putFile(ImageUri);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return filePath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadUrl = task.getResult().toString();
                    Toast.makeText(PostActivity.this, "Post successfully!",  Toast.LENGTH_SHORT).show();
                    SavingPostInformationToDatabase();
                } else {
                    String message = task.getException().getMessage();
                    Toast.makeText(PostActivity.this, "Error occured: " + message,  Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void SavingPostInformationToDatabase() {
        UserRef.document(current_user_id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.exists()){
                    String userFullName = value.get("name").toString();
                    String userProfileImg = value.get("url").toString();

                    HashMap postsMap = new HashMap();
                    postsMap.put("uid", current_user_id);
                    postsMap.put("date", saveCurrentDate);
                    postsMap.put("time", saveCurrentTime);
                    postsMap.put("description", Description);
                    postsMap.put("postimage", downloadUrl);
                    postsMap.put("profileimage", userProfileImg);
                    postsMap.put("fullname", userFullName);
                    postsMap.put("likes", "0");
                    postsMap.put("newLikes", "0");
                    postsMap.put("liker", "");
                    postsMap.put("newLiker", "");
                    postsMap.put("commentCounter", "0");
                    postsMap.put("comments", null);
                    postsMap.put("timestamp", ServerValue.TIMESTAMP);

                    HashMap userPostsMap = new HashMap();
                    userPostsMap.put("date", saveCurrentDate);
                    userPostsMap.put("time", saveCurrentTime);
                    userPostsMap.put("description", Description);
                    userPostsMap.put("postimage", downloadUrl);
                    userPostsMap.put("timestamp", FieldValue.serverTimestamp());

                    collectionReference.document(current_user_id + postRandomName).set(userPostsMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            ;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Error","Error writting document", e);
                        }
                    });

                    PostsRef.child(current_user_id + postRandomName).updateChildren(postsMap)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){
                                        SendUserToMainActivity();
                                        Toast.makeText(PostActivity.this,"Post Uploaded Successfully",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                    else{
                                        Toast.makeText(PostActivity.this,"Error Occured",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery_Pick && resultCode==RESULT_OK && data!=null){
            ImageUri = data.getData();
            SelectImg.setImageURI(ImageUri);
        }
    }

    @Override
    public  boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == android.R.id.home){
            SendUserToMainActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void SendUserToMainActivity() {
        finish();
//        Intent mainIntent = new Intent(PostActivity.this, SocialPlatform.class);
//        startActivity(mainIntent);
    }
}