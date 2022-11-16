package com.example.ihc.ui.home;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    LuckyWheel wheel;
    List<WheelItem> wheelItemList=new ArrayList<>();
    String points;
    public static int time=0;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        wheel = binding.luckywheel;
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
        WheelItem whellItem4 = new WheelItem(ResourcesCompat.getColor(
                getResources(), R.color.purple_500, null), BitmapFactory.decodeResource(getResources(), R.drawable.test), "?");
        wheelItemList.add(whellItem4);
        WheelItem whellItem5 = new WheelItem(ResourcesCompat.getColor(
                getResources(), R.color.purple_200, null), BitmapFactory.decodeResource(getResources(), R.drawable.test), "?");
        wheelItemList.add(whellItem5);
        WheelItem whellItem6 = new WheelItem(ResourcesCompat.getColor(
                getResources(), R.color.teal_200, null), BitmapFactory.decodeResource(getResources(), R.drawable.test), "?");
        wheelItemList.add(whellItem6);
        if(time<1){
        wheel.addWheelItems(wheelItemList);
            time++;
        }

        wheel.setLuckyWheelReachTheTarget(new OnLuckyWheelReachTheTarget() {
            @Override
            public void onReachTarget() {
                Intent i = new Intent(getActivity(), Match.class);
                startActivity(i);
            }
        });

        Button a = binding.btn;
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();
            points= String.valueOf(random.nextInt(10));
            if(points.equals("0")){
        points=String.valueOf(1);
    }
            wheel.rotateWheelTo(Integer.parseInt(points));
            }
        });
        return  root;
    }
}




