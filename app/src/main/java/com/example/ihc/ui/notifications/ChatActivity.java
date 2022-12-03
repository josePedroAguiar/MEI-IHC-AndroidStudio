package com.example.ihc.ui.notifications;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ihc.R;
import com.example.ihc.databinding.ActivityChatBinding;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.GroupieViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;

    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //String name = (String) savedInstanceState.getSerializable("name");
        //String name = getIntent().getExtras().getParcelable("name");
        String name = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(name);

        RecyclerView rv = findViewById(R.id.recycler_chat);

        adapter = new GroupAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        adapter.add(new MessageItem(true));
        adapter.add(new MessageItem(true));
        adapter.add(new MessageItem(false));
        adapter.add(new MessageItem(false));
        adapter.add(new MessageItem(true));
        adapter.add(new MessageItem(false));

    }

    private class MessageItem extends Item<GroupieViewHolder> {

        private final boolean isLeft;

        public MessageItem(boolean isLeft) {
            this.isLeft = isLeft;
        }


        @Override
        public void bind(@NonNull GroupieViewHolder viewHolder, int position) {

        }

        public int getLayout() {
            return isLeft ? R.layout.item_to_message : R.layout.item_from_message;
        }

    }

}