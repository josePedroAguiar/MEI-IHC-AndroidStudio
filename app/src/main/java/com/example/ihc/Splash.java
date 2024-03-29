package com.example.ihc;

import static com.example.ihc.MainActivity.locationsMatches;
import static com.example.ihc.MainActivity.userArrayList;
import static com.example.ihc.MainActivity.messaged;
import static com.example.ihc.ui.matches.MatchFragment.userMatches;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ihc.data.Route;
import com.example.ihc.data.User;
import com.example.ihc.ui.notifications.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


public class Splash extends AppCompatActivity {
    public static User me = new User();
    FirebaseUser currentUser;
    static public Integer b = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        setContentView(R.layout.activity_slash);
        userArrayList = new ArrayList<>();
        messaged = new ArrayList<>();
        locationsMatches = new ArrayList<>();
        userMatches = new ArrayList<>();

        getMatches();
        fetchUsers();
        //fetchMessagedUsers();
        getUser();
        //getSkip();
        Handler handler = new Handler();

        handler.postDelayed(() -> {
            startActivity(new Intent(Splash.this, MainActivity.class));
            finish();
        }, 5000);
    }
    public boolean checkCurrentUser() {
        // [START check_current_user]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null;
        // [END check_current_user]
    }

    private void fetchUsers() {
        //FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            FirebaseFirestore.getInstance().collection("/users").addSnapshotListener((value, error) -> {
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
                    if (user.getUuid() != null && user.getUuid().compareTo(currentUser.getUid()) != 0) {
                        userArrayList.add(user);
                    }
                    //Log.e("DEBUG-1",userArrayList.size()+"");
                }
                fetchMessagedUsers();
            });
        }
    }

    private void fetchMessagedUsers() {
        String fromID = currentUser.getUid();

        if (currentUser!=null) {
            for(int i = 0; i < userArrayList.size(); i++) {
                User thisUser = userArrayList.get(i);
                String toID = thisUser.getUuid();

                if(fromID.compareTo(toID) != 0 ) Log.d("Current fromID", fromID);

                FirebaseFirestore.getInstance().collection("conversations")
                        .document(fromID)
                        .collection(toID)
                        .addSnapshotListener((value, error) -> {
                            /*if (error != null) {
                                Log.e("Erro fetch messaged", error.getMessage(), error);
                                return;
                            }*/

                            List<DocumentSnapshot> docs = value.getDocuments();
                            Log.d("TAMANHO DOCS", Integer.toString(docs.size()));

                            for (DocumentSnapshot doc : docs) {
                                Chat chat = doc.toObject(Chat.class);
                                //assert chat != null;
                                if (chat != null) {
                                    int flag = 0;
                                    for(int j = 0; j < messaged.size(); j++) {
                                        if (messaged.get(j).getUuid().compareTo(thisUser.getUuid()) == 0) {
                                            flag = 1; //este user ja esta na lista
                                            break;
                                        }
                                    }
                                    if (flag == 0) messaged.add(thisUser);
                                    break;
                                    //Log.d("Teste log", chat.getText());
                                }
                                else Log.d("Chat nulo", "erroooooooooooooooooo");

                            }
                        });
            }

        }
    }

    void getUser() {
        //FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DocumentReference docRef = FirebaseFirestore.getInstance().collection("/users").document(currentUser.getUid());
            docRef.get().addOnSuccessListener(documentSnapshot -> me = documentSnapshot.toObject(User.class));
        }
    }

    boolean getUser(String uuid, String local, String pos) {
        //FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DocumentReference docRef = FirebaseFirestore.getInstance().collection("/users").document(uuid);
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                User user = documentSnapshot.toObject(User.class);
                assert user != null;
                if (user.getUuid().compareTo(currentUser.getUid()) != 0) {
                    user.setOrder(Integer.parseInt(pos));
                    userMatches.add(user);
                    userMatches.sort(Comparator.comparingInt(User::getOrder));
                    getLoctions(local, pos);
                    Log.e(":(", Integer.toString(user.getOrder()));
                }
            });
        }
        return false;

    }

    void getMatches() {
        //FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            Log.e("DEBUG", "aaaaa");
            FirebaseFirestore.getInstance().collection("/users").document(currentUser.getUid()).get().addOnCompleteListener(task -> {
                DocumentSnapshot document = task.getResult();
                List<String> group = (List<String>) document.get("matches");
                if (group != null) {
                    for (String a : group) {
                        if (a != null) {
                            String[] array = a.split("@");
                            getUser(array[0], array[1], array[2]);
                        }
                    }
                }
            });
        }
    }

    void getLoctions(String uuid, String pos) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("/locations").document(uuid);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            Route route = documentSnapshot.toObject(Route.class);
            assert route != null;
            route.setOrder(Integer.parseInt(pos));
            locationsMatches.add(route);
            locationsMatches.sort(Comparator.comparingInt(Route::getOrder));

        });
    }
    void getSkip() {
        if (currentUser != null) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("/users").document(currentUser.getUid());
        docRef.get().addOnSuccessListener(documentSnapshot -> {
                    b = Integer.parseInt(documentSnapshot.get("skip").toString());
                    Log.e("DEBUG", " "+b);
                }
        );
        }
    }
}