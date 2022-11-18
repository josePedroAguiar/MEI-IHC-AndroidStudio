package com.example.ihc.ui.matches;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.ihc.R;
import com.example.ihc.databinding.ActivityMatchBinding;

public class MatchActivity extends AppCompatActivity {

    ActivityMatchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMatchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        Intent intent = this.getIntent();

        if (intent != null){

            String name = intent.getStringExtra("name");
            String phone = intent.getStringExtra("phone");
            String country = intent.getStringExtra("country");
            int imageid = intent.getIntExtra("imageid", R.drawable.ic_baseline_person_24);

            binding.nameProfile.setText(name);
            binding.phoneProfile.setText(phone);
            binding.countryProfile.setText(country);
            binding.profileImage.setImageResource(imageid);


        }

    }
}