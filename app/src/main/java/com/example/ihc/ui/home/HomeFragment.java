package com.example.ihc.ui.home;

import static com.example.ihc.MainActivity.locationsMatches;
import static com.example.ihc.MainActivity.userArrayList;
import static com.example.ihc.Splash.me;
import static com.example.ihc.ui.matches.MatchFragment.userMatches;

import android.animation.ObjectAnimator;
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
import androidx.appcompat.app.ActionBar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bluehomestudio.luckywheel.LuckyWheel;
import com.bluehomestudio.luckywheel.WheelItem;
import com.example.ihc.Login;
import com.example.ihc.R;
import com.example.ihc.Register;
import com.example.ihc.Splash;
import com.example.ihc.data.Route;
import com.example.ihc.data.User;
import com.example.ihc.databinding.FragmentHomeBinding;
import com.example.ihc.ui.matches.MatchActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    private LuckyWheel wheel;

    private List<WheelItem> wheelItemList = new ArrayList<>();
    private String points;
    private  int value;
    static Route route;
    private ImageButton logoutBtn;
    //public static int time = 0;
    Integer map;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        wheel = binding.luckywheel;

        Random random = new Random();

        long len=userArrayList.size();
        if(userArrayList!=null) {
            if(userArrayList.size()==0){
                userArrayList.add(new User());
            }
            int in=wheelItemList.size();
            if(in<userArrayList.size()){
                wheelItemList=new ArrayList<>();
                generateWheelItems();
            }
                wheel.addWheelItems(wheelItemList);




            wheel.setRotation(-90);
            ObjectAnimator animation = ObjectAnimator.ofFloat(wheel, "translationX", 550);
            animation.start();


            TextView text = binding.spinText;
            TextView text1 = binding.text;
            if(len>0){
                text1.setVisibility(View.INVISIBLE);
                text.setVisibility(View.VISIBLE);
                wheel.setVisibility(View.VISIBLE);
            wheel.setOnClickListener(view -> {
                map=random.nextInt(3);
                getLoctions(Integer.toString(map+1));
                value=random.nextInt(userArrayList.size());
                points = String.valueOf(value);
                Date date= new Date();
                Long time=date.getTime();
                if(me.getDate()==null ||
                        time>=Long.parseLong(me.getDate())+60000/* swap to 86400000 */)
                {
                    me.setDate(time.toString());
                    wheel.rotateWheelTo(value+1);         }
                else
                    Toast.makeText(getActivity(),"⚠︎ Your daily spin has already been used!", Toast.LENGTH_SHORT).show();
            });
            }
            else  {
                    userArrayList=new ArrayList<>();
                    text1.setVisibility(View.VISIBLE);
                    text.setVisibility(View.INVISIBLE);
                    wheel.setVisibility(View.INVISIBLE);
                  Toast.makeText(getActivity(),"⚠ ︎Come back later! We don't have a match for you yet", Toast.LENGTH_SHORT).show();}
            wheel.setLuckyWheelReachTheTarget(() -> {

                updateMatches(userArrayList.get(value));
                Intent i = new Intent(getActivity(), MatchActivity.class);
                points = String.valueOf(value);
                Log.e("ERR0 ",points);
                i.putExtra("name", userArrayList.get(value).getName());
                if(userArrayList.get(value).getUuid()!=null)
                    i.putExtra("uuid", userArrayList.get(value).getUuid());
                if(userArrayList.get(value).getBio()!=null)
                    i.putExtra("age",  userArrayList.get(value).getAge());
                if(userArrayList.get(value).getBio()!=null)
                    i.putExtra("bio",  userArrayList.get(value).getBio());
                if(userArrayList.get(value).getCountry()!=null)
                    i.putExtra("country", userArrayList.get(value).getCountry());
                if(userArrayList.get(value).getPhotoUri()!=null)
                    i.putExtra("link",userArrayList.get(value).getPhotoUri());
                if(userArrayList.get(value).getUuid()!=null)
                    i.putExtra("id", userArrayList.get(value).getUuid());;



                i.putExtra("link_to_map",route.getLink());
                i.putExtra("link_map",route.getImage());
                startActivity(i);

            });


        }

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
        wheelItemList=new ArrayList<>();
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

        getActivity().finish();
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
        Intent i = new Intent(getActivity(), Splash.class);
        startActivity(i);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.e("DEBUG","aaaaa");
        DocumentReference washingtonRef = FirebaseFirestore.getInstance().collection("/users").document(currentUser.getUid());
        //washingtonRef.update("matches", FieldValue.arrayUnion(user.getUuid()));

        washingtonRef.update("matches", FieldValue.arrayUnion(user.getUuid()+"@"+(map+1)+"@"+(userMatches.size())));
        washingtonRef.update("nMatches", FieldValue.increment(1));
        Long time =new Date().getTime();

        washingtonRef.update("date", time.toString());

        DocumentReference a = FirebaseFirestore.getInstance().collection("/users").document(user.getUuid());
        a.update("matches", FieldValue.arrayUnion(currentUser.getUid()+"@"+(map+1)+"@"+user.getnMatches()));
        a.update("nMatches", FieldValue.increment(1));
    }

    void getLoctions(String uuid){

        DocumentReference docRef =FirebaseFirestore.getInstance().collection("/locations").document(uuid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                 route = documentSnapshot.toObject(Route.class);
                 Log.e("OLLLLA",route.getLink());

            }}
        );
    }
}




