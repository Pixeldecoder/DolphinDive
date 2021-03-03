package com.capstone.dolphindive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.TextView;

import com.capstone.dolphindive.utility.Posts;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SocialPlatform extends Fragment {
        @Nullable

        private ImageView appName;
        private ImageView appIcon;
        private RecyclerView postList;
        private ImageButton Notification;
        private ImageButton AddNewPostButton;
        private DatabaseReference PostsRef;
        View view;
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            view = inflater.inflate(R.layout.activity_social_platform, container, false);

            AddNewPostButton = (ImageButton) view.findViewById(R.id.add_new_post);
            AddNewPostButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent addNewPostIntent = new Intent(getActivity(), PostActivity.class);
                    startActivity(addNewPostIntent);
                }
            });

            Notification = (ImageButton) view.findViewById(R.id.notification);
            Notification.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    getParentFragmentManager().beginTransaction().replace(R.id.post_container,
                            new NotifyActivity()).commit();
                }
//                public void onBackPressed(View v){
//                    getParentFragmentManager().beginTransaction().replace(R.id.post_container,
//                            new SocialPlatform()).commit();
//                }
            });

            PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

            postList = (RecyclerView) view.findViewById(R.id.post_list);
            postList.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            postList.setLayoutManager(linearLayoutManager);

            DisplayAllUsersPosts();

            return view;
        }


        private void DisplayAllUsersPosts(){
            FirebaseRecyclerOptions<Posts> options =
                    new FirebaseRecyclerOptions.Builder<Posts>()
                            .setQuery(PostsRef, Posts.class)
                            .build();

            FirebaseRecyclerAdapter<Posts, PostsViewHolder> firebaseRecyclerAdapter =
                    new FirebaseRecyclerAdapter<Posts, PostsViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull PostsViewHolder holder, int position, @NonNull Posts model) {
                            holder.fullName.setText(model.getFullname());
                            holder.date.setText(model.getDate());
                            holder.description.setText(model.getDescription());
                            holder.time.setText(model.getTime());
//                            holder.uid.setText(model.getUid());
                            Picasso.get().load(model.getProfileimage()).into(holder.profileImage);
                            Picasso.get().load(model.getPostimage()).into(holder.postImage);
                        }

                        @NonNull
                        @Override
                        public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_posts_layout, parent, false);
                            PostsViewHolder viewHolder = new PostsViewHolder(view);
                            return viewHolder;
                        }
                    };
            postList.setAdapter(firebaseRecyclerAdapter);
            firebaseRecyclerAdapter.startListening();
        }

        public static class PostsViewHolder extends RecyclerView.ViewHolder{
            TextView fullName, date, description, time, uid;
            CircleImageView profileImage;
            ImageView postImage;
            public PostsViewHolder(@NonNull View itemView) {
                super(itemView);
                fullName = itemView.findViewById(R.id.post_user_name);
                date = itemView.findViewById(R.id.post_date);
                description = itemView.findViewById(R.id.description);
                time  = itemView.findViewById(R.id.post_time);
//                uid = date = itemView.findViewById(R.id.post_uid);
                postImage = itemView.findViewById(R.id.post_image);
                profileImage = itemView.findViewById(R.id.post_profile_image);
            }
        }

    }