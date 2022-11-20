package com.example.ihc.ui.notifications;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ihc.MainActivity;
import com.example.ihc.R;
import com.example.ihc.databinding.FragmentNotificationsBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    ArrayList <String> nomes = new ArrayList (Arrays.asList(new String[]{"Miguel", "Tiago", "José", "Pedro", "Tomás"}));
    ArrayList <String> subitems = new ArrayList (Arrays.asList(new String[]{"Miguel", "Tiago", "José", "Pedro", "Tomás"}));
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();




        ///

        fetchUsers();
        String[] name = {"Christopher","Craig","Sergio","Mubariz","Mike","Michael","Toa","Ivana","Alex"};
        String[] lastMessage = {"Heye","Supp","Let's Catchup","Dinner tonight?","Gotta go",
                "i'm in meeting","Gotcha","Let's Go","any Weekend Plans?"};
        String[] lastmsgTime = {"8:45 pm","9:00 am","7:34 pm","6:32 am","5:76 am",
                "5:00 am","7:34 pm","2:32 am","7:76 am"};
        String[] phoneNo = {"7656610000","9999043232","7834354323","9876543211","5434432343",
                "9439043232","7534354323","6545543211","7654432343"};
        String[] country = {"United States","Russia","India","Israel","Germany","Thailand","Canada","France","Switzerland"};

        ArrayList<User> userArrayList = new ArrayList<>();
        for(int i = 0;i<9;i++){

            User user = new User(name[i],lastMessage[i],lastmsgTime[i],phoneNo[i],country[i],0);
            userArrayList.add(user);

        }
        ///

        fetchUsers();
        ListAdapter adapter = new ListAdapter(getActivity(),userArrayList);
        //ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),R.layout.activity_listview,R.id.text_view,nomes);
        ListView listView =  root.findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
        listView.setClickable(true);
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void fetchUsers() {
        FirebaseFirestore.getInstance().collection("/users")
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.e("Teste", error.getMessage(), error);
                        return;
                    }
                List<DocumentSnapshot> docs = value.getDocuments();
                    for (DocumentSnapshot doc : docs) {
                        User user = doc.toObject(User.class);
                        Log.d("Teste", user.getName());
                    }
                }   });
    }
}