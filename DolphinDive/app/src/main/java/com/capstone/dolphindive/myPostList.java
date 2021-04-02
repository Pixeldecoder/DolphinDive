package com.capstone.dolphindive;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capstone.dolphindive.utility.Comments;
import com.capstone.dolphindive.utility.myPosts;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class myPostList extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private RecyclerView postList;
    private String current_user_id;
    private DatabaseReference PostsRef;
    private FirebaseFirestore db;
    private Query query;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        query = db.collection("Users").document(current_user_id).collection("posts").orderBy("time");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        postList = (RecyclerView) findViewById(R.id.post_list);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);
        DisplayAllPosts();
    }

    private void DisplayAllPosts(){

        FirestoreRecyclerOptions<myPosts> options =
                new FirestoreRecyclerOptions.Builder<myPosts>()
                        .setQuery(query, myPosts.class)
                        .build();

        FirestoreRecyclerAdapter<myPosts, myPostList.PostsViewHolder> firestoreRecyclerAdapter =
                new FirestoreRecyclerAdapter<myPosts, PostsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull PostsViewHolder holder, int position, @NonNull myPosts model) {
                        holder.date.setText(model.getDate());
                        holder.time.setText(model.getTime());
                        holder.description.setText(model.getDescription());
                        Picasso.get().load(model.getPostimage()).into(holder.post_image);
                        holder.delBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getSnapshots().getSnapshot(position).getReference().delete();
                                PostsRef.child(getSnapshots().getSnapshot(position).getId()).removeValue();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_my_posts, parent, false);
                        myPostList.PostsViewHolder viewHolder = new myPostList.PostsViewHolder(view);
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
}
