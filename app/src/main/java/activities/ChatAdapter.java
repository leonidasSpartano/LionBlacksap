package activities;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lionblacksap.R;

import java.util.List;

import model.Chat;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {
    private List<Chat> messages;
    private String currentUserId;

    // Constructor para inicializar los datos
    public ChatAdapter(List<Chat> messages, String currentUserId) {
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout para los mensajes
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        // Obtener el mensaje correspondiente
        Chat message = messages.get(position);
        holder.messageTextView.setText(message.getMessageText());

//        // Determinar si el mensaje fue enviado o recibido
//        if (message.getSenderId().equals(currentUserId)) {
//            // Si el mensaje fue enviado por el usuario actual, se alinea a la derecha
//            holder.messageContainer.setGravity(Gravity.END); // Alineación de los mensajes enviados a la derecha
//            holder.messageContainer.setBackgroundResource(R.drawable.message_right); // Fondo de los mensajes enviados
//        } else {
//            // Si el mensaje fue recibido, se alinea a la izquierda
//            holder.messageContainer.setGravity(Gravity.START); // Alineación de los mensajes recibidos a la izquierda
//            holder.messageContainer.setBackgroundResource(R.drawable.message_left); // Fondo de los mensajes recibidos
//        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    // Método para agregar un mensaje de manera segura
    public void addMessage(Chat newMessage) {
        // Verifica si el mensaje ya está en la lista para evitar duplicados
        for (Chat message : messages) {
            if (message.getId().equals(newMessage.getId())) {
                return;  // Si el mensaje ya está presente, no lo agregamos
            }
        }
        messages.add(newMessage);
        notifyItemInserted(messages.size() - 1);  // Notifica que se ha agregado un nuevo mensaje
    }

    // ViewHolder para cada mensaje
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public LinearLayout messageContainer;

        public MessageViewHolder(View itemView) {
            super(itemView);
            // Inicializa las vistas del mensaje
            messageTextView = itemView.findViewById(R.id.messageTextView);
            messageContainer = itemView.findViewById(R.id.messageContainer);
        }
    }
}
