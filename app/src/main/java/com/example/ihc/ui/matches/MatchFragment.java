package com.example.ihc.ui.matches;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ihc.R;
import com.example.ihc.data.User;
import com.example.ihc.databinding.FragmentMathesBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MatchFragment extends Fragment {

    private FragmentMathesBinding binding;
    public static int time = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MatchViewModel homeViewModel =
                new ViewModelProvider(this).get(MatchViewModel.class);

        binding = FragmentMathesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ///


        String[] name = {"Christopher", "Craig", "Sergio", "Mubariz", "Mike", "Michael", "Toa", "Ivana", "Alex"};
        String[] lastMessage = {"Heye", "Supp", "Let's Catchup", "Dinner tonight?", "Gotta go",
                "i'm in meeting", "Gotcha", "Let's Go", "any Weekend Plans?"};
        String[] lastmsgTime = {"8:45 pm", "9:00 am", "7:34 pm", "6:32 am", "5:76 am",
                "5:00 am", "7:34 pm", "2:32 am", "7:76 am"};
        String[] phoneNo = {"7656610000", "9999043232", "7834354323", "9876543211", "5434432343",
                "9439043232", "7534354323", "6545543211", "7654432343"};
        String[] country = {"United States", "Russia", "India", "Israel", "Germany", "Thailand", "Canada", "France", "Switzerland"};

        ArrayList<Match> userArrayList = new ArrayList<>();




        for (int i = 0; i < 9; i++) {
            //

            //

            //
            Match match = new Match(name[i], lastMessage[i], lastmsgTime[i], phoneNo[i], country[i], 0);
            userArrayList.add(match);

        }
        ///


        ListAdapterMatch adapter = new ListAdapterMatch(getActivity(), userArrayList);
        //ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),R.layout.activity_listview,R.id.text_view,nomes);
        ListView listView = root.findViewById(R.id.match_list);
        listView.setAdapter(adapter);
        listView.setClickable(true);
        listView.setOnItemClickListener((parent, view, position, id) -> {

            Intent i = new Intent(getActivity(), MatchActivity.class);
            i.putExtra("name", name[position]);
            i.putExtra("phone", phoneNo[position]);
            i.putExtra("country", country[position]);
            //i.putExtra("imageid",imageId[position]);
            startActivity(i);

        });


        return root;
    }



   static public User correntUser;
   void getCurrentUser(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        DocumentReference docRef =FirebaseFirestore.getInstance().collection("/users").document(currentUser.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                correntUser=user;
            }}
        );
    }
}




