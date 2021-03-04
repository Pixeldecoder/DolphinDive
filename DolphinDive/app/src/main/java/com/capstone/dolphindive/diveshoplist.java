package com.capstone.dolphindive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.capstone.dolphindive.adapter.placesAdapter;
import com.capstone.dolphindive.model.diveshopdata;

import java.util.ArrayList;
import java.util.List;

public class diveshoplist extends AppCompatActivity {
    RecyclerView recentRecycler;
    placesAdapter recentsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diveshoplist);

        List<diveshopdata> recentsDataList = new ArrayList<>();
        recentsDataList.add(new diveshopdata("AM Lake","Cancun","From $200",R.drawable.bali));
        recentsDataList.add(new diveshopdata("Nilgiri Hills","Cancun","From $300",R.drawable.bali));
        recentsDataList.add(new diveshopdata("AM Lake","Cancun","From $200",R.drawable.bali));
        recentsDataList.add(new diveshopdata("Nilgiri Hills","Cancun","From $300",R.drawable.bali));
        recentsDataList.add(new diveshopdata("AM Lake","Cancun","From $200",R.drawable.bali));
        recentsDataList.add(new diveshopdata("Nilgiri Hills","Cancun","From $300",R.drawable.bali));

        setRecentRecycler(recentsDataList);

    }

    private void setRecentRecycler(List<diveshopdata> recentsDataList) {
        recentRecycler = findViewById(R.id.recent_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recentRecycler.setLayoutManager(layoutManager);
        recentsAdapter = new placesAdapter(this, recentsDataList);
        recentRecycler.setAdapter(recentsAdapter);
    }
}