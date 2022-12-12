package com.example.ihc.ui.matches;

import static com.example.ihc.Splash.b;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ihc.R;
import com.example.ihc.Splash;
import com.example.ihc.data.User;
import com.example.ihc.databinding.ActivityMatchBinding;
import com.example.ihc.ui.notifications.ChatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.example.ihc.Splash.*;


public class MatchActivity extends AppCompatActivity {
    ActivityMatchBinding binding;
    void alert( Intent i){
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(MatchActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("\n" +
                "This operation redirects you out of the application. If you want to send a message or see your matches you will have to return to the application alone.\n Do you want to continue?");

        // Set Alert Title
        builder.setTitle("Do you want to continue?!");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            startActivity(i);
        });

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });
        builder.setPositiveButton("Yes and Never show again ", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            updateSkip();
            startActivity(i);
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMatchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();

        if (intent != null) {
            String name = intent.getStringExtra("name");
            String age = intent.getStringExtra("age");
            String bio = intent.getStringExtra("bio");
            String link = intent.getStringExtra("link");
            String link_map = intent.getStringExtra("link_map");
            String link_to_map = intent.getStringExtra("link_to_map");
            String country = intent.getStringExtra("country");
            String id = intent.getStringExtra("id");
            int imageid = intent.getIntExtra("imageid", R.drawable.ic_baseline_person_24);

            binding.nameProfile.setText(name);
            binding.bio.setText(bio);
            binding.age.setText(age);
            binding.countryProfile.setText(country);

            ImageView user = binding.image;
            user.setImageResource(imageid);

            if (link != null)
                Picasso.get().load(link).into(user);
            ImageView map = binding.map;
            Picasso.get().load(link_map).into(map);

            Button btnMessage = findViewById(R.id.button_message);
            btnMessage.setOnClickListener(v -> {
                Intent i = new Intent(this, ChatActivity.class);

                Bundle extras = new Bundle();
                extras.putString("name", name);
                extras.putInt("imageid", imageid);
                extras.putString("toID", id);
                i.putExtras(extras);
                startActivity(i);
            });
            ImageView map_card = findViewById(R.id.map);
            map_card.setOnClickListener(v -> {
                Uri uri = Uri.parse(link_to_map);

                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                getSkip();
                if (b!=1)
                alert(i);
                else
                    startActivity(i);
                //
            });

        }
    }
    void getSkip() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("/users").document(currentUser.getUid());
        docRef.get().addOnSuccessListener(documentSnapshot -> {
                    b = Integer.parseInt(documentSnapshot.get("skip").toString());
                }
        );
    }

    void updateSkip() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        DocumentReference washingtonRef = FirebaseFirestore.getInstance().collection("/users").document(currentUser.getUid());
        //washingtonRef.update("matches", FieldValue.arrayUnion(user.getUuid()));
        washingtonRef.update("skip", 1);
        b=1;

    }

}