package com.capstone.dolphindive;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.capstone.dolphindive.R;
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

import java.util.HashMap;
import java.util.Map;


public class DiveLog_Free_Frag extends Fragment implements View.OnClickListener{
    Button save;
    Button cancel;
    TextView think, start_time, end_time, dive_date,dive_loc,num_dive, max_dep, avg_dep, min_tmp, avg_tmp;

    FirebaseFirestore db;
    DocumentReference documentReference;
    String uid;
    String logNum;
    FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        logNum = getArguments().getString("numlog");
        return inflater.inflate(R.layout.divelog_free_frag, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db=  FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        documentReference = db.collection(uid).document("profile").collection("divelog").document(logNum);

        cancel = view.findViewById(R.id.cancel_free);
        save = view.findViewById(R.id.save_free);
        think = view.findViewById(R.id.think_content_free);
        start_time = view.findViewById(R.id.start_time_free);
        end_time = view.findViewById(R.id.end_time_free);
        dive_date = view.findViewById(R.id.div_date_free);
        dive_loc = view.findViewById(R.id.dive_location_free);
        num_dive = view.findViewById(R.id.num_dive_free);
        max_dep = view.findViewById(R.id.max_dep_input_free);
        avg_dep = view.findViewById(R.id.avg_dep_input_free);
        min_tmp = view.findViewById(R.id.min_temp_input_free);
        avg_tmp = view.findViewById(R.id.avg_temp_input_free);

        save.setOnClickListener((View.OnClickListener) this);
        cancel.setOnClickListener((View.OnClickListener) this);
        think.setOnClickListener((View.OnClickListener) this);
        start_time.setOnClickListener((View.OnClickListener) this);
        end_time.setOnClickListener((View.OnClickListener) this);
        dive_date.setOnClickListener((View.OnClickListener) this);
        dive_loc.setOnClickListener((View.OnClickListener) this);
        num_dive.setOnClickListener((View.OnClickListener) this);
        max_dep.setOnClickListener((View.OnClickListener) this);
        avg_dep.setOnClickListener((View.OnClickListener) this);
        min_tmp.setOnClickListener((View.OnClickListener) this);
        avg_tmp.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_free:
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()){
                            EditData();
                        }else{
                            UploadData();
                        }
                    }
                });
                break;
            case R.id.cancel_free:
                getActivity().finish();
                break;
        }
    }

    private void UploadData() {
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

        if(  !TextUtils.isEmpty(num_dive_txt)) {
            Map<String, String> divelog = new HashMap<>();
            divelog.put("genre", "free");
            divelog.put("think", think_txt);
            divelog.put("start_time", start_time_txt);
            divelog.put("end_time", end_time_txt);
            divelog.put("dive_date", dive_date_txt);
            divelog.put("dive_loc", dive_loc_txt);
            divelog.put("num_dive", num_dive_txt);
            divelog.put("max_dep", max_dep_txt);
            divelog.put("avg_dep", avg_dep_txt);
            divelog.put("min_tmp", min_tmp_txt);
            divelog.put("avg_tmp", avg_tmp_txt);


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
            Toast.makeText(getActivity(), "Number of Dive is Required",Toast.LENGTH_SHORT).show();
        }
    }


    private void EditData() {
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

        if(!TextUtils.isEmpty(num_dive_txt) ){

            final DocumentReference sfDocRef = db.collection(uid).document("profile").collection("divelog").document(logNum);

            db.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    DocumentSnapshot snapshot = transaction.get(sfDocRef);

                    //transaction.update(sfDocRef, "population", newPopulation);
                    transaction.update(sfDocRef, "think", think_txt);
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
                    Toast.makeText(getActivity(), "Divelog Updated",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Number of Dive is Required",Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onStart() {
        super.onStart();

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
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
