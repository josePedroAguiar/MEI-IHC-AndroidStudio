package com.example.ihc.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ihc.R;
import com.example.ihc.data.User;
import com.example.ihc.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;

    private GroupAdapter adapter;
    private int imageid;
    private String toID;
    private User me;
    private EditText editChat;
    private String currentUserUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUserUid = FirebaseAuth.getInstance().getUid();

        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");
        getSupportActionBar().setTitle(name); //coloca o nome do outro user na action bar

        toID = extras.getString("toID");
        //imageid = extras.getInt("imageid");

        RecyclerView rv = findViewById(R.id.recycler_chat);

        Button btnChat = findViewById(R.id.send);
        editChat = findViewById(R.id.edit_chat);
        btnChat.setOnClickListener(v -> sendMessage());

        adapter = new GroupAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection("/users")
                .document(currentUserUid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    me = documentSnapshot.toObject(User.class);
                    fetchMessages();
                });
    }

    private void fetchMessages() {
        if (me != null) {
            String fromID = me.getUuid();

            FirebaseFirestore.getInstance().collection("/conversations")
                    .document(fromID)
                    .collection(toID)
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                    .addSnapshotListener((value, error) -> {
                        assert value != null;
                        List<DocumentChange> documentChanges = value.getDocumentChanges();

                        for (DocumentChange doc : documentChanges) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                Chat message = doc.getDocument().toObject(Chat.class);
                                adapter.add(new MessageItem(message));
                            }
                        }
                    });

        }
    }

    private void sendMessage() {
        String text = editChat.getText().toString();

        editChat.setText(null);

        String fromID = currentUserUid;

        Chat message = new Chat();
        message.setFromID(fromID);
        message.setToID(toID);
        message.setTimestamp(System.currentTimeMillis());
        message.setText(text);

        if (!message.getText().isEmpty()) {
            FirebaseFirestore.getInstance().collection("conversations").document(fromID).collection(toID).add(message)
                    .addOnSuccessListener(documentReference -> Log.d("Teste", documentReference.getId()))
                    .addOnFailureListener(e -> Log.e("Teste", e.getMessage(), e));

            FirebaseFirestore.getInstance().collection("conversations").document(toID).collection(fromID).add(message)
                    .addOnSuccessListener(documentReference -> Log.d("Teste", documentReference.getId()))
                    .addOnFailureListener(e -> Log.e("Teste", e.getMessage(), e));
        }
    }

    private static class MessageItem extends Item<GroupieViewHolder> {
        private final Chat message;

        public MessageItem(Chat message) {
            this.message = message;
        }


        @Override
        public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
            TextView txtMsg = viewHolder.itemView.findViewById(R.id.txt_msg);
            txtMsg.setText(message.getText());
        }

        public int getLayout() {
            return message.getFromID().equals(FirebaseAuth.getInstance().getUid())
                    ? R.layout.item_to_message
                    : R.layout.item_from_message;
        }

    }

}