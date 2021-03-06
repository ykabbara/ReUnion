package com.safaorhan.reunion.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.safaorhan.reunion.FirestoreHelper;
import com.safaorhan.reunion.R;
import com.safaorhan.reunion.model.Conversation;
import com.safaorhan.reunion.model.Message;
import com.safaorhan.reunion.model.User;

public class ConversationAdapter extends FirestoreRecyclerAdapter<Conversation, ConversationAdapter.ConversationHolder> {
    private static final String TAG = ConversationAdapter.class.getSimpleName();
    ConversationClickListener conversationClickListener;
    User opponent = null;
    Context context;


    public ConversationAdapter(@NonNull FirestoreRecyclerOptions<Conversation> options) {
        super(options);
    }

    public ConversationClickListener getConversationClickListener() {
        if (conversationClickListener == null) {
            conversationClickListener = new ConversationClickListener() {
                @Override
                public void onConversationClick(DocumentReference documentReference) {
                    Log.e(TAG, "You need to call setConversationClickListener() to set the click listener of ConversationAdapter");
                }
            };
        }

        return conversationClickListener;
    }

    public void setConversationClickListener(ConversationClickListener conversationClickListener) {
        this.conversationClickListener = conversationClickListener;
    }

    public static ConversationAdapter get() {
        Query query = FirebaseFirestore.getInstance()
                .collection("conversations")
                .whereEqualTo(FirestoreHelper.getMe().getId(),true)
                .limit(50);

        FirestoreRecyclerOptions<Conversation> options = new FirestoreRecyclerOptions.Builder<Conversation>()
                .setQuery(query, Conversation.class)
                .build();

        return new ConversationAdapter(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ConversationHolder holder, int position, @NonNull Conversation conversation) {
        conversation.setId(getSnapshots().getSnapshot(position).getId());
        context = holder.itemView.getContext();
        holder.bind(conversation);
    }

    @NonNull
    @Override
    public ConversationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation, parent, false);
        return new ConversationHolder(itemView);
    }

    public class ConversationHolder extends RecyclerView.ViewHolder {

        View itemView;
        TextView opponentNameText;
        TextView lastMessageText;
        TextView opponentIconText;

        private ConversationHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            opponentNameText = itemView.findViewById(R.id.opponentNameText);
            lastMessageText = itemView.findViewById(R.id.lastMessageText);
            opponentIconText = itemView.findViewById(R.id.opponentIconText);
        }

        private void bind(final Conversation conversation) {

            itemView.setVisibility(View.INVISIBLE);
            final GradientDrawable circle = (GradientDrawable) opponentIconText.getBackground();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getConversationClickListener().onConversationClick(FirestoreHelper.getConversationRef(conversation));
                }
            });

            conversation.getOpponent().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    opponent = documentSnapshot.toObject(User.class);
                    if (opponent != null) {
                        opponentNameText.setText(opponent.getName());

                    circle.setColor(context.getResources().getColor(opponent.getColorId()));
                    String firstLetter = opponent.getName().charAt(0) + "";
                    opponentIconText.setText(firstLetter);
                    }
                    itemView.setVisibility(View.VISIBLE);
                }
            });


            if(conversation.getLastMessage() != null) {
                conversation.getLastMessage().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Message lastMessage = documentSnapshot.toObject(Message.class);
                        if (lastMessage != null) {
                            lastMessageText.setText(lastMessage.getText());
                        }
                    }
                });
            } else {
                lastMessageText.setText(R.string.start_a_conversation);
            }

        }
    }

    public interface ConversationClickListener {
        void onConversationClick(DocumentReference conversationRef);
    }
}
