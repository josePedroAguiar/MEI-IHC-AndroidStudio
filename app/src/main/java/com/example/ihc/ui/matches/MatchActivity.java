package com.example.ihc.ui.matches;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ihc.R;
import com.example.ihc.databinding.ActivityMatchBinding;

public class MatchActivity extends AppCompatActivity {

    ActivityMatchBinding binding;
    int[] imageIds = {R.drawable.austin_wade_x6uj51n5ce8_unsplash, R.drawable.hassan_khan_egvccebwodm_unsplash};
    int count = imageIds.length;
    // to keep current Index of ImageID array
    int currentIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMatchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();

        if (intent != null) {
            String name = intent.getStringExtra("name");
            String phone = intent.getStringExtra("phone");
            String bio = intent.getStringExtra("bio");
            String country = intent.getStringExtra("country");
            int imageid = intent.getIntExtra("imageid", R.drawable.ic_baseline_person_24);

            binding.nameProfile.setText(name);
            binding.bio.setText(bio);
            binding.phoneProfile.setText(phone);
            binding.countryProfile.setText(country);

            Button btnNext = findViewById(R.id.btnNext);
            Button btnPrevious = findViewById(R.id.btnPrevious);

            ImageSwitcher imageSwitcher = findViewById(R.id.container_image); // get reference of ImageSwitcher

            // Set the ViewFactory of the ImageSwitcher that will create ImageView object when asked
            imageSwitcher.setFactory(() -> {
                // Create a new ImageView and set it's properties
                ImageView imageView = new ImageView(getApplicationContext());
                // set Scale type of ImageView to Fit Center
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                // set the Height And Width of ImageView To FIll PARENT
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
                return imageView;
            });

            imageSwitcher.setImageResource(imageid); // set the image in ImageSwitcher

            // Declare in and out animations and load them using AnimationUtils class
            Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
            Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);

            // set the animation type to ImageSwitcher
            imageSwitcher.setInAnimation(in);
            imageSwitcher.setOutAnimation(out);


            // ClickListener for NEXT button
            // When clicked on Button ImageSwitcher will switch between Images
            // The current Image will go OUT and next Image  will come in with specified animation
            btnNext.setOnClickListener(v -> {
                currentIndex++;
                //  Check If index reaches maximum then reset it
                if (currentIndex == count)
                    currentIndex = 0;
                imageSwitcher.setImageResource(imageIds[currentIndex]); // set the image in ImageSwitcher
            });

            btnPrevious.setOnClickListener(v -> {
                currentIndex--;
                //  Check If index reaches maximum then reset it
                if (currentIndex <= -1)
                    currentIndex = count - 1;
                imageSwitcher.setImageResource(imageIds[currentIndex]); // set the image in ImageSwitcher
            });
        }

    }
}