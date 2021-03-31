package com.capstone.dolphindive.utility;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;

public class UserProfile {

    private DocumentReference documentReference;
    private String userId;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public UserProfile(String userId){
        this.userId = userId;
        documentReference = db.collection("Users").document(userId);
    }


    public void getProfile(UserProfileCallback customCallback){
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                HashMap<String,String> profile = new HashMap<String,String>();
                if(task.getResult().exists()){
                    String name = task.getResult().getString("name");
                    String email = task.getResult().getString("email");
                    String phone = task.getResult().getString("phone");
                    String address = task.getResult().getString("address");
                    String diverId = task.getResult().getString("diverId");
                    String url = task.getResult().getString("url");
                    String numFollowing = task.getResult().getString("numFollowing");
                    String numFollower = task.getResult().getString("numFollower");
                    String numPosts = task.getResult().getString("numPosts");

                    profile.put("userId", userId);
                    profile.put("name", name);
                    profile.put("email", email);
                    profile.put("phone", phone);
                    profile.put("address", address);
                    profile.put("diverId", diverId);
                    profile.put("url", url);
                    profile.put("numPosts", numPosts);
                    profile.put("numFollowing", numFollowing);
                    profile.put("numFollower", numFollower);

                }else{
                    //remember to check if profile is null in your implementation.
                    profile=null;
                }
                if (customCallback != null) {
                    customCallback.onComplete(profile);
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    public void increaseFollowing(UserProfileCallback customCallback){
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(documentReference);

                //transaction.update(sfDocRef, "population", newPopulation);
                transaction.update(documentReference, "numFollowing", "2");

                // Success
                return null;
            }
        });
    }

    public void increaseFollower(UserProfileCallback customCallback){
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(documentReference);

                //transaction.update(sfDocRef, "population", newPopulation);
                transaction.update(documentReference, "numFollowing", "2");

                // Success
                return null;
            }
        });
    }




}