package com.example.ihc.ui.matches;

import static com.example.ihc.MainActivity.locationsMatches;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ihc.R;
import com.example.ihc.data.User;
import com.example.ihc.databinding.FragmentMathesBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MatchFragment extends Fragment {

    private FragmentMathesBinding binding;
    public static int time = 0;
    public static ArrayList<User> userMatches=new ArrayList<>();
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


        Log.e("wewe",Integer.toString(userMatches.size()));
        Log.e("wewe",Integer.toString(locationsMatches.size()));
        int pos=0;
        for (User user:userMatches) {
            //

            //

            //
            Match match = new Match(user.getName(), locationsMatches.get(pos).getImage(), user.getUuid(), user.getPhotoUri(), user.getCountry(), user.getPhotoUri());
            userArrayList.add(match);
            pos++;

        }
        ///


        ListAdapterMatch adapter = new ListAdapterMatch(getActivity(), userArrayList);
        //ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),R.layout.activity_listview,R.id.text_view,nomes);
        ListView listView = root.findViewById(R.id.match_list);
        listView.setAdapter(adapter);
        listView.setClickable(true);
        listView.setOnItemClickListener((parent, view, position, id) -> {

            Intent i = new Intent(getActivity(), MatchActivity.class);
            i.putExtra("name", userMatches.get(position).getName());
            i.putExtra("bio", userMatches.get(position).getName());
            i.putExtra("phone", userMatches.get(position).getPhotoUri());
            i.putExtra("country", userMatches.get(position).getCountry());
            i.putExtra("link",userMatches.get(position).getPhotoUri());
            i.putExtra("link_map",locationsMatches.get(position).getImage());
            startActivity(i);

        });


        return root;
    }





}




