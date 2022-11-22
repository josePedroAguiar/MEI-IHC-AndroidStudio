package com.example.ihc.ui.matches;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ihc.R;

import java.util.ArrayList;

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
        TextView lastMsg = convertView.findViewById(R.id.lastMessage);
        TextView time = convertView.findViewById(R.id.msgtime);

        //imageView.setImageResource(user.imageId);
        userName.setText(match.name);
        lastMsg.setText(match.lastMessage);
        time.setText(match.lastMsgTime);





        return convertView;
    }
}