package com.example.ihc;

import static com.example.ihc.MainActivity.locationsMatches;
import static com.example.ihc.MainActivity.userArrayList;
import static com.example.ihc.ui.matches.MatchFragment.userMatches;

import static java.util.Collections.sort;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.ihc.data.Route;
import com.example.ihc.data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Splash extends AppCompatActivity {
        public static  User me= new User();
        @Override
        protected void onCreate (Bundle savedInstanceState) {
            super.onCreate (savedInstanceState);
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
            setContentView(R.layout.activity_slash);
            userArrayList=new ArrayList<>();
            locationsMatches=new ArrayList<>();
            userMatches=new ArrayList<>();

            getMatches();
            fetchUsers();
            getUser();

            Handler handler =new Handler();


            handler.postDelayed (new Runnable () {
                @Override
                public void run(){

                    startActivity (new Intent(  Splash.this, MainActivity.class));
                finish ();}}, 5000);
            }

    private void fetchUsers() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!=null) {
        FirebaseFirestore.getInstance().collection("/users")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("Teste", error.getMessage(), error);
                        return;
                    }

                    List<DocumentSnapshot> docs = value.getDocuments();

                    for (DocumentSnapshot doc : docs) {
                        User user = doc.toObject(User.class);
                        assert user != null;
                        Log.d("Teste", user.getName());
                        if(user.getUuid()!=null&&user.getUuid().compareTo(currentUser.getUid())!=0)
                            userArrayList.add(user);
                        //Log.e("DEBUG-1",userArrayList.size()+"");
                    }
                });}
    }
    void getUser(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!=null) {
            DocumentReference docRef =FirebaseFirestore.getInstance().collection("/users").document(currentUser.getUid());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                      @Override
                      public void onSuccess(DocumentSnapshot documentSnapshot) {
                           me  = documentSnapshot.toObject(User.class);
                          }

            });
        }
        }
    boolean getUser(String uuid,String local,String pos){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!=null) {
        DocumentReference docRef =FirebaseFirestore.getInstance().collection("/users").document(uuid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user  = documentSnapshot.toObject(User.class);
                if(user.getUuid().compareTo(currentUser.getUid())!=0){
                    user.setOrder(Integer.parseInt(pos));
                    userMatches.add(user);
                    Collections.sort(userMatches, new Comparator<User>() {
                        @Override
                        public int compare(User a1, User a2) {
                            return a1.getOrder() - a2.getOrder();
                        }
                    });
                    getLoctions(local,pos);
                    Log.e(":(",Integer.toString(user.getOrder()));

                }


            }
        }
        );
        }
        return false;

    }
    void getMatches() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!=null) {
            Log.e("DEBUG", "aaaaa");
            FirebaseFirestore.getInstance().collection("/users")
                    .document(currentUser.getUid()).get()
                    .addOnCompleteListener(
                            new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot document = task.getResult();
                                    List<String> group = (List<String>) document.get("matches");
                                   if(group!=null){
                                    for (String a : group) {
                                        if(a!=null){
                                        String array[]=a.split("@");
                                        getUser(array[0],array[1],array[2]);
                                        }
                                    }
                                   }


                                }
                            });
        }
    }
    void getLoctions(String uuid,String pos){
            DocumentReference docRef =FirebaseFirestore.getInstance().collection("/locations").document(uuid);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                  @Override
                                                  public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                      Route route  = documentSnapshot.toObject(Route.class);
                                                      route.setOrder(Integer.parseInt(pos));
                                                      locationsMatches.add(route);
                                                      Collections.sort(locationsMatches, new Comparator<Route>() {
                                                          @Override
                                                          public int compare(Route a1, Route a2) {
                                                              return a1.getOrder() - a2.getOrder();
                                                          }
                                                      });

                                              }}
            );
    }

}