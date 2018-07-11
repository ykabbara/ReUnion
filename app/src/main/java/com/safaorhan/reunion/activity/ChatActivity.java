package com.safaorhan.reunion.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.safaorhan.reunion.FirestoreHelper;
import com.safaorhan.reunion.R;
import com.safaorhan.reunion.adapter.ChatHolder;
import com.safaorhan.reunion.model.Message;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = ChatActivity.class.getSimpleName();
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter adapter;
    static DocumentReference mDocumentReference;
    EditText sendEditText;
    ImageView sendButton;
    Query query = FirebaseFirestore.getInstance()
            .collection("messages")
            .orderBy("timestamp")
            .limit(50);

    public static void setDocumentReference(DocumentReference documentReference) {
        mDocumentReference = documentReference;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerView = findViewById(R.id.recyclerView);
        sendEditText = findViewById(R.id.send_edit_text);
        sendButton = findViewById(R.id.send_button);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirestoreHelper.sendMessage(sendEditText.getText().toString(), mDocumentReference);
                sendEditText.setText("");
            }
        });

        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class).build();

        adapter = new FirestoreRecyclerAdapter<Message, ChatHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatHolder holder, int position, @NonNull Message model) {

            }

            @NonNull
            @Override
            public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
                return new ChatHolder(view);
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
