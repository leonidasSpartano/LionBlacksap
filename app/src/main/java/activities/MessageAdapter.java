package activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lionblacksap.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import model.Chat;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Chat> messages;
    private String currentUserId;

    public MessageAdapter(List<Chat> messages, String currentUserId) {
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el layout item_message.xml
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Chat message = messages.get(position);
        holder.messageTextView.setText(message.getText());

        // Determinar si es un mensaje enviado o recibido
        if (message.getSenderId().equals(currentUserId)) {
            // Si el mensaje fue enviado por el usuario actual, aplica el fondo de la derecha
            holder.messageContainer.setBackgroundResource(R.drawable.message_right);
        } else {
            // Si el mensaje fue recibido, aplica el fondo de la izquierda
            holder.messageContainer.setBackgroundResource(R.drawable.message_left);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    // ViewHolder
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public LinearLayout messageContainer;

        public MessageViewHolder(View itemView) {
            super(itemView);
            // Encuentra las vistas por su ID
            messageTextView = itemView.findViewById(R.id.messageTextView);
            messageContainer = itemView.findViewById(R.id.messageContainer);
        }
    }
}
