package activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lionblacksap.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ChildEventListener;

import java.util.ArrayList;
import java.util.List;

import model.Chat;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText messageInput;
    private Button sendButton;
    private String chatId;
    private List<Chat> chatMessages;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        // Vincula los elementos del layout
        recyclerView = findViewById(R.id.recyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        chatId = getIntent().getStringExtra("chatId");
        chatMessages = new ArrayList<>();

        // Verifica si el usuario está autenticado antes de inicializar el adaptador
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            messageAdapter = new MessageAdapter(chatMessages, FirebaseAuth.getInstance().getCurrentUser().getUid());
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(messageAdapter);

            // Cargar los mensajes del chat desde Firebase
            loadMessages();
        } else {
            Toast.makeText(this, "Error: usuario no autenticado", Toast.LENGTH_SHORT).show();
        }

        // Acción al presionar el botón de enviar mensaje
        sendButton.setOnClickListener(v -> {
            String messageText = messageInput.getText().toString().trim();
            if (!messageText.isEmpty()) {
                sendMessage(messageText);
            } else {
                Toast.makeText(ChatActivity.this, "Por favor ingresa un mensaje", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMessages() {
        // Referencia a los mensajes del chat en Firebase
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chats").child(chatId).child("messages");

        // Usamos ChildEventListener para manejar eventos individuales (más eficiente en este caso)
        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                // Cada vez que se añade un mensaje, se obtiene el mensaje y se añade a la lista
                Chat chatMessage = dataSnapshot.getValue(Chat.class);
                if (chatMessage != null) {
                    // Evita agregar el mensaje si ya está en la lista
                    if (!chatMessages.contains(chatMessage)) {
                        chatMessages.add(chatMessage);
                        messageAdapter.notifyItemInserted(chatMessages.size() - 1);  // Notifica que se ha agregado un nuevo mensaje
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                // Si un mensaje se actualiza, puedes manejarlo aquí (si es necesario)
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // Si un mensaje es eliminado, puedes manejarlo aquí (si es necesario)
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                // Si un mensaje se mueve, puedes manejarlo aquí (si es necesario)
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ChatActivity.this, "Error al cargar mensajes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage(String messageText) {
        String senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        long timestamp = System.currentTimeMillis();

        // Obtener el nombre del usuario actual (remitente)
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(senderId).child("name");
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                String senderName = task.getResult().getValue(String.class);
                String recipientId = chatId; // Usualmente debes asociar el recipientId en función de la conversación

                // Crear el nuevo mensaje
                Chat newMessage = new Chat(senderId, senderName, recipientId, messageText, timestamp);

                // Referencia al nodo "chats" de Firebase y agregar el mensaje
                DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chats").child(chatId).child("messages");
                String messageId = chatRef.push().getKey();
                if (messageId != null) {
                    newMessage.setId(messageId);  // Establece el ID del mensaje
                    chatRef.child(messageId).setValue(newMessage).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            messageInput.setText("");
                            messageAdapter.addMessage(newMessage);  // Agrega el mensaje al adaptador
                        } else {
                            Toast.makeText(ChatActivity.this, "Error al enviar el mensaje", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
