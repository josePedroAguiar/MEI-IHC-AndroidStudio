package com.example.ihc.ui.home;

import static com.example.ihc.MainActivity.locationsMatches;
import static com.example.ihc.MainActivity.userArrayList;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    private LuckyWheel wheel;

    private final List<WheelItem> wheelItemList = new ArrayList<>();
    private String points;
    private  int value;

    private ImageButton logoutBtn;
    //public static int time = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        if(userArrayList!=null) {
            if(userArrayList.size()==0){
                userArrayList.add(new User());
            }
            if(wheelItemList.size()<userArrayList.size())
            generateWheelItems();
            wheel = binding.luckywheel;
            wheel.addWheelItems(wheelItemList);

            wheel.setRotation(-90);
            ObjectAnimator animation = ObjectAnimator.ofFloat(wheel, "translationX", 550);
            animation.start();


            LuckyWheel a = binding.luckywheel;

            a.setOnClickListener(view -> {
                Random random = new Random();
                value=random.nextInt(userArrayList.size());
                points = String.valueOf(value);
                Toast.makeText(getActivity(),points,Toast.LENGTH_SHORT).show();
                wheel.rotateWheelTo(value+1);
            });
            wheel.setLuckyWheelReachTheTarget(() -> {
                updateMatches(userArrayList.get(value));
                Intent i = new Intent(getActivity(), MatchActivity.class);
                points = String.valueOf(value);
                Log.e("ERR0 ",points);
                i.putExtra("name", userArrayList.get(value).getName());
                i.putExtra("bio", "ola");
                i.putExtra("country", userArrayList.get(value).getCountry());
                i.putExtra("link",userArrayList.get(value).getPhotoUri());
                i.putExtra("id", userArrayList.get(value).getUuid());
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
        Intent i = new Intent(getActivity(), Splash.class);
        startActivity(i);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.e("DEBUG","aaaaa");
        DocumentReference washingtonRef = FirebaseFirestore.getInstance().collection("/users").document(currentUser.getUid());
        //washingtonRef.update("matches", FieldValue.arrayUnion(user.getUuid()));
        Random random = new Random();
        Integer map=random.nextInt(4);
        washingtonRef.update("matches", FieldValue.arrayUnion(user.getUuid()+"@"+map+"@"+(locationsMatches.size()+1)));
    }
    boolean getUser(String uuid,String local,String pos){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!=null) {
            DocumentReference docRef =FirebaseFirestore.getInstance().collection("/users").document(uuid);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                  @Override
                                                  public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                      User user  = documentSnapshot.toObject(User.class);
                                                      if(user.getUuid().compareTo(currentUser.getUid())!=0){
                                                          user.setOrder(Integer.parseInt(pos));
                                                          userMatches.add(user);
                                                          Collections.sort(userMatches, new Comparator<User>() {
                                                              @Override
                                                              public int compare(User a1, User a2) {
                                                                  return a1.getOrder() - a2.getOrder();
                                                              }
                                                          });
                                                          getLoctions(local,pos);
                                                          Log.e(":(",Integer.toString(user.getOrder()));

                                                      }


                                                  }
                                              }
            );
        }
        return false;

    }
    void getMatches() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!=null) {
            Log.e("DEBUG", "aaaaa");
            FirebaseFirestore.getInstance().collection("/users")
                    .document(currentUser.getUid()).get()
                    .addOnCompleteListener(
                            new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot document = task.getResult();
                                    List<String> group = (List<String>) document.get("matches");
                                    for (String a : group) {
                                        String array[]=a.split("@");
                                        getUser(array[0],array[1],array[2]);
                                    }


                                }
                            });
        }
    }
    void getLoctions(String uuid,String pos){
        DocumentReference docRef =FirebaseFirestore.getInstance().collection("/locations").document(uuid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Route route  = documentSnapshot.toObject(Route.class);
                route.setOrder(Integer.parseInt(pos));
                locationsMatches.add(route);
                Collections.sort(locationsMatches, new Comparator<Route>() {
                    @Override
                    public int compare(Route a1, Route a2) {
                        return a1.getOrder() - a2.getOrder();
                    }
                });

            }}
        );
    }


}




