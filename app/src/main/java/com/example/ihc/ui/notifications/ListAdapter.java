package com.example.ihc.ui.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ihc.R;

import org.w3c.dom.Text;

import com.example.ihc.data.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<User> {

    public ListAdapter(Context context, ArrayList<User> userArrayList) {
        super(context, R.layout.items, userArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        User user = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.items, parent, false);
        }
        ImageView imageView = convertView.findViewById(R.id.img);
        TextView userName = convertView.findViewById(R.id.personName);
        TextView lastMsg = convertView.findViewById(R.id.lastMessage);
        TextView time = convertView.findViewById(R.id.msgtime);

        //imageView.setImageResource(user.getPhotoUri());
        if(user.getPhotoUri() == null) {
            imageView.setImageResource(R.drawable.ic_baseline_person_24);
        } else {
            Picasso.get().load(user.getPhotoUri()).into(imageView);
        }
        userName.setText(user.getName());
        lastMsg.setText("");
        time.setText("");

        return convertView;
    }
}