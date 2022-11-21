package com.example.ihc.ui.home;

import static com.example.ihc.MainActivity.userArrayList;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bluehomestudio.luckywheel.LuckyWheel;
import com.bluehomestudio.luckywheel.OnLuckyWheelReachTheTarget;
import com.bluehomestudio.luckywheel.WheelItem;

import com.example.ihc.Match;
import com.example.ihc.R;
import com.example.ihc.databinding.FragmentHomeBinding;
import com.example.ihc.ui.notifications.User;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    LuckyWheel wheel;
    List<WheelItem> wheelItemList = new ArrayList<>();
    String points;
    public static int time = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        wheel = binding.luckywheel;
        wheel.setRotation(-90);
        ObjectAnimator animation = ObjectAnimator.ofFloat(wheel, "translationX", 550);
        animation.start();


        int n_user=0;
        Log.e("DEBUG",Integer.toString(userArrayList.size()));
        for (User user : userArrayList){
        if(n_user==0){
            WheelItem whellItem = new WheelItem(ResourcesCompat.getColor(
                    getResources(), R.color.purple_500, null), BitmapFactory.decodeResource(getResources(), R.drawable.test), "?");
            wheelItemList.add(whellItem);
        }
        else {
            WheelItem whellItem = new WheelItem(ResourcesCompat.getColor(
                    getResources(), R.color.purple_200, null), BitmapFactory.decodeResource(getResources(), R.drawable.test), "?");
            wheelItemList.add(whellItem);

        }
            n_user++;
        }
        WheelItem whellItem = new WheelItem(ResourcesCompat.getColor(
                getResources(), R.color.purple_500, null), BitmapFactory.decodeResource(getResources(), R.drawable.test), "?");
        wheelItemList.add(whellItem);
        WheelItem whellItem2 = new WheelItem(ResourcesCompat.getColor(
                getResources(), R.color.purple_200, null), BitmapFactory.decodeResource(getResources(), R.drawable.test), "?");
        wheelItemList.add(whellItem2);
        WheelItem whellItem3 = new WheelItem(ResourcesCompat.getColor(
                getResources(), R.color.teal_200, null), BitmapFactory.decodeResource(getResources(), R.drawable.test), "?");
        wheelItemList.add(whellItem3);
        wheel.addWheelItems(wheelItemList);
        wheel = binding.luckywheel;
        if (time < 1) {
            wheel.addWheelItems(wheelItemList);
            time++;
        }

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
        return root;
    }
}




