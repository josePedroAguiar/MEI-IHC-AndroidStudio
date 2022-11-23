package com.example.ihc;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ihc.data.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private Button btn;
    private EditText name, email, password;
    private TextView loginLink;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    // [END declare_auth]

    private User user;
    private String USERS = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        firebaseAuthStateListener = firebaseAuth -> {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null)
                updateUI(user);
        };
        // [END initialize_auth]

        btn = findViewById(R.id.register_btn);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginLink = findViewById(R.id.link_redirect);
        loginLink.setOnClickListener(v -> redirectToLogin());

        btn.setOnClickListener(view -> {
            final String n = name.getText().toString();
            final String em = email.getText().toString();
            final String pass = password.getText().toString();
            if (TextUtils.isEmpty(em) || TextUtils.isEmpty(pass)) {
                Log.w(TAG, "Email or Password empty!");
                Toast.makeText(Register.this, "Email or Password empty!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (pass.length() < 6) {
                Log.w(TAG, "Password needs 6 or more characters!");
                Toast.makeText(Register.this, "Password needs 6 or more characters!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            user = new User(n, em);

            mAuth.createUserWithEmailAndPassword(em, pass).addOnCompleteListener(Register.this, task -> {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCustomToken:success");
                    Toast.makeText(Register.this, "User registered with success.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                    Toast.makeText(Register.this, "User register failed.",
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void redirectToLogin() {
        Intent view = new Intent(Register.this, Login.class);
        startActivity(view);
        finish();
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }
    // [END on_start_check_user]

    // [START on_start_check_user]
    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
    // [END on_start_check_user]

    private void updateUI(FirebaseUser currentUser) {
        FirebaseFirestore.getInstance().collection(USERS).document(currentUser.getUid()).set(user)
                .addOnSuccessListener(documentReference ->
                        Log.d(TAG, "User added with ID: " + currentUser.getUid()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

        Intent intent = new Intent(Register.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}