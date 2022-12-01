package com.example.ihc.ui.notifications;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolders>{
    private ArrayList<Chat> resultsChat = new ArrayList<>();

    public ChatAdapter(List<Chat> dataSetChat, ChatActivity chatActivity){

    }

    private List<Chat> getDataSetChat() { return resultsChat; }


    @NonNull
    @Override
    public ChatViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolders holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
