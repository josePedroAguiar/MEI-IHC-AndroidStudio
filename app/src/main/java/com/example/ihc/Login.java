package com.example.ihc;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private Button btn;
    private EditText email, password;
    private TextView registerLink;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    // [END declare_auth]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_login);
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

        btn = findViewById(R.id.login_btn);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        registerLink = findViewById(R.id.link_redirect);
        registerLink.setOnClickListener(v -> redirectToRegister());

        btn.setOnClickListener(view -> {
            final String em = email.getText().toString();
            final String pass = password.getText().toString();

            if (TextUtils.isEmpty(em) || TextUtils.isEmpty(pass)) {
                Log.w(TAG, "Email or Password empty!");
                Toast.makeText(Login.this, "Email or Password empty!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(em, pass).addOnCompleteListener(Login.this, task -> {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    Log.d(TAG, "signInWithCustomToken:success");
                    Toast.makeText(Login.this, "Logged in with success.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(user);

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                    Toast.makeText(Login.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            });
        });
    }

    private void redirectToRegister() {
        Intent view = new Intent(Login.this, Register.class);
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

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(Login.this, Splash.class);
        startActivity(intent);
        finish();
    }
}