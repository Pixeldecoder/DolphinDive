package com.capstone.dolphindive.utility;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.capstone.dolphindive.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static  final int MSG_TYPE_LEFT = 0;
    public static  final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Message> mChat;
    private String imageurl;

    private String TAG = "Chatting";

    FirebaseUser fuser;

    public MessageAdapter(Context mContext, List<Message> mChat, String imageurl){
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        Message chat = mChat.get(position);

        if (imageurl.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(imageurl).into(holder.profile_image);
        }

        if(chat.getMessage() != null) {
            holder.show_message.setText(chat.getMessage());
            holder.show_message.setVisibility(TextView.VISIBLE);
            holder.messageImageView.setVisibility(ImageView.GONE);
            holder.messageAudioView.setVisibility(ImageView.GONE);
        } else if(chat.getFileUrl() != null) {
            String type = chat.getType();
            String fileUrl = chat.getFileUrl();
            if (type.equals("image")) {
                if(fileUrl.startsWith("gs://")) {
                    holder.show_message.setVisibility(TextView.GONE);
                    holder.messageImageView.setVisibility(ImageView.VISIBLE);
                    holder.messageAudioView.setVisibility(ImageView.GONE);
                    StorageReference storageReference = FirebaseStorage.getInstance()
                            .getReferenceFromUrl(fileUrl);
                    storageReference.getDownloadUrl().addOnCompleteListener(
                            new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        String downloadUrl = task.getResult().toString();
                                        Glide.with(holder.messageImageView.getContext())
                                                .load(downloadUrl)
                                                .into(holder.messageImageView);
                                    } else {
                                        Log.w(TAG, "Getting download url was not successful.",
                                                task.getException());
                                    }
                                }
                            });
                }else{
                    Glide.with(holder.messageImageView.getContext())
                            .load(chat.getFileUrl())
                            .into(holder.messageImageView);
                }
            }else if(type.equals("audio")){
                holder.show_message.setVisibility(TextView.GONE);
                holder.messageImageView.setVisibility(ImageView.GONE);
                holder.messageAudioView.setVisibility(ImageView.VISIBLE);

                if (fileUrl.equals("audio_default")){
                   holder.messageAudioView.setImageResource(R.drawable.audio_uploading);
                }else {
                    StorageReference storageReference = FirebaseStorage.getInstance()
                            .getReferenceFromUrl(fileUrl);
                    storageReference.getDownloadUrl().addOnCompleteListener(
                            new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        String downloadUrl = task.getResult().toString();
                                        holder.messageAudioView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                final MediaPlayer mediaplayer = new MediaPlayer();
                                                mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                                try {
                                                    mediaplayer.setDataSource(downloadUrl);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                mediaplayer.prepareAsync();
                                                mediaplayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                                    @Override
                                                    public void onPrepared(MediaPlayer mp) {
                                                        mp.start();
                                                    }
                                                });
                                            }
                                        });
                                        holder.messageAudioView.setImageResource(R.drawable.audio_message);
                                    } else {
                                        Log.w(TAG, "Getting download url was not successful.",
                                                task.getException());
                                    }
                                }
                            });
                }
            }
//            else{
//                    Glide.with(holder.messageImageView.getContext())
//                            .load(chat.getFileUrl())
//                            .into(holder.messageImageView);
//                }
//                holder.messageImageView.setVisibility(ImageView.VISIBLE);
//                holder.show_message.setVisibility(TextView.GONE);
        }


        if (position == mChat.size()-1){
            if (chat.isIsseen()){
                holder.txt_seen.setText("Seen");
            } else {
                holder.txt_seen.setText("Delivered");
            }
        } else {
            holder.txt_seen.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image;
        public TextView txt_seen;
        public ImageView messageImageView;
        public ImageView messageAudioView;

        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);
            messageImageView = itemView.findViewById(R.id.messageImageView);
            messageAudioView = itemView.findViewById(R.id.messageAudioView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}