package com.capstone.dolphindive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
    ListView list;

    FirebaseFirestore db;
    CollectionReference collectionReference;
    String uid;
    FirebaseUser user;

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

                }else {
                    Log.d("Error getting document:", task.getException().toString());
                }
            }
        });
    }
}