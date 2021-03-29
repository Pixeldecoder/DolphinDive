package com.capstone.dolphindive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.capstone.dolphindive.adapter.placesAdapter;
import com.capstone.dolphindive.model.diveshopdata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class diveshoplist extends AppCompatActivity {
    private ArrayList<diveshopdata> recentsDataList;
    private placesAdapter recycleviewadapter;
    private placesAdapter mRecyclerViewAdapter;
    private RadioGroup radioFilterGroup;
    private RadioButton radioFilterButton;
    private TextView btndisplay;
    private RecyclerView recyclerView;
    RecyclerView recentRecycler;
    placesAdapter recentsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diveshoplist);
        initSearchWidget();
        ExampleData();
        buildRecentRecycler(recentsDataList);
        //setRecentRecycler(recentsDataList);


        RadioGroup radiotext = (RadioGroup) findViewById(R.id.FilterGroup);
        radiotext.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.RateSelector){
                    sortArrayListRate();
                }
                if(checkedId == R.id.PriceSelector){
                    sortArrayListPrice();
                }
                if(checkedId == R.id.PopularSelector){
                    sortArrayListPopular();
                }
            }
        });

    }



    private void ExampleData(){
        recentsDataList = new ArrayList<>();
        recentsDataList.add(new diveshopdata("Bali Diveshop","Bali","$175","4.5","Superhot",R.drawable.bali));
        recentsDataList.add(new diveshopdata("Cancun Diveshop","Cancun","$603","3.8","Medium",R.drawable.cancun));
        recentsDataList.add(new diveshopdata("Raja Ampat Diveshop","Indonesia","$200","2.7","Hot",R.drawable.rajaampat));
        recentsDataList.add(new diveshopdata("Semporna Diveshop","Indonesia","$324","3.6","Hot",R.drawable.semporna));
        recentsDataList.add(new diveshopdata("Red Sea Diveshop","Egypt","$200","5","Superhot",R.drawable.redsea));
        recentsDataList.add(new diveshopdata("Honolulu Diveshop","Honolulu","$260","4.9","Superhot",R.drawable.cancun));
    }


    private void sortArrayListRate(){
        Collections.sort(recentsDataList, new Comparator<diveshopdata>() {
            @Override
            public int compare(diveshopdata o1, diveshopdata o2) {
                return o2.getRate().compareTo(o1.getRate());
            }
        });
        mRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void sortArrayListPrice(){
        Collections.sort(recentsDataList, new Comparator<diveshopdata>() {
            @Override
            public int compare(diveshopdata o1, diveshopdata o2) {
                return o1.getPrice().compareTo(o2.getPrice());
            }
        });
        mRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void sortArrayListPopular(){
        Collections.sort(recentsDataList, new Comparator<diveshopdata>() {
            @Override
            public int compare(diveshopdata o1, diveshopdata o2) {
                return o2.getPopular().compareTo(o1.getPopular());
            }
        });
        mRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void initSearchWidget(){
        SearchView searchView = (SearchView) findViewById(R.id.dest_seach);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<diveshopdata> filteredlist = new ArrayList<diveshopdata>();
                for(diveshopdata item : recentsDataList)
                {
                    if (item.getCountryName().toLowerCase().contains(s.toLowerCase()))
                    {
                    filteredlist.add(item);
                    }
                }
                buildRecentRecycler(filteredlist);

                return false;
            }
        });
    }


    private void setRecentRecycler(ArrayList<diveshopdata> recentsDataList) {
        recentRecycler = findViewById(R.id.recent_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recentRecycler.setLayoutManager(layoutManager);
        recentsAdapter = new placesAdapter(this, 0, recentsDataList);
        recentRecycler.setAdapter(recentsAdapter);
    }

    private void buildRecentRecycler(ArrayList liststate){
        RecyclerView recyclerView = findViewById(R.id.recent_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        mRecyclerViewAdapter = new placesAdapter(this, 0, liststate);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mRecyclerViewAdapter);
    }


}