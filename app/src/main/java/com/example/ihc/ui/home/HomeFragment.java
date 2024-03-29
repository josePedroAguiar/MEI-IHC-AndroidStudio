package com.example.ihc.ui.home;

import static com.example.ihc.MainActivity.userArrayList;
import static com.example.ihc.Splash.me;
import static com.example.ihc.ui.matches.MatchFragment.userMatches;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bluehomestudio.luckywheel.LuckyWheel;
import com.bluehomestudio.luckywheel.WheelItem;
import com.example.ihc.Login;
import com.example.ihc.R;
import com.example.ihc.Splash;
import com.example.ihc.data.Route;
import com.example.ihc.data.User;
import com.example.ihc.databinding.FragmentHomeBinding;
import com.example.ihc.ui.matches.MatchActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    private LuckyWheel wheel;
    private List<WheelItem> wheelItemList = new ArrayList<>();
    private String points;
    private int value;
    static Route route;
    private ImageButton logoutBtn;
    Integer map;
    void alert(){
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set the message show for the Alert time
        builder.setMessage("\n" +
                "This operation will make exit to this account.You will have to login again if you want to use the application .\n Do you want to continue?");

        // Set Alert Title
        builder.setTitle("Do you want to Logout?");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });
        builder.setPositiveButton("Yes ", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            signOut();
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        });
        builder.show();
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        wheel = binding.luckywheel;

        Random random = new Random();

        long len = userArrayList.size();
        if (userArrayList.size() == 0) {
            userArrayList.add(new User());
        }
        int in = wheelItemList.size();
        if (in < userArrayList.size()) {
            wheelItemList = new ArrayList<>();
            generateWheelItems();
        }
        wheel.addWheelItems(wheelItemList);

        wheel.setRotation(-90);
        ObjectAnimator animation = ObjectAnimator.ofFloat(wheel, "translationX", 550);
        animation.start();


        TextView text = binding.spinText;
        TextView text1 = binding.text;
        if (len > 0) {
            text1.setVisibility(View.INVISIBLE);
            text.setVisibility(View.VISIBLE);
            wheel.setVisibility(View.VISIBLE);
            wheel.setOnClickListener(view -> {
                map = random.nextInt(3);
                getLoctions(Integer.toString(map + 1));
                value = random.nextInt(userArrayList.size());
                points = String.valueOf(value);
                Date date = new Date();
                long time = date.getTime();
                if (me.getDate() == null ||
                        time >= Long.parseLong(me.getDate()) + 60000/* swap to 86400000 */) {
                    me.setDate(Long.toString(time));
                    wheel.rotateWheelTo(value + 1);
                } else
                    Toast.makeText(getActivity(), "⚠︎ Your daily spin has already been used!", Toast.LENGTH_SHORT).show();
            });
        } else {
            userArrayList = new ArrayList<>();
            text1.setVisibility(View.VISIBLE);
            text.setVisibility(View.INVISIBLE);
            wheel.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(), "⚠ ︎Come back later! We don't have a match for you yet", Toast.LENGTH_SHORT).show();
        }
        wheel.setLuckyWheelReachTheTarget(() -> {
            updateMatches(userArrayList.get(value));
            Intent i = new Intent(getActivity(), MatchActivity.class);
            points = String.valueOf(value);
            Log.e("ERR0 ", points);
            i.putExtra("name", userArrayList.get(value).getName());
            if (userArrayList.get(value).getUuid() != null)
                i.putExtra("uuid", userArrayList.get(value).getUuid());
            if (userArrayList.get(value).getBio() != null)
                i.putExtra("age", userArrayList.get(value).getAge());
            if (userArrayList.get(value).getBio() != null)
                i.putExtra("bio", userArrayList.get(value).getBio());
            if (userArrayList.get(value).getCountry() != null)
                i.putExtra("country", userArrayList.get(value).getCountry());
            if (userArrayList.get(value).getPhotoUri() != null)
                i.putExtra("link", userArrayList.get(value).getPhotoUri());
            if (userArrayList.get(value).getUuid() != null)
                i.putExtra("id", userArrayList.get(value).getUuid());

            i.putExtra("link_to_map", route.getLink());
            i.putExtra("link_map", route.getImage());
            startActivity(i);
        });

        logoutBtn = binding.logoutBtn;
        logoutBtn.setOnClickListener(v -> {
         alert();

        });
        return root;
    }


    private void generateWheelItems() {
        int i = 0;
        wheelItemList = new ArrayList<>();
        for (User u : userArrayList) {
            if (i % 2 == 0) {
                WheelItem whellItem = new WheelItem(ResourcesCompat.getColor(getResources(), R.color.purple_500, null),
                        BitmapFactory.decodeResource(getResources(), R.drawable.test), u.getName());
                wheelItemList.add(whellItem);
            } else {
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
        getActivity().finish();
        // [END auth_sign_out]
    }

    private void addDataToFirestore(@NonNull User user) {

        // creating a collection reference
        // for our Firebase Firetore database.
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Match courses = new Match(user.getName(),user.getCountry());
        FirebaseFirestore.getInstance().collection("/matches").add(user).
                addOnSuccessListener(documentReference -> {
                    // after the data addition is successful
                    // we are displaying a success toast message.
                    Toast.makeText(getActivity(), "Your Match has been added to Firebase Firestore", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(exception -> {
                    // this method is called when the data addition process is failed.
                    // displaying a toast message when data addition is failed.
                    Toast.makeText(getActivity(), "Fail to add course \n" + exception, Toast.LENGTH_SHORT).show();
                });
    }


    void updateMatches(@NonNull User user) {
        Intent i = new Intent(getActivity(), Splash.class);
        startActivity(i);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.e("DEBUG", "aaaaa");
        assert currentUser != null;
        DocumentReference washingtonRef = FirebaseFirestore.getInstance().collection("/users").document(currentUser.getUid());
        //washingtonRef.update("matches", FieldValue.arrayUnion(user.getUuid()));

        washingtonRef.update("matches", FieldValue.arrayUnion(user.getUuid() + "@" + (map + 1) + "@" + (userMatches.size())));
        washingtonRef.update("nMatches", FieldValue.increment(1));
        long time = new Date().getTime();

        washingtonRef.update("date", Long.toString(time));

        DocumentReference a = FirebaseFirestore.getInstance().collection("/users").document(user.getUuid());
        a.update("matches", FieldValue.arrayUnion(currentUser.getUid() + "@" + (map + 1) + "@" + user.getnMatches()));
        a.update("nMatches", FieldValue.increment(1));
    }
    void getLoctions(String uuid) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("/locations").document(uuid);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
                    route = documentSnapshot.toObject(Route.class);
                    assert route != null;
                    Log.e("OLLLLA", route.getLink());
                }
        );
    }



}



