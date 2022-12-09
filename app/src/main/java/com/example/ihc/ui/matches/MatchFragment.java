package com.example.ihc.ui.matches;

import static com.example.ihc.MainActivity.locationsMatches;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ihc.R;
import com.example.ihc.data.User;
import com.example.ihc.databinding.FragmentMathesBinding;

import java.util.ArrayList;

public class MatchFragment extends Fragment {

    private FragmentMathesBinding binding;
    public static ArrayList<User> userMatches = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MatchViewModel homeViewModel =
                new ViewModelProvider(this).get(MatchViewModel.class);

        binding = FragmentMathesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ArrayList<Match> userArrayList = new ArrayList<>();

        Log.e("wewe", Integer.toString(userMatches.size()));
        //Log.e("LOCATIONS SIZE", Integer.toString(locationsMatches.size()));
        int pos = 0;
        for (User user : userMatches) {
            Match match = new Match(user.getName(), locationsMatches.get(pos).getImage(), user.getAge(), user.getPhotoUri(), user.getCountry(), user.getPhotoUri());
            userArrayList.add(match);
            pos++;
        }

        ListAdapterMatch adapter = new ListAdapterMatch(getActivity(), userArrayList);
        ListView listView = root.findViewById(R.id.match_list);
        listView.setAdapter(adapter);
        listView.setClickable(true);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(getActivity(), MatchActivity.class);
            i.putExtra("name", userMatches.get(position).getName());
            i.putExtra("bio", userMatches.get(position).getBio());
            i.putExtra("age", userMatches.get(position).getAge());
            i.putExtra("phone", userMatches.get(position).getPhotoUri());
            i.putExtra("country", userMatches.get(position).getCountry());
            i.putExtra("id", userMatches.get(position).getUuid());
            i.putExtra("link", userMatches.get(position).getPhotoUri());
            i.putExtra("link_map", locationsMatches.get(position).getImage());
            i.putExtra("link_to_map", locationsMatches.get(position).getLink());
            startActivity(i);
        });

        return root;
    }
}




