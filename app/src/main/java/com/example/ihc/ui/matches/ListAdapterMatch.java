package com.example.ihc.ui.matches;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ihc.R;
import com.example.ihc.data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ListAdapterMatch extends ArrayAdapter<Match> {


    public ListAdapterMatch(Context context, ArrayList<Match> userArrayList){

        super(context, R.layout.items_match,userArrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Match match = getItem(position);
        if (convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.items_match,parent,false);

        }
        //ImageView imageView = convertView.findViewById(R.id.profile_pic);
        TextView userName = convertView.findViewById(R.id.personName);
        TextView bio = convertView.findViewById(R.id.bio);
        TextView lastMsg = convertView.findViewById(R.id.lastMessage);
        TextView time = convertView.findViewById(R.id.msgtime);

        //imageView.setImageResource(user.imageId);
        userName.setText(match.name);
        lastMsg.setText(match.lastMessage);
        time.setText(match.lastMsgTime);





        return convertView;
    }
    void getMatches(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.e("DEBUG","aaaaa");
         FirebaseFirestore.getInstance().collection("/users")
                .document(currentUser.getUid()).get()
                 .addOnCompleteListener(
                         new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot document = task.getResult();
                                List<String> group = (List<String>) document.get("matches");
                                Log.d("myTag", group.get(0));
                            }
                                                                                    });
    }
}