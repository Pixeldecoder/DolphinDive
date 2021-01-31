package com.capstone.dolphindive;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class PreLogin extends Fragment implements View.OnClickListener{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pre_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button loginBtn = (Button)view.findViewById(R.id.preLoginbtn);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(getActivity(), Login.class);
        startActivity(i);
    }
}