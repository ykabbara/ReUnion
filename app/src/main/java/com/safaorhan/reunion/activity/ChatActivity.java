package com.safaorhan.reunion.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.firebase.firestore.DocumentReference;
import com.safaorhan.reunion.FirestoreHelper;
import com.safaorhan.reunion.R;
import com.safaorhan.reunion.adapter.ChatAdapter;

public class ChatActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    static DocumentReference mDocumentReference;
    EditText sendEditText;
    ImageView sendButton;
    ChatAdapter chatAdapter;

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
        setAdapter();


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirestoreHelper.sendMessage(sendEditText.getText().toString(), mDocumentReference);
                sendEditText.setText("");
            }
        });



    }
    private void setAdapter(){
        chatAdapter = ChatAdapter.get(mDocumentReference);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        chatAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        chatAdapter.stopListening();
    }
}
