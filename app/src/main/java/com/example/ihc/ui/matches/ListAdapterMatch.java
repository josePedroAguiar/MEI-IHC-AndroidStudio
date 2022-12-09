package com.example.ihc.ui.matches;

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListAdapterMatch extends ArrayAdapter<Match> {
    public ListAdapterMatch(Context context, ArrayList<Match> userArrayList) {
        super(context, R.layout.items_match, userArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Match match = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.items_match, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.image);
        imageView.setImageResource(R.drawable.ic_baseline_person_24);
        ImageView imageView2 = convertView.findViewById(R.id.map);
        TextView userName = convertView.findViewById(R.id.personName);

        TextView bio = convertView.findViewById(R.id.lastMessage);
        TextView time = convertView.findViewById(R.id.msgtime);

        if (match.imageId != null)
            Picasso.get().load(match.imageId).into(imageView);
        if (match.mapId != null)
            Picasso.get().load(match.mapId).into(imageView2);
        userName.setText(match.name);
        time.setText(match.age);
        bio.setText(match.country);

        return convertView;
    }

}