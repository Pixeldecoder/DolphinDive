package com.capstone.dolphindive;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DiveLog_Create_Scu_Frag extends Fragment implements View.OnClickListener{
    Button cancel;
    Button save;
    TextView think, water, scenery, weather, start_time, end_time, dive_date, dive_loc, num_dive, max_dep, avg_dep, weight, min_temp,
    avg_tmp, BCD_brand, mask_brand, watch_brand, wetsuit_brand, fin_brand, cam_brand, title;

    FirebaseFirestore db;
    DocumentReference documentReference;
    String uid;
    String logId;
    FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        logId = getArguments().getString("logId");
        return inflater.inflate(R.layout.divelog_scuba_frag, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db=  FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        documentReference = db.collection("Users").document(uid).collection("divelog").document(logId);

        cancel = view.findViewById(R.id.cancel_scuba);
        title = view.findViewById(R.id.title);
        save = view.findViewById(R.id.save_scuba);
        think = view.findViewById(R.id.think_content);
        water = view.findViewById(R.id.water_content);
        scenery = view.findViewById(R.id.scenery_content);
        weather = view.findViewById(R.id.weather_content);
        start_time = view.findViewById(R.id.start_time);
        end_time = view.findViewById(R.id.end_time);
        dive_date = view.findViewById(R.id.div_date);
        dive_loc = view.findViewById(R.id.dive_location);
        num_dive = view.findViewById(R.id.num_dive);
        max_dep = view.findViewById(R.id.max_dep_input);
        avg_dep = view.findViewById(R.id.avg_dep_input);
        weight = view.findViewById(R.id.weight_input);
        min_temp = view.findViewById(R.id.min_temp_input);
        avg_tmp = view.findViewById(R.id.avg_temp_input);
        BCD_brand = view.findViewById(R.id.BCD_Brand);
        mask_brand = view.findViewById(R.id.mask_Brand);
        watch_brand = view.findViewById(R.id.watch_brand);
        wetsuit_brand = view.findViewById(R.id.wetsuit_brand);
        fin_brand = view.findViewById(R.id.fin_brand);
        cam_brand = view.findViewById(R.id.cam_brand);

        save.setOnClickListener((View.OnClickListener) this);
        cancel.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_scuba:
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()){
                            Toast.makeText(getActivity(),"The log already existed",Toast.LENGTH_SHORT).show();
                        }else{
                            UploadData();
                        }
                    }
                });
                break;
            case R.id.cancel_scuba:
                getActivity().finish();
                break;
        }
    }


    private void UploadData() {
        String think_txt = think.getText().toString();
        String title_txt = title.getText().toString();
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

        if(  !TextUtils.isEmpty(title_txt)) {
            Map<String, String> divelog = new HashMap<>();
            divelog.put("id", logId);
            divelog.put("genre", "Scuba");
            divelog.put("think", think_txt);
            divelog.put("title", title_txt);
            divelog.put("water", water_txt);
            divelog.put("scenery", scenery_txt);
            divelog.put("weather", weather_txt);
            divelog.put("start_time", start_time_txt);
            divelog.put("end_time", end_time_txt);
            divelog.put("dive_date", dive_date_txt);
            divelog.put("dive_loc", dive_loc_txt);
            divelog.put("num_dive", num_dive_txt);
            divelog.put("max_dep", max_dep_txt);
            divelog.put("avg_dep", avg_dep_txt);
            divelog.put("weight", weight_txt);
            divelog.put("min_tmp", min_tmp_txt);
            divelog.put("avg_tmp", avg_tmp_txt);
            divelog.put("BCD_brand", BCD_brand_txt);
            divelog.put("mask_brand", mask_brand_txt);
            divelog.put("watch_brand", watch_brand_txt);
            divelog.put("wetsuit_brand", wetsuit_brand_txt);
            divelog.put("fin_brand", fin_brand_txt);
            divelog.put("cam_brand", cam_brand_txt);


            documentReference.set(divelog).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getActivity(), "Dive Log Created",Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "failed",Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(getActivity(), "Title is Required",Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public void onStart() {
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
