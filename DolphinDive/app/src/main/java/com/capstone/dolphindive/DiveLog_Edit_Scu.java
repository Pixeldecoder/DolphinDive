package com.capstone.dolphindive;

import android.os.Bundle;
import android.text.TextUtils;
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

public class DiveLog_Edit_Scu extends AppCompatActivity {
    Button cancel;
    Button save;
    TextView think, water, scenery, weather, start_time, end_time, dive_date, dive_loc, num_dive, max_dep, avg_dep, weight, min_temp,
            avg_tmp, BCD_brand, mask_brand, watch_brand, wetsuit_brand, fin_brand, cam_brand, title;

    FirebaseFirestore db;
    DocumentReference documentReference;
    String uid;
    String logId;
    FirebaseUser user;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_divelog_edit_scu);

        logId = getIntent().getStringExtra("logId");

        db=  FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        documentReference = db.collection("Users").document(uid).collection("divelog").document(logId);

        cancel = findViewById(R.id.cancel_scuba);
        save = findViewById(R.id.save_scuba);
        think = findViewById(R.id.think_content);
        water = findViewById(R.id.water_content);
        scenery = findViewById(R.id.scenery_content);
        weather = findViewById(R.id.weather_content);
        start_time = findViewById(R.id.start_time);
        end_time = findViewById(R.id.end_time);
        dive_date = findViewById(R.id.div_date);
        dive_loc = findViewById(R.id.dive_location);
        num_dive = findViewById(R.id.num_dive);
        max_dep = findViewById(R.id.max_dep_input);
        avg_dep = findViewById(R.id.avg_dep_input);
        weight = findViewById(R.id.weight_input);
        min_temp = findViewById(R.id.min_temp_input);
        avg_tmp = findViewById(R.id.avg_temp_input);
        BCD_brand = findViewById(R.id.BCD_Brand);
        mask_brand = findViewById(R.id.mask_Brand);
        watch_brand = findViewById(R.id.watch_brand);
        wetsuit_brand = findViewById(R.id.wetsuit_brand);
        fin_brand = findViewById(R.id.fin_brand);
        cam_brand = findViewById(R.id.cam_brand);
        title = findViewById(R.id.title);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()){
                            EditData();
                        }else{
                            Toast.makeText(DiveLog_Edit_Scu.this, "Log doesn't exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void EditData() {
        String title_txt = title.getText().toString();
        String think_txt = think.getText().toString();
        String water_txt = water.getText().toString();
        String scenery_txt = scenery.getText().toString();
        String weather_txt = weather.getText().toString();
        String start_time_txt = start_time.getText().toString();
        String end_time_txt = end_time.getText().toString();
        String dive_date_txt = dive_date.getText().toString();
        String dive_loc_txt = dive_loc.getText().toString();
        String num_dive_txt = num_dive.getText().toString();
        String max_dep_txt = max_dep.getText().toString();
        String avg_dep_txt = avg_dep.getText().toString();
        String weight_txt = weight.getText().toString();
        String min_tmp_txt = min_temp.getText().toString();
        String avg_tmp_txt = avg_tmp.getText().toString();
        String BCD_brand_txt = BCD_brand.getText().toString();
        String mask_brand_txt = mask_brand.getText().toString();
        String watch_brand_txt = watch_brand.getText().toString();
        String wetsuit_brand_txt = wetsuit_brand.getText().toString();
        String fin_brand_txt = fin_brand.getText().toString();
        String cam_brand_txt = cam_brand.getText().toString();

        if(!TextUtils.isEmpty(title_txt) ){

            final DocumentReference sfDocRef = db.collection("Users").document(uid).collection("divelog").document(logId);

            db.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    DocumentSnapshot snapshot = transaction.get(sfDocRef);

                    //transaction.update(sfDocRef, "population", newPopulation);
                    transaction.update(sfDocRef, "title", title_txt);
                    transaction.update(sfDocRef, "think", think_txt);
                    transaction.update(sfDocRef, "water", water_txt);
                    transaction.update(sfDocRef, "scenery", scenery_txt);
                    transaction.update(sfDocRef, "weather", weather_txt);
                    transaction.update(sfDocRef, "start_time", start_time_txt);
                    transaction.update(sfDocRef, "end_time", end_time_txt);
                    transaction.update(sfDocRef, "dive_date", dive_date_txt);
                    transaction.update(sfDocRef, "dive_loc", dive_loc_txt);
                    transaction.update(sfDocRef, "num_dive", num_dive_txt);
                    transaction.update(sfDocRef, "max_dep", max_dep_txt);
                    transaction.update(sfDocRef, "avg_dep", avg_dep_txt);
                    transaction.update(sfDocRef, "weight", weight_txt);
                    transaction.update(sfDocRef, "min_tmp", min_tmp_txt);
                    transaction.update(sfDocRef, "avg_tmp", avg_tmp_txt);
                    transaction.update(sfDocRef, "BCD_brand", BCD_brand_txt);
                    transaction.update(sfDocRef, "mask_brand", mask_brand_txt);
                    transaction.update(sfDocRef, "watch_brand", watch_brand_txt);
                    transaction.update(sfDocRef, "wetsuit_brand", wetsuit_brand_txt);
                    transaction.update(sfDocRef, "fin_brand", fin_brand_txt);
                    transaction.update(sfDocRef, "cam_brand", cam_brand_txt);

                    //transaction.update(sfDocRef, "url", downloadUri.toString());

                    // Success
                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(DiveLog_Edit_Scu.this, "Divelog Updated",Toast.LENGTH_SHORT).show();
                    finish();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DiveLog_Edit_Scu.this, "failed",Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(DiveLog_Edit_Scu.this, "Title is Required",Toast.LENGTH_SHORT).show();
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
                    String water_txt = task.getResult().getString("water");
                    String scenery_txt = task.getResult().getString("scenery");
                    String weather_txt = task.getResult().getString("weather");
                    String start_time_txt = task.getResult().getString("start_time");
                    String end_time_txt = task.getResult().getString("end_time");
                    String dive_date_txt = task.getResult().getString("dive_date");
                    String dive_loc_txt = task.getResult().getString("dive_loc");
                    String num_dive_txt = task.getResult().getString("num_dive");
                    String max_dep_txt = task.getResult().getString("max_dep");
                    String avg_dep_txt = task.getResult().getString("avg_dep");
                    String weight_txt = task.getResult().getString("weight");
                    String min_tmp_txt = task.getResult().getString("min_tmp");
                    String avg_tmp_txt = task.getResult().getString("avg_tmp");
                    String BCD_brand_txt = task.getResult().getString("BCD_brand");
                    String mask_brand_txt = task.getResult().getString("mask_brand");
                    String watch_brand_txt = task.getResult().getString("watch_brand");
                    String wetsuit_brand_txt = task.getResult().getString("wetsuit_brand");
                    String fin_brand_txt = task.getResult().getString("fin_brand");
                    String cam_brand_txt = task.getResult().getString("cam_brand");

                    think.setText(think_txt);
                    title.setText(title_txt);
                    water.setText(water_txt);
                    scenery.setText(scenery_txt);
                    weather.setText(weather_txt);
                    start_time.setText(start_time_txt);
                    end_time.setText(end_time_txt);
                    dive_date.setText(dive_date_txt);
                    dive_loc.setText(dive_loc_txt);
                    num_dive.setText(num_dive_txt);
                    max_dep.setText(max_dep_txt);
                    avg_dep.setText(avg_dep_txt);
                    weight.setText(weight_txt);
                    min_temp.setText(min_tmp_txt);
                    avg_tmp.setText(avg_tmp_txt);
                    BCD_brand.setText(BCD_brand_txt);
                    mask_brand.setText(mask_brand_txt);
                    watch_brand.setText(watch_brand_txt);
                    wetsuit_brand.setText(wetsuit_brand_txt);
                    fin_brand.setText(fin_brand_txt);
                    cam_brand.setText(cam_brand_txt);

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
