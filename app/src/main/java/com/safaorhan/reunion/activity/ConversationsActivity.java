package com.safaorhan.reunion.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.firestore.DocumentReference;
import com.safaorhan.reunion.R;
import com.safaorhan.reunion.adapter.ConversationAdapter;

public class ConversationsActivity extends AppCompatActivity implements ConversationAdapter.ConversationClickListener {

    private static final String TAG = ConversationsActivity.class.getSimpleName();

    RecyclerView recyclerView;
    ConversationAdapter conversationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConversationsActivity.this, UsersActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerView);

        conversationAdapter = ConversationAdapter.get();
        conversationAdapter.setConversationClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(conversationAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        conversationAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        conversationAdapter.stopListening();
    }

    @Override
    public void onConversationClick(DocumentReference conversationRef) {
        Intent intent = new Intent(ConversationsActivity.this,ChatActivity.class);
        ChatActivity.setDocumentReference(conversationRef);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.conversations, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_people:
                Intent intent = new Intent(this, UsersActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
