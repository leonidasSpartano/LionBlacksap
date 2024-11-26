package activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lionblacksap.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import model.Chat;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView messagesRecyclerView;
    private EditText messageInput;
    private Button sendButton;
    private String chatId;
    private ArrayList<Chat> chatMessages;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        // Obtener el chatId desde el Intent
        chatId = getIntent().getStringExtra("chatId");

        // Vincula los elementos de la vista
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        // Configurar RecyclerView
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, chatMessages);
        messagesRecyclerView.setAdapter(chatAdapter);

        // Obtener mensajes de Firebase
        loadMessages();

        // Enviar mensaje
        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void loadMessages() {
        // Obtener mensajes del chat desde Firebase
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chats").child(chatId).child("messages");
        chatRef.orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                chatMessages.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat message = dataSnapshot.getValue(Chat.class);
                    if (message != null) {
                        chatMessages.add(message);
                    }
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ChatActivity.this, "Error al cargar los mensajes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        if (messageText.isEmpty()) {
            Toast.makeText(ChatActivity.this, "Escribe un mensaje", Toast.LENGTH_SHORT).show();
            return;
        }

        String senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        long timestamp = System.currentTimeMillis();
        Chat newMessage = new Chat("", "",messageText, senderId, timestamp);

        // Guardar el mensaje en Firebase
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chats").child(chatId).child("messages");
        String messageId = chatRef.push().getKey();
        if (messageId != null) {
            newMessage.setId(messageId);
            chatRef.child(messageId).setValue(newMessage).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    messageInput.setText("");
                    loadMessages();  // Recargar los mensajes
                } else {
                    Toast.makeText(ChatActivity.this, "Error al enviar el mensaje", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
