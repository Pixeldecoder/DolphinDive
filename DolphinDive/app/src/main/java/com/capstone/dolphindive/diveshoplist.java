package com.capstone.dolphindive;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.capstone.dolphindive.adapter.placesAdapter;
import com.capstone.dolphindive.model.diveshopdata;
import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class diveshoplist extends AppCompatActivity {
    private ArrayList<diveshopdata> recentsDataList;
    private placesAdapter recycleviewadapter;
    private placesAdapter mRecyclerViewAdapter;
    private RadioGroup radioFilterGroup;
    private RadioButton radioFilterButton;
    private TextView btndisplay,checkin_date,checkout_date;
    private RecyclerView recyclerView;
    private AlertDialog dialog, dialogcalendar;
    private AlertDialog.Builder dialogBuilder,dialogBuildercalendar;
    private TextView Groupsize, num_rooms, num_adults, num_child;
    private ImageButton filtercheck, filtercancel, groupsize_minus, groupsize_plus, room_minus, room_plus, adults_minus, adult_plus, child_minus, child_plus, Calendar_confirm,Calendar_goback;
    private int group_size = 0, num_room = 0, num_adult = 0, num_children = 0;
    private String check_in,check_out,teststring;
    RecyclerView recentRecycler;
    placesAdapter recentsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diveshoplist);
        Bundle extras = getIntent().getExtras();
        String instruction = extras.getString("Key");
        initSearchWidget();
        ExampleData();
        buildRecentRecycler(recentsDataList);
        filterwithinstruction(instruction);



        ImageButton filterbutton  = (ImageButton) findViewById(R.id.con_filter);
        filterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                filterpopup();
            }
        });

        ImageButton calanderbutton  = (ImageButton) findViewById(R.id.date_selector);
        calanderbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                calendar();
            }
        });

    }
    private void sorting(ArrayList inputlist){
        RadioGroup radiotext = (RadioGroup) findViewById(R.id.FilterGroup);
        radiotext.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.RateSelector){
                    sortArrayListRate(inputlist);
                }
                if(checkedId == R.id.PriceSelector){
                    sortArrayListPrice(inputlist);
                }
                if(checkedId == R.id.PopularSelector){
                    sortArrayListPopular(inputlist);
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
        recentsDataList.add(new diveshopdata("Lembongan Diveshop","Indonesia","$260","4.9","Superhot",R.drawable.cancun));
    }
    private void sortArrayListRate(ArrayList inputlist){
        Collections.sort(inputlist, new Comparator<diveshopdata>() {
            @Override
            public int compare(diveshopdata o1, diveshopdata o2) {
                return o2.getRate().compareTo(o1.getRate());
            }
        });
        mRecyclerViewAdapter.notifyDataSetChanged();
    }
    private void sortArrayListPrice(ArrayList inputlist){
        Collections.sort(inputlist, new Comparator<diveshopdata>() {
            @Override
            public int compare(diveshopdata o1, diveshopdata o2) {
                return o1.getPrice().compareTo(o2.getPrice());
            }
        });
        mRecyclerViewAdapter.notifyDataSetChanged();
    }
    private void sortArrayListPopular(ArrayList inputlist){
        Collections.sort(inputlist, new Comparator<diveshopdata>() {
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
                sorting(filteredlist);
                return false;
            }
        });
    }
    private boolean filterwithinstruction(String instruction){
        ArrayList<diveshopdata> filteredlist = new ArrayList<diveshopdata>();
        for (diveshopdata item : recentsDataList) {
            if (instruction != null && item.getCountryName().toLowerCase().contains(instruction.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        buildRecentRecycler(filteredlist);
        sorting(filteredlist);
        return false;
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
    private void calendar() {
        dialogBuildercalendar = new AlertDialog.Builder(this);
        final View calendarpopupview = getLayoutInflater().inflate(R.layout.activity_diveshop_calendar, null);
        checkin_date = (TextView) findViewById(R.id.checkindate);
        checkout_date = (TextView) findViewById(R.id.checkoutdate);
        TextView test = (TextView) findViewById(R.id.test);
        Calendar_confirm = (ImageButton) calendarpopupview.findViewById(R.id.calendar_confirm);
        Calendar_goback = (ImageButton) calendarpopupview.findViewById(R.id.calendar_goback);
        Date today = new Date();
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        CalendarPickerView datePicker = calendarpopupview.findViewById(R.id.calendar);
        datePicker.init(today, nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.RANGE)
                .withSelectedDate(today);

        datePicker.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                //String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(date);
                Calendar calSelected = Calendar.getInstance();
                calSelected.setTime(date);
                String selectedDate = calSelected.get(Calendar.DAY_OF_MONTH)
                        + "-" + (calSelected.get(Calendar.MONTH) + 1)
                        + "-" + calSelected.get(Calendar.YEAR);
                String selecteddate = datePicker.getSelectedDates().toString();
                //Toast.makeText(diveshop_calendar.this, selectedDate, Toast.LENGTH_SHORT).show();
                ArrayList<String> myList = new ArrayList<String>(Arrays.asList(selecteddate.split(" ")));
                //ArrayList<String> myListcheckout = new ArrayList<String>(Arrays.asList(myList1.split(" ")));
                String checkindate = myList.get(1).toString()+"-"+myList.get(2).toString()+"-"+myList.get(5).toString();
                String checkoutdate = myList.get(myList.size()-5).toString()+"-"+myList.get(myList.size()-4).toString()+"-"+myList.get(myList.size()-1).toString();
                checkindate = checkindate.replace(",", "");
                checkoutdate = checkoutdate.replace("]", "");
                check_out = checkoutdate;
                check_in = checkindate;
                teststring = selecteddate;

            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });
        dialogBuildercalendar.setView(calendarpopupview);
        dialogcalendar = dialogBuildercalendar.create();
        dialogcalendar.show();
        Calendar_confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkout_date.setText(check_out);
                checkin_date.setText(check_in);
                test.setText(teststring);
                dialogcalendar.dismiss();
            }
        });

        Calendar_goback.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialogcalendar.dismiss();
            }
        });


    }
    private void filterpopup(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View filterpopupview = getLayoutInflater().inflate(R.layout.diveshop_filter,null);
        Groupsize = (TextView) filterpopupview.findViewById(R.id.groupsize_num);
        num_rooms = (TextView) filterpopupview.findViewById(R.id.rooms_num);
        num_adults = (TextView) filterpopupview.findViewById(R.id.adult_num);
        num_child = (TextView) filterpopupview.findViewById(R.id.child_num);
        filtercheck = (ImageButton) filterpopupview.findViewById(R.id.filter_check);
        filtercancel = (ImageButton) filterpopupview.findViewById(R.id.filter_can);
        groupsize_minus = (ImageButton ) filterpopupview.findViewById(R.id.groupsize_minus);
        groupsize_plus = (ImageButton ) filterpopupview.findViewById(R.id.groupsize_add);
        room_minus = (ImageButton ) filterpopupview.findViewById(R.id.rooms_minus);
        room_plus = (ImageButton ) filterpopupview.findViewById(R.id.rooms_add);
        adults_minus = (ImageButton ) filterpopupview.findViewById(R.id.adult_minus);
        adult_plus = (ImageButton ) filterpopupview.findViewById(R.id.adult_add);
        child_minus = (ImageButton ) filterpopupview.findViewById(R.id.child_minus);
        child_plus = (ImageButton ) filterpopupview.findViewById(R.id.child_add);
        num_rooms.setText(String.valueOf(num_room));
        num_adults.setText(String.valueOf(num_adult));
        Groupsize.setText(String.valueOf(group_size));
        num_child.setText(String.valueOf(num_children));
        dialogBuilder.setView(filterpopupview);
        dialog = dialogBuilder.create();
        dialog.show();

        groupsize_minus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(group_size>=1){
                    group_size = group_size -1;
                }
                Groupsize.setText(String.valueOf(group_size));
            }
        });

        groupsize_plus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                group_size = group_size +1;
                Groupsize.setText(String.valueOf(group_size));
            }
        });

        room_plus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                num_room = num_room +1;
                num_rooms.setText(String.valueOf(num_room));
            }
        });

        room_minus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(num_room>=1){
                    num_room = num_room -1;
                    num_rooms.setText(String.valueOf(num_room));
                }

            }
        });

        adults_minus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(num_adult>=1){
                    num_adult = num_adult -1;
                    num_adults.setText(String.valueOf(num_adult));
                }

            }
        });

        adult_plus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                num_adult = num_adult +1;
                num_adults.setText(String.valueOf(num_adult));
            }
        });

        child_plus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                num_children = num_children +1;
                num_child.setText(String.valueOf(num_children));
            }
        });

        child_minus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(num_children>=1){
                    num_children = num_children -1;
                    num_child.setText(String.valueOf(num_children));
                }
            }
        });

        filtercheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                group_size = Integer.parseInt(Groupsize.getText().toString());
                num_room = Integer.parseInt(num_rooms.getText().toString());
                num_adult = Integer.parseInt(num_adults.getText().toString());
                num_children = Integer.parseInt(num_child.getText().toString());
                dialog.dismiss();
            }
        });

        filtercancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                group_size = 0;
                num_room = 0;
                num_adult = 0;
                num_children = 0;
                num_rooms.setText(String.valueOf(num_room));
                num_adults.setText(String.valueOf(num_adult));
                Groupsize.setText(String.valueOf(group_size));
                num_child.setText(String.valueOf(num_children));
            }
        });




    }

}