package com.capstone.dolphindive;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

public class DiveLog_Edit_Free extends AppCompatActivity{

    Button save;
    Button cancel;
    TextView think, start_time, end_time, dive_date,dive_loc,num_dive, max_dep, avg_dep, min_tmp, avg_tmp,title;

    FirebaseFirestore db;
    DocumentReference documentReference;
    String uid;
    String logNum;
    FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_divelog_edit_free);
        logNum = getIntent().getStringExtra("numlog");
        db=  FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        documentReference = db.collection("Users").document(uid).collection("divelog").document(logNum);

        title = findViewById(R.id.title);
        cancel = findViewById(R.id.cancel_free);
        save = findViewById(R.id.save_free);
        think = findViewById(R.id.think_content_free);
        start_time = findViewById(R.id.start_time_free);
        end_time = findViewById(R.id.end_time_free);
        dive_date = findViewById(R.id.div_date_free);
        dive_loc = findViewById(R.id.dive_location_free);
        num_dive = findViewById(R.id.num_dive_free);
        max_dep = findViewById(R.id.max_dep_input_free);
        avg_dep = findViewById(R.id.avg_dep_input_free);
        min_tmp = findViewById(R.id.min_temp_input_free);
        avg_tmp = findViewById(R.id.avg_temp_input_free);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()){
                            EditData();
                        }else{
                            Toast.makeText(DiveLog_Edit_Free.this, "Log doesn't exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void EditData() {
        String title_txt = title.getText().toString();
        String think_txt = think.getText().toString();
        String start_time_txt = start_time.getText().toString();
        String end_time_txt = end_time.getText().toString();
        String dive_date_txt = dive_date.getText().toString();
        String dive_loc_txt = dive_loc.getText().toString();
        String num_dive_txt = num_dive.getText().toString();
        String max_dep_txt = max_dep.getText().toString();
        String avg_dep_txt = avg_dep.getText().toString();
        String min_tmp_txt = min_tmp.getText().toString();
        String avg_tmp_txt = avg_tmp.getText().toString();

        if(!TextUtils.isEmpty(title_txt) ){

            final DocumentReference sfDocRef = db.collection("Users").document(uid).collection("divelog").document(logNum);

            db.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    DocumentSnapshot snapshot = transaction.get(sfDocRef);

                    //transaction.update(sfDocRef, "population", newPopulation);
                    transaction.update(sfDocRef, "think", think_txt);
                    transaction.update(sfDocRef, "title", title_txt);
                    transaction.update(sfDocRef, "start_time", start_time_txt);
                    transaction.update(sfDocRef, "end_time", end_time_txt);
                    transaction.update(sfDocRef, "dive_date", dive_date_txt);
                    transaction.update(sfDocRef, "dive_loc", dive_loc_txt);
                    transaction.update(sfDocRef, "num_dive", num_dive_txt);
                    transaction.update(sfDocRef, "max_dep", max_dep_txt);
                    transaction.update(sfDocRef, "avg_dep", avg_dep_txt);
                    transaction.update(sfDocRef, "min_tmp", min_tmp_txt);
                    transaction.update(sfDocRef, "avg_tmp", avg_tmp_txt);

                    //transaction.update(sfDocRef, "url", downloadUri.toString());

                    // Success
                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(DiveLog_Edit_Free.this, "Divelog Updated",Toast.LENGTH_SHORT).show();
                    finish();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DiveLog_Edit_Free.this, "failed",Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(DiveLog_Edit_Free.this, "Title is Required",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    String title_txt = task.getResult().getString("title");
                    String think_txt = task.getResult().getString("think");
                    String start_time_txt = task.getResult().getString("start_time");
                    String end_time_txt = task.getResult().getString("end_time");
                    String dive_date_txt = task.getResult().getString("dive_date");
                    String dive_loc_txt = task.getResult().getString("dive_loc");
                    String num_dive_txt = task.getResult().getString("num_dive");
                    String max_dep_txt = task.getResult().getString("max_dep");
                    String avg_dep_txt = task.getResult().getString("avg_dep");
                    String min_tmp_txt = task.getResult().getString("min_tmp");
                    String avg_tmp_txt = task.getResult().getString("avg_tmp");

                    title.setText(title_txt);
                    think.setText(think_txt);
                    start_time.setText(start_time_txt);
                    end_time.setText(end_time_txt);
                    dive_date.setText(dive_date_txt);
                    dive_loc.setText(dive_loc_txt);
                    num_dive.setText(num_dive_txt);
                    max_dep.setText(max_dep_txt);
                    avg_dep.setText(avg_dep_txt);
                    min_tmp.setText(min_tmp_txt);
                    avg_tmp.setText(avg_tmp_txt);

                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
