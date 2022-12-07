package com.example.ihc.ui.notifications;

import static com.example.ihc.MainActivity.userArrayList;

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
    ArrayList<User> messaged;


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

        fetchUsers();

        messaged = new ArrayList<>();
        fetchMessagedUsers();
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
            //extras.putInt("imageid", userArrayList.get(position).getPhotoUri());
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

    private void fetchUsers() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!=null) {
            FirebaseFirestore.getInstance().collection("/users")
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            Log.e("Teste", error.getMessage(), error);
                            return;
                        }

                        List<DocumentSnapshot> docs = value.getDocuments();
                        Log.d("TAMANHO DOCS USER", Integer.toString(docs.size()));


                        for (DocumentSnapshot doc : docs) {
                            User user = doc.toObject(User.class);
                            assert user != null;
                            //Log.d("Teste", user.getName());
                            int flag = 0;
                            if(user.getUuid()!=null && user.getUuid().compareTo(currentUser.getUid())!=0) {
                                for (int i = 0; i < userArrayList.size(); i++) {
                                    if(user.getUuid().compareTo(userArrayList.get(i).getUuid()) == 0) flag = 1;
                                }
                                if(flag == 0) userArrayList.add(user);
                            }
                            //Log.e("DEBUG-1",userArrayList.size()+"");
                        }
                    });}
    }

    private void fetchMessagedUsers() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String fromID = currentUser.getUid();

        if (currentUser!=null) {
            for(int i = 0; i < userArrayList.size(); i++) {
                User thisUser = userArrayList.get(i);
                String toID = thisUser.getUuid();
                if(fromID.compareTo(toID) != 0 ) Log.d("Current fromID", fromID);
                FirebaseFirestore.getInstance().collection("conversations")
                        .document(fromID)
                        .collection(toID)
                        .addSnapshotListener((value, error) -> {
                            if (error != null) {
                                Log.e("Erro fetch messaged", error.getMessage(), error);
                                return;
                            }

                            List<DocumentSnapshot> docs = value.getDocuments();
                            Log.d("TAMANHO DOCS", Integer.toString(docs.size()));

                            for (DocumentSnapshot doc : docs) {
                                Chat chat = doc.toObject(Chat.class);
                                //assert chat != null;
                                if (chat != null) {
                                    messaged.add(thisUser);
                                    Log.d("Teste log", thisUser.getName());
                                }
                                else Log.d("Chat nulo", "erroooooooooooooooooo");

                            }
                        });
            }

        }
    }
}