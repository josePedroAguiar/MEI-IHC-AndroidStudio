package com.example.ihc.ui.home;

import static com.example.ihc.MainActivity.userArrayList;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bluehomestudio.luckywheel.LuckyWheel;
import com.bluehomestudio.luckywheel.WheelItem;
import com.example.ihc.Login;
import com.example.ihc.R;
import com.example.ihc.data.User;
import com.example.ihc.databinding.FragmentHomeBinding;
import com.example.ihc.ui.matches.MatchActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

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
            updateMatches(userArrayList.get(Integer.parseInt(points)));
            Intent i = new Intent(getActivity(), MatchActivity.class);
            i.putExtra("name", userArrayList.get(Integer.parseInt(points)).getName());
            i.putExtra("phone", userArrayList.get(Integer.parseInt(points)).getPhoneNo());
            i.putExtra("country",userArrayList.get(Integer.parseInt(points)).getCountry());
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
        int i=0;
        for (User u: userArrayList){
            if(i%2==0){
                WheelItem whellItem = new WheelItem(ResourcesCompat.getColor(getResources(), R.color.purple_500, null),
                        BitmapFactory.decodeResource(getResources(), R.drawable.test), u.getName());
                wheelItemList.add(whellItem);
            }
            else{
                WheelItem whellItem = new WheelItem(ResourcesCompat.getColor(getResources(), R.color.purple_200, null),
                        BitmapFactory.decodeResource(getResources(), R.drawable.test), u.getName());
                wheelItemList.add(whellItem);
            }
            i++;
        }
        /*WheelItem whellItem = new WheelItem(ResourcesCompat.getColor(getResources(), R.color.purple_500, null),
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
        wheelItemList.add(whellItem6);*/
    }

    public void signOut() {
        // [START auth_sign_out]
        FirebaseAuth.getInstance().signOut();
        // [END auth_sign_out]
    }
    private void addDataToFirestore(@NonNull User user) {

        // creating a collection reference
        // for our Firebase Firetore database.
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Match courses = new Match(user.getName(),user.getCountry());
        FirebaseFirestore.getInstance().collection("/matches").add(user).
                addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // after the data addition is successful
                        // we are displaying a success toast message.
                        Toast.makeText(getActivity(), "Your Match has been added to Firebase Firestore", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // this method is called when the data addition process is failed.
                        // displaying a toast message when data addition is failed.
                        Toast.makeText(getActivity(), "Fail to add course \n" + exception, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    void updateMatches(@NonNull User user){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        DocumentReference washingtonRef = FirebaseFirestore.getInstance().collection("/users").document(currentUser.getUid());
        washingtonRef.update("matches", FieldValue.arrayUnion(user.getUuid()));
    }


}




