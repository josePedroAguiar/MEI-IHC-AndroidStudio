package com.example.ihc.ui.notifications;

import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

public class ChatViewHolders extends RecyclerView.ViewHolder implements DialogInterface.OnClickListener {


    public ChatViewHolders(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}