package com.example.ihc;

import static com.example.ihc.MainActivity.locationsMatches;

import android.content.Intent;
import android.os.Bundle;

import com.example.ihc.ui.matches.MatchActivity;
import com.example.ihc.ui.notifications.ChatActivity;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ihc.databinding.ActivityMatchBinding;

public class Match extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMatchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMatchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }

     /*@Override
   public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_match);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }*/
}