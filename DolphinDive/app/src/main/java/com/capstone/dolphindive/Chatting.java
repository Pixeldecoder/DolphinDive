package com.capstone.dolphindive;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.capstone.dolphindive.utility.Message;
import com.capstone.dolphindive.utility.MessageAdapter;
import com.capstone.dolphindive.utility.User;
import com.capstone.dolphindive.utility.UserProfile;
import com.capstone.dolphindive.utility.UserProfileCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chatting extends AppCompatActivity {
    CircleImageView profile_image;
    TextView username;

    FirebaseUser fuser;
    DatabaseReference chatReference;
    DatabaseReference userReference;

    ImageButton btn_send;
    EditText text_send;

    MessageAdapter messageAdapter;
    List<Message> mchat;

    RecyclerView recyclerView;

    Intent intent;

    ValueEventListener seenListener;

    String userid;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private static final String TAG = "Chatting";

    private String mUsername;
    private String mPhotoUrl;

    public static final String ANONYMOUS = "anonymous";
    private static final String LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif";
    private static DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    private DatabaseReference mFirebaseDatabaseReference;
    private ImageView mAddMessageImageView;
    private ImageView mAddAudioImageView;
    private Button mRecordButton;
    private MediaRecorder recorder = null;
    private static final int REQUEST_IMAGE = 2;

    boolean notify = false;

    private String fileName;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(Chatting.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int permission = ContextCompat.checkSelfPermission(Chatting.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            Chatting.this.requestPermissions(PERMISSIONS_STORAGE,
                   1);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        File cacheFile = new File(getApplicationContext().getCacheDir(), "tempAudioCapture.3gp");
        fileName = cacheFile.getAbsolutePath();

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        mRecordButton = findViewById(R.id.record_button);
        text_send = findViewById(R.id.text_send);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            Toast.makeText(getApplicationContext(), "Chat Function requires an account log-in", Toast.LENGTH_LONG).show();
            finish();
            return;
        }else {
            fuser = FirebaseAuth.getInstance().getCurrentUser();
        }


        chatReference = FirebaseDatabase.getInstance().getReference("Chats");
        userReference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        getUserProfileImg();

        userid = getIntent().getExtras().getString("userid");
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                String msg = text_send.getText().toString();
                if (!msg.equals("")){
                    sendMessage(fuser.getUid(), userid, msg, mPhotoUrl, "");
                } else {
                    Toast.makeText(Chatting.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

//        userReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
//
//        userReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//                username.setText(user.getSearch());
//                if (user.getImageURL().equals("default")){
//                    profile_image.setImageResource(R.mipmap.ic_launcher);
//                } else {
//                    //and this
//                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
//                }
//                readMessages(fuser.getUid(), userid, user.getImageURL());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//        });

        mAddAudioImageView = findViewById(R.id.addMessageAudioView);

        mAddAudioImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hasRecordAudioPermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
                if (hasRecordAudioPermission == PackageManager.PERMISSION_GRANTED) {
                    if (mRecordButton.getVisibility() == View.GONE){
                        mRecordButton.setVisibility(View.VISIBLE);
                        text_send.setVisibility(View.GONE);
                    } else {
                        mRecordButton.setVisibility(View.GONE);
                        text_send.setVisibility(View.VISIBLE);
                    }
                }else{
                    requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                            REQUEST_CODE_ASK_PERMISSIONS);
                }
            }
        });

        mRecordButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    startRecording();
                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    stopRecording();
                }
                return false;
            }
        });

        UserProfile profile = new UserProfile(userid);
        profile.getProfile(new UserProfileCallback() {
            @Override
            public void onComplete(HashMap<String, String> profile) {
                String url = profile.get("url");
                username.setText(profile.get("name"));
                if (TextUtils.isEmpty(url) || url==null){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                    url = "default";
                } else {
                    //and this
                    Glide.with(getApplicationContext()).load(url).into(profile_image);
                }
                readMessages(fuser.getUid(), userid, url);
            }
        });

        mAddMessageImageView = (ImageView) findViewById(R.id.addMessageImageView);
        mAddMessageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });


        seenMessage(userid);
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
            Log.e(TAG, e.toString());
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;

        uploadAudio();
    }

    private void uploadAudio() {
        StorageReference chatsAudioStorageReference =
                FirebaseStorage.getInstance()
                        .getReference("Chats Audio");

        Uri uri = Uri.fromFile(new File(fileName));
        Log.d(TAG, "Uri: " + uri.toString());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", fuser.getUid());
        hashMap.put("receiver", userid);
        hashMap.put("message", null);
        hashMap.put("isseen", false);
        hashMap.put("photoUrl",  mPhotoUrl);
        hashMap.put("fileUrl", "audio_default");
        hashMap.put("type","audio");

        reference.child("Chats").push().setValue(hashMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                chatsAudioStorageReference.putFile(uri).addOnCompleteListener(Chatting.this,
                        new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                String key = ref.getKey();
                                if (task.isSuccessful()) {
                                    task.getResult().getMetadata().getReference().getDownloadUrl()
                                            .addOnCompleteListener(Chatting.this,
                                                    new OnCompleteListener<Uri>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Uri> task) {
                                                            if (task.isSuccessful()) {
                                                                Message Message =
                                                                        new Message(fuser.getUid(), userid, null, false, mPhotoUrl,
                                                                                task.getResult().toString(), "audio");
                                                                mFirebaseDatabaseReference.child("Chats").child(key)
                                                                        .setValue(Message);
                                                            }
                                                        }
                                                    });
                                } else {
                                    Log.w(TAG, "Image upload task was not successful.",
                                            task.getException());
                                }
                            }
                        });
            }
        });

        chatsAudioStorageReference.putFile(uri);
    }

    private void seenMessage(final String userid){
        seenListener = chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Message chat = snapshot.getValue(Message.class);
                    if (chat.getReceiver().equals(fuser.getUid()) && chat.getSender().equals(userid)){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender, final String receiver, String message, String photoUrl, String fileUrl){

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);
        hashMap.put("photoUrl", photoUrl);
        hashMap.put("fileUrl", fileUrl);
        hashMap.put("type", "message");

        reference.child("Chats").push().setValue(hashMap);


        // add user to chat fragment
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(fuser.getUid())
                .child(userid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(userid);
                }
                if(fileUrl == ""){
                    chatRef.child("message").setValue(message);
                }else{
                    chatRef.child("message").setValue("[image]");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(userid)
                .child(fuser.getUid());
        chatRefReceiver.child("id").setValue(fuser.getUid());
        if(message != null){
            chatRefReceiver.child("message").setValue(message);
        }else{
            chatRefReceiver.child("message").setValue("[image]");
        }

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (notify) {
                    //sendNotifiaction(receiver, user.getUsername(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessages(final String myid, final String userid, final String imageurl){
        mchat = new ArrayList<>();
        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Message chat = snapshot.getValue(Message.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        mchat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(Chatting.this, mchat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void currentUser(String userid){
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentuser", userid);
        editor.apply();
    }

    private void status(String status){
        userReference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        userReference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
        currentUser(userid);
    }

    @Override
    protected void onPause() {
        super.onPause();
        chatReference.removeEventListener(seenListener);
        status("offline");
        currentUser("none");
    }

    private void getUserProfileImg() {
        userReference.child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
//                    String userFullName = dataSnapshot.child("username").getValue().toString();
                    String userProfileImg = dataSnapshot.child("imageURL").getValue().toString();
                    mPhotoUrl = userProfileImg;
//                    current_user_name =  userFullName;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getUserProfileImg() {
        userReference.child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
//                    String userFullName = dataSnapshot.child("username").getValue().toString();
                    String userProfileImg = dataSnapshot.child("imageURL").getValue().toString();
                    mPhotoUrl = userProfileImg;
//                    current_user_name =  userFullName;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    final Uri uri = data.getData();
                    Log.d(TAG, "Uri: " + uri.toString());

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("sender", fuser.getUid());
                    hashMap.put("receiver", userid);
                    hashMap.put("message", null);
                    hashMap.put("isseen", false);
                    hashMap.put("photoUrl",  mPhotoUrl);
                    hashMap.put("fileUrl", LOADING_IMAGE_URL);
                    hashMap.put("type", "image");

                    reference.child("Chats").push().setValue(hashMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError,
                                               DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                String key = databaseReference.getKey();
                                StorageReference chatsImageStorageReference =
                                        FirebaseStorage.getInstance()
                                                .getReference("Chats Images")
                                                .child(key)
                                                .child(uri.getLastPathSegment());

                                putImageInStorage(chatsImageStorageReference, uri, key);
                            } else {
                                Log.w(TAG, "Unable to write message to database.",
                                        databaseError.toException());
                            }
                        }
                    });

                    // add user to chat fragment
                    final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                            .child(fuser.getUid())
                            .child(userid);

                    chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()){
                                chatRef.child("id").setValue(userid);
                                chatRef.child("message").setValue("[Image]");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                            .child(userid)
                            .child(fuser.getUid());
                    chatRefReceiver.child("id").setValue(fuser.getUid());
                    chatRefReceiver.child("message").setValue("[Image]");

                    userReference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                    userReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            if (notify) {
                                //sendNotifiaction(receiver, user.getUsername(), msg);
                            }
                            notify = false;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        }
    }

    private void putImageInStorage(StorageReference storageReference, Uri uri, final String key) {
        storageReference.putFile(uri).addOnCompleteListener(Chatting.this,
                new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            task.getResult().getMetadata().getReference().getDownloadUrl()
                                    .addOnCompleteListener(Chatting.this,
                                            new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task) {
                                                    if (task.isSuccessful()) {
                                                        Message Message =
                                                                new Message(fuser.getUid(), userid, null, false, mPhotoUrl,
                                                                        task.getResult().toString(), "image");
                                                        mFirebaseDatabaseReference.child("Chats").child(key)
                                                                .setValue(Message);
                                                    }
                                                }
                                            });
                        } else {
                            Log.w(TAG, "Image upload task was not successful.",
                                    task.getException());
                        }
                    }
                });
    }

}
