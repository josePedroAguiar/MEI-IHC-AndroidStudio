package com.example.ihc.ui.home;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bluehomestudio.luckywheel.LuckyWheel;
import com.bluehomestudio.luckywheel.WheelItem;
import com.example.ihc.Login;
import com.example.ihc.Match;
import com.example.ihc.R;
import com.example.ihc.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    private LuckyWheel wheel;

    private final List<WheelItem> wheelItemList = new ArrayList<>();
    private String points;

    private ImageButton logoutBtn;
    //public static int time = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        generateWheelItems();
        wheel = binding.luckywheel;
        wheel.addWheelItems(wheelItemList);
        wheel.setTarget(1);

        wheel.setRotation(-90);
        ObjectAnimator animation = ObjectAnimator.ofFloat(wheel, "translationX", 550);
        animation.start();

        wheel.setLuckyWheelReachTheTarget(() -> {
            Intent i = new Intent(getActivity(), Match.class);
            startActivity(i);
        });

        LuckyWheel a = binding.luckywheel;
        a.setOnClickListener(view -> {
            Random random = new Random();
            points = String.valueOf(random.nextInt(10));
            if (points.equals("0")) {
                points = String.valueOf(1);
            }
            wheel.rotateWheelTo(Integer.parseInt(points));
        });

        logoutBtn = binding.logoutBtn;
        logoutBtn.setOnClickListener(v -> {
            signOut();
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        });
        return root;
    }

    private void generateWheelItems() {
        WheelItem whellItem = new WheelItem(ResourcesCompat.getColor(getResources(), R.color.purple_500, null),
                BitmapFactory.decodeResource(getResources(), R.drawable.test), "?");
        wheelItemList.add(whellItem);
        WheelItem whellItem2 = new WheelItem(ResourcesCompat.getColor(getResources(), R.color.purple_200, null),
                BitmapFactory.decodeResource(getResources(), R.drawable.test), "?");
        wheelItemList.add(whellItem2);
        WheelItem whellItem3 = new WheelItem(ResourcesCompat.getColor(getResources(), R.color.teal_200, null),
                BitmapFactory.decodeResource(getResources(), R.drawable.test), "?");
        wheelItemList.add(whellItem3);
        WheelItem whellItem4 = new WheelItem(ResourcesCompat.getColor(getResources(), R.color.purple_500, null),
                BitmapFactory.decodeResource(getResources(), R.drawable.test), "?");
        wheelItemList.add(whellItem4);
        WheelItem whellItem5 = new WheelItem(ResourcesCompat.getColor(getResources(), R.color.purple_200, null),
                BitmapFactory.decodeResource(getResources(), R.drawable.test), "?");
        wheelItemList.add(whellItem5);
        WheelItem whellItem6 = new WheelItem(ResourcesCompat.getColor(getResources(), R.color.teal_200, null),
                BitmapFactory.decodeResource(getResources(), R.drawable.test), "?");
        wheelItemList.add(whellItem6);
    }

    public void signOut() {
        // [START auth_sign_out]
        FirebaseAuth.getInstance().signOut();
        // [END auth_sign_out]
    }

}




