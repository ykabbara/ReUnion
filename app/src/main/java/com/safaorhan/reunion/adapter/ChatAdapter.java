package com.safaorhan.reunion.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.safaorhan.reunion.R;
import com.safaorhan.reunion.model.Message;
import com.safaorhan.reunion.model.User;

public class ChatAdapter extends FirestoreRecyclerAdapter<Message, ChatAdapter.ChatHolder> {

    private ChatAdapter(@NonNull FirestoreRecyclerOptions<Message> options) {
        super(options);
    }

    public static ChatAdapter get(DocumentReference conversationRef) {

        Query query = FirebaseFirestore.getInstance()
                .collection("messages")
                .whereEqualTo("conversation", conversationRef)
                .orderBy("sentAt")
                .limit(50);

        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();

        return new ChatAdapter(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatHolder holder, int position, @NonNull Message message) {
        holder.bind(message);
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatHolder(itemView);
    }

    class ChatHolder extends RecyclerView.ViewHolder {

        View itemView;
        TextView nameText;
        TextView messageText;

        ChatHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            nameText = itemView.findViewById(R.id.name_text);
            messageText = itemView.findViewById(R.id.message_text);
        }

        void bind(final Message message) {

            message.getFrom().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User user = documentSnapshot.toObject(User.class);
                    String ourMail = "team6@nomail.me";
                    if (user != null) {
                        if (ourMail.equals(user.getEmail())) {
                            nameText.setText(R.string.you);
                        } else {
                            nameText.setText(user.getName());
                        }
                    }
                }
            });

            messageText.setText(message.getText());
        }
    }
}
