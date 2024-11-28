package activities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lionblacksap.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import model.Chat;
import model.User;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {

    private List<Chat> chatList;
    private Context context;

    public ChatListAdapter(List<Chat> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);

        // Obtener el ID del destinatario (ya sea senderId o recipientId)
        String recipientId = chat.getRecipientId();
        if (recipientId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            recipientId = chat.getSenderId();
        }

        // Obtener el nombre del usuario basado en el ID con ValueEventListener
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(recipientId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {  // Cambiado a ListenerForSingleValueEvent
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        holder.userNameTextView.setText(user.getName()); // Mostrar el nombre del usuario
                    }
                } else {
                    holder.userNameTextView.setText("Usuario no encontrado");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Error al obtener nombre del usuario", Toast.LENGTH_SHORT).show();
            }
        });

        // Mostrar el último mensaje en la vista
        holder.lastMessageTextView.setText(chat.getMessageText());

        holder.itemView.setOnClickListener(v -> {
            // Al hacer clic en la tarjeta, navega al ChatActivity con el chatId
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("chatId", chat.getSenderId());  // Pasamos el ID del chat para cargarlo en la ChatActivity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView, lastMessageTextView;

        public ChatViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.senderNameTextView);
            lastMessageTextView = itemView.findViewById(R.id.messageTextView);
        }
    }
}
