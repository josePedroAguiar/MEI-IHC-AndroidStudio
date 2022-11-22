package com.example.ihc;

import static com.example.ihc.MainActivity.userArrayList;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.ihc.ui.notifications.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class Slash extends AppCompatActivity {
        @Override
        protected void onCreate (Bundle savedInstanceState) {
            super.onCreate (savedInstanceState);
            setContentView(R.layout.activity_slash);
            Handler handler =new Handler();
            fetchUsers();
            handler.postDelayed (new Runnable () {
                @Override
                public void run(){

                    startActivity (new Intent(  Slash.this, MainActivity.class));
                finish ();}}, 4000);
            }

    private void fetchUsers() {

        FirebaseFirestore.getInstance().collection("/users")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("Teste", error.getMessage(), error);
                        return;
                    }
                    assert value != null;
                    List<DocumentSnapshot> docs = value.getDocuments();
                    for (DocumentSnapshot doc : docs) {
                        User user = doc.toObject(User.class);
                        assert user != null;
                        Log.d("Teste", user.getName());
                        userArrayList.add(user);
                        Log.e("DEBUG-1",userArrayList.size()+"");
                    }
                });
    }


}