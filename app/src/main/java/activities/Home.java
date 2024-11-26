package activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lionblacksap.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.Chat;

public class Home extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private Button startConversationButton, logoutButton;
    private EditText userEmailInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // Vincula los elementos del layout
        toolbar = findViewById(R.id.toolbar);
        startConversationButton = findViewById(R.id.startConversationButton);
        logoutButton = findViewById(R.id.logoutButton);
        userEmailInput = findViewById(R.id.userEmailInput);

        // Obtén el UID del usuario desde el Intent
        String userId = getIntent().getStringExtra("userId");
        Log.d("Login", "UID recibido al Home: " + userId);

        if (userId != null) {
            // Referencia a Firebase Realtime Database
            DatabaseReference userRef = FirebaseDatabase.getInstance()
                    .getReference("Users").child(userId).child("name");

            // Obtén el nombre del usuario desde Firebase
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        String userName = task.getResult().getValue(String.class);
                        toolbar.setTitle("Bienvenido " + userName + " a tus mensajes");
                    } else {
                        toolbar.setTitle("Bienvenido a tus mensajes");
                    }
                } else {
                    toolbar.setTitle("Bienvenido a tus mensajes");
                    Log.e("Firebase", "Error al obtener el nombre del usuario", task.getException());
                }
            });
        } else {
            toolbar.setTitle("Bienvenido a tus mensajes");
            Toast.makeText(this, "No se encontró información del usuario.", Toast.LENGTH_SHORT).show();
            Log.e("Intent", "El UID del usuario no fue proporcionado.");
        }

        // Acción al presionar el botón "Iniciar conversación"
        startConversationButton.setOnClickListener(v -> {
            String recipientEmail = userEmailInput.getText().toString().trim();

            if (recipientEmail.isEmpty()) {
                Toast.makeText(Home.this, "Por favor ingresa un correo electrónico", Toast.LENGTH_SHORT).show();
                return;
            }

            // Obtener el ID del usuario actual (senderId)
            String senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // Crear un nuevo chat en Firebase
            long timestamp = System.currentTimeMillis();
            Chat newChat = new Chat("", recipientEmail, senderId, "", timestamp);

            // Crear un ID único para el chat
            String chatId = FirebaseDatabase.getInstance().getReference().child("chats").push().getKey();
            newChat.setId(chatId);

            // Guardar el chat en Firebase
            if (chatId != null) {
                DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chats");
                chatRef.child(chatId).setValue(newChat).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Home.this, "Conversación iniciada", Toast.LENGTH_SHORT).show();

                        // Navegar a la actividad ChatActivity con el chatId
                        Intent intent = new Intent(Home.this, ChatActivity.class);
                        intent.putExtra("chatId", chatId);  // Pasamos el ID del chat para cargarlo en la ChatActivity
                        startActivity(intent);
                    } else {
                        Toast.makeText(Home.this, "Error al iniciar la conversación", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Acción al presionar el botón de "Cerrar sesión"
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Home.this, Login.class));
            finish();  // Finaliza la actividad actual
        });
    }
}
