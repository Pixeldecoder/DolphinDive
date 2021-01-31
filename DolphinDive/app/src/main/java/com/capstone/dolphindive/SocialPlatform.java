package com.capstone.dolphindive;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SocialPlatform extends Fragment {
    @Nullable
    Button chatBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        chatBtn = (Button) inflater.inflate(R.layout.activity_social_platform, container, false).findViewById(R.id.chatBtn);

        return inflater.inflate(R.layout.activity_social_platform, container, false);
    }
}