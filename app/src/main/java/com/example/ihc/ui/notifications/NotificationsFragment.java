package com.example.ihc.ui.notifications;

import static com.example.ihc.MainActivity.userArrayList;
import static com.example.ihc.MainActivity.messaged;

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
import com.example.ihc.databinding.FragmentNotificationsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.ihc.data.User;
import com.example.ihc.Splash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    //ArrayList<String> nomes = new ArrayList<>(Arrays.asList("Miguel", "Tiago", "José", "Pedro", "Tomás"));
    //ArrayList<String> subitems = new ArrayList<>(Arrays.asList("Miguel", "Tiago", "José", "Pedro", "Tomás"));
    //ArrayList<User> messaged;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        /*[] name = {"Christopher", "Craig", "Sergio", "Mubariz", "Mike", "Michael", "Toa", "Ivana", "Alex"};
        String[] lastMessage = {"Heye", "Supp", "Let's Catchup", "Dinner tonight?", "Gotta go",
                "i'm in meeting", "Gotcha", "Let's Go", "any Weekend Plans?"};
        String[] lastmsgTime = {"8:45 pm", "9:00 am", "7:34 pm", "6:32 am", "5:76 am",
                "5:00 am", "7:34 pm", "2:32 am", "7:76 am"};
        String[] phoneNo = {"7656610000", "9999043232", "7834354323", "9876543211", "5434432343",
                "9439043232", "7534354323", "6545543211", "7654432343"};
        String[] country = {"United States", "Russia", "India", "Israel", "Germany", "Thailand", "Canada", "France", "Switzerland"};*/



        //ArrayList<User> userArrayList = new ArrayList<>();
        /*for (int i = 0; i < 9; i++) {
            User user = new User(name[i], phoneNo[i], country[i]);
            userArrayList.add(user);
        }*/

        //fetchUsers();


        //fetchMessagedUsers();
        Log.e("Messaged Size: ", Integer.toString(messaged.size()));


        for(int i = 0; i < messaged.size(); i++) {
            Log.d("TESTE NOME",messaged.get(i).getName());
        }

        ListAdapter adapter = new ListAdapter(getActivity(), messaged);  //userArrayList);
        //ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),R.layout.activity_listview,R.id.text_view,nomes);
        ListView listView = root.findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
        listView.setClickable(true);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(getActivity(), ChatActivity.class);

            Bundle extras = new Bundle();
            extras.putString("name", messaged.get(position).getName());
            extras.putString("imageid", messaged.get(position).getPhotoUri());
            extras.putString("toID", messaged.get(position).getUuid());


            i.putExtras(extras);
            startActivity(i);
        });


        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}