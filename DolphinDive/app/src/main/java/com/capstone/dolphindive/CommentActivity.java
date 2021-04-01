package com.capstone.dolphindive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.dolphindive.utility.Comments;
import com.capstone.dolphindive.utility.Posts;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
//import xyz.hanks.library.bang.SmallBangView;


public class CommentActivity extends AppCompatActivity {

    private ProgressDialog loadingBar;
    private RecyclerView commentList;
    private Toolbar mToolbar;
    private DatabaseReference UsersRef, PostsRef, userPostRef, CommentsRef;
    private FirebaseAuth mAuth;
    private String current_user_id;
    private TextView fullName, date, description, time, likes, uid, commentCounter;
    private CircleImageView profileImage, userProfileImage;
    private ImageView postImage;
    private Button addComment;
    private EditText new_comment;
    private String comment, saveCurrentDate, saveCurrentTime, new_commentCounter;

    private StorageReference PostImageReference;

//    private UploadTask uploadTask;

//    private String saveCurrentDate, saveCurrentTime, postRandomName, downloadUrl, current_user_id;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        fullName = (TextView) findViewById(R.id.post_user_name);
        date =  (TextView) findViewById(R.id.post_date);
        description =  (TextView) findViewById(R.id.description);
        time  =  (TextView) findViewById(R.id.post_time);
//        uid = date = findViewById(R.id.post_uid);
        postImage = (ImageView) findViewById(R.id.post_image);
        profileImage = (CircleImageView) findViewById(R.id.post_profile_image);
        likes =  (TextView) findViewById(R.id.likeCounter);
        commentCounter =  (TextView) findViewById(R.id.commentCounter);
        userProfileImage= (CircleImageView) findViewById(R.id.user_image);

        mToolbar = (Toolbar) findViewById(R.id.update_post_tool_bar);

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        mToolbar = (Toolbar) findViewById(R.id.update_post_tool_bar);

        PostImageReference = FirebaseStorage.getInstance().getReference();
//        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        String postID = getIntent().getStringExtra("PostID");
        userPostRef = PostsRef.child(postID);
        CommentsRef = FirebaseDatabase.getInstance().getReference().child("Comments").child(postID);

        loadingBar = new ProgressDialog(this);

        fullName.setText(getIntent().getStringExtra("fullName"));
        date.setText(getIntent().getStringExtra("date"));
        description.setText(getIntent().getStringExtra("description"));
        time.setText(getIntent().getStringExtra("time"));
        likes.setText(getIntent().getStringExtra("likes"));
        commentCounter.setText(getIntent().getStringExtra("commentCounter"));
        Picasso.get().load(getIntent().getStringExtra("profileImage")).into(profileImage);
        Picasso.get().load(getIntent().getStringExtra("postImage")).into(postImage);
        Picasso.get().load(getIntent().getStringExtra("userProfileImage")).into(userProfileImage);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("COMMENTS");

        new_comment = (EditText) findViewById(R.id.new_comment);
        addComment = (Button) findViewById(R.id.addComment);
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateComment(v);
            }
        });


        commentList = (RecyclerView) findViewById(R.id.comment_list);
        commentList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        commentList.setLayoutManager(linearLayoutManager);
        DisplayAllComments();
    }

    private void validateComment(View v){
        comment = new_comment.getText().toString();
        if(TextUtils.isEmpty(comment)){
            Toast.makeText(CommentActivity.this,"Comment is Empty", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("COMMENT");
            loadingBar.setMessage("Uploading your new comment...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            SavingCommentToDatabase(v);
        }
    }

    private void SavingCommentToDatabase(View v){
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        UsersRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String userFullName = dataSnapshot.child("username").getValue().toString();
                    String userProfileImg = dataSnapshot.child("imageURL").getValue().toString();

                    HashMap commentMap = new HashMap();
                    commentMap.put("uid", current_user_id);
                    commentMap.put("date", saveCurrentDate);
                    commentMap.put("time", saveCurrentTime);
                    commentMap.put("content", comment);
                    commentMap.put("profileimage", userProfileImg);
                    commentMap.put("fullname", userFullName);

                    CommentsRef.child(current_user_id + saveCurrentDate + saveCurrentTime).updateChildren(commentMap)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){
                                        userPostRef.runTransaction(new Transaction.Handler() {
                                            @NonNull
                                            @Override
                                            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                                Posts p = currentData.getValue(Posts.class);
                                                if (p==null){ return Transaction.success(currentData);}
                                                new_commentCounter = String.valueOf(Integer.parseInt(p.getCommentCounter())+1);
                                                p.setCommentCounter(new_commentCounter);
                                                currentData.setValue(p);
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        commentCounter.setText(new_commentCounter);
                                                    }
                                                });
                                                return Transaction.success(currentData);
                                            }

                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                                                loadingBar.dismiss();
                                                new_comment.getText().clear();
                                                Toast.makeText(CommentActivity.this,"Comment Uploaded Successfully",Toast.LENGTH_SHORT).show();
                                                hideKeybaord(v);
                                            }
                                        });

                                    }
                                    else{
                                        Toast.makeText(CommentActivity.this,"Error Occured",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void DisplayAllComments(){
        FirebaseRecyclerOptions<Comments> options =
                new FirebaseRecyclerOptions.Builder<Comments>()
                        .setQuery(CommentsRef, Comments.class)
                        .build();

        FirebaseRecyclerAdapter<Comments, CommentActivity.CommentsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Comments, CommentActivity.CommentsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CommentActivity.CommentsViewHolder holder, int position, @NonNull Comments model) {
                        holder.fullName.setText(model.getFullname());
                        holder.date.setText(model.getDate());
                        holder.content.setText(model.getContent());
                        holder.time.setText(model.getTime());
//                            holder.uid.setText(model.getUid());
                        Picasso.get().load(model.getProfileimage()).into(holder.profileImage);

                    }
                    @NonNull
                    @Override
                    public CommentActivity.CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_comments_layout, parent, false);
                        CommentActivity.CommentsViewHolder viewHolder = new CommentActivity.CommentsViewHolder(view);
                        return viewHolder;
                    }
                };
        commentList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class CommentsViewHolder extends RecyclerView.ViewHolder{
        TextView fullName, date, content, time, uid;
        CircleImageView profileImage;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.post_user_name);
            date = itemView.findViewById(R.id.post_date);
            content = itemView.findViewById(R.id.text);
            time  = itemView.findViewById(R.id.post_time);
//                uid = date = itemView.findViewById(R.id.post_uid);
            profileImage = itemView.findViewById(R.id.post_profile_image);

        }
    }

    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
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
    }
}