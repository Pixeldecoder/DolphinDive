package com.capstone.dolphindive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DiveLogList extends AppCompatActivity {

    ImageButton add_log_btn;
    SwipeMenuListView list;

    FirebaseFirestore db;
    CollectionReference collectionReference;
    String uid;
    FirebaseUser user;
    SwipeMenuCreator creator;

    int count;
    ArrayList<String> logList;
    ArrayList<String> genreList;
    ArrayList<String> nameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dive_log_list);

        db=  FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        collectionReference = db.collection("Users").document(uid).collection("divelog");

        list = findViewById(R.id.divelog_list);
        add_log_btn = findViewById(R.id.add_new_log);

        add_log_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DiveLogList.this, DiveLog_Create.class);
                Bundle bundle =new Bundle();
                bundle.putString("numlog","Log"+ String.valueOf(count+1));
                i.putExtras(bundle);
                startActivity(i);
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    int tmp = 0;
                    genreList = new ArrayList<>();
                    nameList = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        tmp++;
                        genreList.add(document.getString("genre"));
                        nameList.add(document.getString("title"));
                    }
                    count=tmp;
                    logList = new ArrayList<>();
                    for(int i=1; i<=count;i++){
                        logList.add("Log"+i+" -- "+nameList.get(i-1)+" -- "+genreList.get(i-1)+" Diving");
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, logList);
                    list.setAdapter(arrayAdapter);

                    creator = new SwipeMenuCreator() {

                        @Override
                        public void create(SwipeMenu menu) {

                            // create "delete" item
                            SwipeMenuItem deleteItem = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                                    0x3F, 0x25)));
                            // set item width
                            deleteItem.setWidth(170);
                            // set a icon
                            deleteItem.setIcon(R.drawable.ic_baseline_delete_forever_24);
                            // add to menu
                            menu.addMenuItem(deleteItem);
                        }
                    };
                    // set creator
                    list.setMenuCreator(creator);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String clickedItem=(String) list.getItemAtPosition(position);
                            String[] splitItem = clickedItem.split(" ");
                            String genre = splitItem[splitItem.length-2].trim();
                            String logNum = splitItem[0].trim();
                            Toast.makeText(DiveLogList.this,clickedItem,Toast.LENGTH_LONG).show();
                            if(TextUtils.equals(genre,"Free")){
                                Intent i = new Intent(DiveLogList.this, DiveLog_Edit_Free.class);
                                i.putExtra("numlog",logNum);
                                startActivity(i);
                            }else if(TextUtils.equals(genre,"Scuba")){
                                Intent i = new Intent(DiveLogList.this, DiveLog_Edit_Scu.class);
                                i.putExtra("numlog",logNum);
                                startActivity(i);
                            }else{
                                Toast.makeText(DiveLogList.this, "The genre is incorrect",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                            switch (index) {
                                case 0:
                                    int num = position+1;
                                    showDialog(nameList.get(position), "Log"+num);
                                    break;
                            }
                            // false : close the menu; true : not close the menu
                            return false;
                        }
                    });

                }else {
                    Log.d("Error getting document:", task.getException().toString());
                }
            }
        });
    }

    private void showDialog(String name, String lognum){
        AlertDialog.Builder builder = new AlertDialog.Builder(DiveLogList.this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure to delete "+name);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                collectionReference.document(lognum).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DiveLogList.this, "Log deleted",Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}