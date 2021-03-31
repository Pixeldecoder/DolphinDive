package com.capstone.dolphindive;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.capstone.dolphindive.model.diveshopdata;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class DiveShop extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dive_shop, container, false);

        ImageButton cancunbtnOpen = (ImageButton) view.findViewById(R.id.Cancun_button);
        cancunbtnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sessionId = "cancun";
                Intent in = new Intent(getActivity(),diveshoplist.class);
                in.putExtra("Key", sessionId);
                startActivity(in);
            }
        });

        ImageButton redseabtnOpen = (ImageButton) view.findViewById(R.id.redsea_button);
        redseabtnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sessionId = "Egypt";
                Intent in = new Intent(getActivity(),diveshoplist.class);
                in.putExtra("Key", sessionId);
                startActivity(in);
            }
        });

        ImageButton sempornabtnOpen = (ImageButton) view.findViewById(R.id.semporna_button);
        sempornabtnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sessionId = "semporna";
                Intent in = new Intent(getActivity(),diveshoplist.class);
                in.putExtra("Key", sessionId);
                startActivity(in);
            }
        });

        ImageButton rajaampatbtnOpen = (ImageButton) view.findViewById(R.id.rajaampat_button);
        rajaampatbtnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sessionId = "raja ampat";
                Intent in = new Intent(getActivity(),diveshoplist.class);
                in.putExtra("Key", sessionId);
                startActivity(in);
            }
        });

        ImageButton balibtnOpen = (ImageButton) view.findViewById(R.id.bali_button);
        balibtnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sessionId = "bali";
                Intent in = new Intent(getActivity(),diveshoplist.class);
                in.putExtra("Key", sessionId);
                startActivity(in);
            }
        });

        ImageButton lembornaislandbtnOpen = (ImageButton) view.findViewById(R.id.lembornaisland_button);
        lembornaislandbtnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sessionId = "lemborna island";
                Intent in = new Intent(getActivity(),diveshoplist.class);
                in.putExtra("Key", sessionId);
                startActivity(in);
            }
        });

        ImageButton diveequipmentbtnOpen = (ImageButton) view.findViewById(R.id.diveequip_button);
        diveequipmentbtnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(),equipment_rental.class);
                startActivity(in);
            }
        });

        ImageButton localfoodbtnOpen = (ImageButton) view.findViewById(R.id.localfood_button);
        localfoodbtnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(),Local_Food.class);
                startActivity(in);
            }
        });

        ImageButton localtipsbtnOpen = (ImageButton) view.findViewById(R.id.localtip_button);
        localtipsbtnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(),Local_tips.class);
                startActivity(in);
            }
        });

        SearchView searchView = (SearchView) view.findViewById(R.id.main_seach_dest);
        CharSequence instruction = searchView.getQuery();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent in = new Intent(getActivity(),diveshoplist.class);
                in.putExtra("Key", instruction.toString().toLowerCase());
                startActivity(in);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        return view;
    }



}