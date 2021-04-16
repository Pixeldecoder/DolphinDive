package com.capstone.dolphindive;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capstone.dolphindive.utility.CircleTransform;
import com.capstone.dolphindive.utility.UserProfile;
import com.capstone.dolphindive.utility.UserProfileCallback;
import com.capstone.dolphindive.utility.UserProfileFollowCallback;
import com.capstone.dolphindive.utility.myPosts;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Social_Profile extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseUser user;
    DocumentReference documentReference;
    DocumentReference following;
    DocumentReference follower;
    Button followBtn;
    Button msgBtn;
    TextView userName;
    TextView numPosts;
    TextView numFollowing ;
    TextView numFollower;
    ImageView portrait;
    String cur_uid;
    String target_uid;

    private RecyclerView postList;
    private Query query;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_profile);

        db=  FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        cur_uid = user.getUid();
        Intent intent = getIntent();
        //Use following to pass the targeting user Uid to the activity
        target_uid = intent.getStringExtra("uid");
        documentReference = db.collection("Users").document(cur_uid);

        following = documentReference.collection("following").document(target_uid);
        follower  = db.collection("Users").document(target_uid).collection("follower").document(cur_uid);

        followBtn= (Button)findViewById(R.id.followBtn);
        msgBtn = (Button) findViewById(R.id.chatBtn);
        userName = (TextView) findViewById(R.id.profile_username);
        numPosts = (TextView) findViewById(R.id.profile_numPosts);
        numFollowing = (TextView) findViewById(R.id.profile_numFol);
        numFollower = (TextView) findViewById(R.id.profile_numFollower);
        portrait = (ImageView)findViewById(R.id.profile_portrait);

        query = db.collection("Users").document(target_uid).collection("posts").orderBy("time");
        postList = (RecyclerView) findViewById(R.id.post_list);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);
        DisplayAllPosts();

        msgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.equals(target_uid, cur_uid)){
                    Intent intent = new Intent(Social_Profile.this, Chatting.class);
                    intent.putExtra("userid", target_uid);
                    startActivity(intent);
                }else{
                    Toast.makeText(Social_Profile.this, "Message failed",Toast.LENGTH_SHORT).show();
                }
            }
        });

        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> fileds = new HashMap<>();
                fileds.put("exist", true);
                following.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(!task.getResult().exists()){
                            following.set(fileds).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    follower.set(fileds).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            UserProfile curUser = new UserProfile(cur_uid);
                                            UserProfile tarUser = new UserProfile(target_uid);
                                            curUser.increaseFollowing(new UserProfileFollowCallback() {
                                                @Override
                                                public void onComplete() {
                                                    tarUser.increaseFollower(new UserProfileFollowCallback() {
                                                        @Override
                                                        public void onComplete() {
                                                            Toast.makeText(Social_Profile.this, "Successfully Followed",Toast.LENGTH_SHORT).show();
                                                            //followBtn.setText("Unfollow");
                                                            finish();
                                                            startActivity(getIntent());
                                                        }
                                                    });
                                                }
                                            });

    //                                finish();
    //                                Intent intent = new Intent(Social_Profile.this, Social_Profile.class);
    //                                intent.putExtra("uid",target_uid);
    //                                startActivity(intent);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Social_Profile.this, "Following Failed",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }else{
                            following.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    follower.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            UserProfile curUser = new UserProfile(cur_uid);
                                            UserProfile tarUser = new UserProfile(target_uid);
                                            curUser.decreaseFollowing(new UserProfileFollowCallback() {
                                                @Override
                                                public void onComplete() {
                                                    tarUser.decreaseFollower(new UserProfileFollowCallback() {
                                                        @Override
                                                        public void onComplete() {
                                                            Toast.makeText(Social_Profile.this, "Successfully Unfollowed",Toast.LENGTH_SHORT).show();
                                                            //followBtn.setText("Follow");
                                                            finish();
                                                            startActivity(getIntent());
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void DisplayAllPosts(){
        FirestoreRecyclerOptions<myPosts> options =
                new FirestoreRecyclerOptions.Builder<myPosts>()
                        .setQuery(query, myPosts.class)
                        .build();

        FirestoreRecyclerAdapter<myPosts, Social_Profile.PostsViewHolder> firestoreRecyclerAdapter =
                new FirestoreRecyclerAdapter<myPosts, Social_Profile.PostsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Social_Profile.PostsViewHolder holder, int position, @NonNull myPosts model) {
                        holder.date.setText(model.getDate());
                        holder.time.setText(model.getTime());
                        holder.description.setText(model.getDescription());
                        Picasso.get().load(model.getPostimage()).into(holder.post_image);
                        holder.delBtn.setVisibility(View.INVISIBLE);
                    }

                    @NonNull
                    @Override
                    public Social_Profile.PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_my_posts, parent, false);
                        Social_Profile.PostsViewHolder viewHolder = new Social_Profile.PostsViewHolder(view);
                        return viewHolder;
                    }
                };
        postList.setAdapter(firestoreRecyclerAdapter);
        firestoreRecyclerAdapter.startListening();
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder{
        ImageButton post_image;
        TextView date, description, time;
        Button delBtn;

        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.text);
            date = itemView.findViewById(R.id.post_date);
            time  = itemView.findViewById(R.id.post_time);
            post_image = itemView.findViewById(R.id.post_image);
            delBtn = itemView.findViewById(R.id.del_btn);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        UserProfile userProfile = new UserProfile(target_uid);
        userProfile.getProfile(new UserProfileCallback() {
            @Override
            public void onComplete(HashMap<String,String> profile) {
                if(profile!=null){
                    String name = profile.get("name");
                    String numFollower_text = profile.get("numFollower");
                    String numFollowing_text = profile.get("numFollowing");
                    String numPosts_text = profile.get("numPosts");
                    String url = profile.get("url");
                    if(!TextUtils.isEmpty(url)){
                        Picasso.get().load(url).transform(new CircleTransform()).centerCrop().fit().into(portrait);
                        userName.setText(name);
                        numFollower.setText(numFollower_text);
                        numFollowing.setText(numFollowing_text);
                        numPosts.setText(numPosts_text);

                    }else{
                        userName.setText(name);
                        numFollower.setText(numFollower_text);
                        numFollowing.setText(numFollowing_text);
                        numPosts.setText(numPosts_text);
                    }
                }else{
                    Toast.makeText(Social_Profile.this, "No Profile Exist",Toast.LENGTH_SHORT).show();
                }
                following.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()){
                            followBtn.setText("Unfollow");
                        }else{
                            followBtn.setText("Follow");
                        }
                    }
                });
            }
        });
    }
}
