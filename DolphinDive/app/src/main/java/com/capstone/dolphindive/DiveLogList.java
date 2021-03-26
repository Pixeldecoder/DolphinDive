package com.capstone.dolphindive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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
    ArrayList<String> arrayList;

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
                Intent i = new Intent(DiveLogList.this, DiveLog.class);
                Bundle bundle =new Bundle();
                bundle.putString("numlog","log"+ String.valueOf(count+1));
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
                    for (DocumentSnapshot document : task.getResult()) {
                        tmp++;
                    }
                    count=tmp;
                    arrayList = new ArrayList<>();
                    for(int i=1; i<=count;i++){
                        arrayList.add("log"+i);
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
                    list.setAdapter(arrayAdapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String clickedItem=(String) list.getItemAtPosition(position);
                            Toast.makeText(DiveLogList.this,clickedItem,Toast.LENGTH_LONG).show();
                            Intent i = new Intent(DiveLogList.this, DiveLog.class);
                            Bundle bundle =new Bundle();
                            bundle.putString("numlog",clickedItem);
                            i.putExtras(bundle);
                            startActivity(i);
                        }
                    });

                }else {
                    Log.d("Error getting document:", task.getException().toString());
                }
            }
        });
    }
}