package com.example.ihc;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ihc.databinding.ActivityMatchBinding;

public class Match extends AppCompatActivity {

    private ActivityMatchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMatchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}