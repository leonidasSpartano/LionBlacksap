package activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lionblacksap.R;

import java.util.List;

import model.Chat;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Chat> chatMessages;  // Lista que almacena los mensajes de chat
    private String currentUserId;     // ID del usuario actual (quien está viendo el chat)

    // Constructor del adaptador que inicializa los mensajes del chat y el ID del usuario actual
    public MessageAdapter(List<Chat> chatMessages, String currentUserId) {
        this.chatMessages = chatMessages;
        this.currentUserId = currentUserId;
    }

    // Este método crea un ViewHolder para cada mensaje
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Infla el layout de cada mensaje (un item de la lista)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view); // Retorna el ViewHolder para este mensaje
    }

    // Este método se ejecuta para cada mensaje cuando se va a mostrar
    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        // Obtiene el mensaje en la posición actual
        Chat chat = chatMessages.get(position);

        // Verifica que el ID del remitente no sea nulo
        if (chat.getSenderId() != null && currentUserId != null) {
            // Si el mensaje no es del usuario actual, muestra el nombre del remitente
            if (!chat.getSenderId().equals(currentUserId)) {
                holder.senderName.setVisibility(View.VISIBLE);  // Muestra el nombre del remitente
                holder.senderName.setText(chat.getSenderName()); // Establece el nombre del remitente
            } else {
                holder.senderName.setVisibility(View.GONE);  // Oculta el nombre del remitente si es el usuario actual
            }
        } else {
            // Si el ID del remitente o del usuario actual es nulo, oculta el nombre del remitente
            holder.senderName.setVisibility(View.GONE);
        }

        // Muestra el contenido del mensaje
        holder.messageContent.setText(chat.getMessageText());
    }

    // Este método devuelve el número total de mensajes en la lista
    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    // Método para agregar un nuevo mensaje de manera segura
    public void addMessage(Chat newMessage) {
        // Verifica si el mensaje ya existe en la lista para evitar duplicados
        for (Chat chat : chatMessages) {
            if (chat.getId().equals(newMessage.getId())) {
                return;  // Si el mensaje ya existe, no lo agregamos
            }
        }

        // Agrega el nuevo mensaje a la lista de mensajes
        chatMessages.add(newMessage);
        // Notifica al adaptador que se ha agregado un nuevo mensaje, para actualizar la vista
        notifyItemInserted(chatMessages.size() - 1);
    }

    // ViewHolder es un contenedor para las vistas de un mensaje (nombre del remitente y contenido del mensaje)
    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView senderName;  // TextView para mostrar el nombre del remitente
        TextView messageContent;  // TextView para mostrar el contenido del mensaje

        public MessageViewHolder(View itemView) {
            super(itemView);
            // Inicializa las vistas del ViewHolder
            senderName = itemView.findViewById(R.id.senderName);
            messageContent = itemView.findViewById(R.id.messageContent);
        }
    }
}
