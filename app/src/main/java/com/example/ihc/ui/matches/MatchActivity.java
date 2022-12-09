package com.example.ihc.ui.matches;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ihc.R;
import com.example.ihc.databinding.ActivityMatchBinding;
import com.example.ihc.ui.notifications.ChatActivity;
import com.squareup.picasso.Picasso;


public class MatchActivity extends AppCompatActivity {
    ActivityMatchBinding binding;

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
                startActivity(i);
            });
        }
    }
}