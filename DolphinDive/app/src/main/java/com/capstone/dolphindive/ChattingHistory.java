package com.capstone.dolphindive;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capstone.dolphindive.utility.History;
import com.capstone.dolphindive.utility.UserProfile;
import com.capstone.dolphindive.utility.UserProfileCallback;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChattingHistory extends Fragment{
    private DatabaseReference mFirebaseDatabaseReference;

    private static final String TAG = "ChattingList";
    @Nullable

    private ImageView appName;
    private ImageView appIcon;
    private RecyclerView chatList;
    private FirebaseAuth mAuth;
    private String current_user_id;
    private String current_user_image;
    private DatabaseReference UsersRef;
    private DatabaseReference HistoryRef;

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        getUserProfileImg();

        view = inflater.inflate(R.layout.activity_chatting_list, container, false);

        HistoryRef = FirebaseDatabase.getInstance().getReference().child("Chatlist").child(current_user_id);

        chatList = (RecyclerView) view.findViewById(R.id.chat_list);
        chatList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        chatList.setLayoutManager(linearLayoutManager);

        DisplayAllUsersChats();

        return view;
    }

    private void DisplayAllUsersChats(){
        FirebaseRecyclerOptions<History> options =
                new FirebaseRecyclerOptions.Builder<History>()
                        .setQuery(HistoryRef, History.class)
                        .build();
//            PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        FirebaseRecyclerAdapter<History, ChattingHistory.HistoryViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<History,ChattingHistory.HistoryViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ChattingHistory.HistoryViewHolder holder, int position, @NonNull History model) {
                       UserProfile peer = new UserProfile(model.getId());

                       peer.getProfile(new UserProfileCallback() {
                           @Override
                           public void onComplete(HashMap<String, String> profile) {
                               if (profile != null){
                                   holder.fullName.setText(profile.get("name"));
                                   Picasso.get().load(profile.get("url")).into(holder.profileImage);
                               }
                           }
                       });
                       holder.message.setText(model.getMessage());

                       holder.profileImage.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               String uid = model.getId();
                               if(!TextUtils.equals(uid, current_user_id)){
                                   Intent intent = new Intent(getActivity(), Social_Profile.class);
                                   intent.putExtra("uid", uid);
                                   startActivity(intent);
                               }else{
                                   Toast.makeText(getActivity(), "Click on other's portrait to view profile",Toast.LENGTH_SHORT).show();
                               }
                           }
                       });

                       holder.contactContent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String uid = model.getId();
                                if(!TextUtils.equals(uid, current_user_id)){
                                    Intent intent = new Intent(getActivity(), Chatting.class);
                                    intent.putExtra("userid", uid);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(getActivity(), "Message failed",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ChattingHistory.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_contact_layout, parent, false);
                        ChattingHistory.HistoryViewHolder viewHolder = new ChattingHistory.HistoryViewHolder(view);

                        return viewHolder;
                    }
                };
        chatList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    private void getUserProfileImg() {
        UsersRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
//                    String userFullName = dataSnapshot.child("username").getValue().toString();
                    String userProfileImg = dataSnapshot.child("imageURL").getValue().toString();
                    current_user_image = userProfileImg;
//                    current_user_name =  userFullName;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder{
        TextView fullName, message;
        CircleImageView profileImage;
        LinearLayout contactContent;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.contact_user_name);
            message= itemView.findViewById(R.id.message_content);
            profileImage = itemView.findViewById(R.id.contact_profile_image);
            contactContent = itemView.findViewById(R.id.contact_content);
        }
    }
}
