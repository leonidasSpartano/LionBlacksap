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

        // Obtén el UID del usuario actual desde Firebase Realtime Database (por correo electrónico)
        String userId = getIntent().getStringExtra("userId");
        Log.d("Login", "UID recibido al Home: " + userId);

        if (userId != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance()
                    .getReference("Users").child(userId).child("name");

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

        startConversationButton.setOnClickListener(v -> {
            String recipientEmail = userEmailInput.getText().toString().trim();
            Log.d("FirebaseDebug", "Correo ingresado para buscar: " + recipientEmail);

            if (recipientEmail.isEmpty()) {
                Toast.makeText(Home.this, "Por favor ingresa un correo electrónico", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
            usersRef.orderByChild("mail").equalTo(recipientEmail).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        // Obtener el ID del remitente desde Realtime Database
                        String senderId = task.getResult().getChildren().iterator().next().getKey();
                        String senderName = task.getResult().getChildren().iterator().next().child("name").getValue(String.class);

                        Log.d("FirebaseDebug", "Sender ID: " + senderId);
                        Log.d("FirebaseDebug", "Sender Name: " + senderName);

                        if (senderId != null && senderName != null) {
                            // Obtener el receptor usando el correo ingresado
                            usersRef.orderByChild("mail").equalTo(recipientEmail).get().addOnCompleteListener(userTask -> {
                                if (userTask.isSuccessful() && userTask.getResult().exists()) {
                                    String recipientId = userTask.getResult().getChildren().iterator().next().getKey();
                                    Log.d("FirebaseDebug", "Recipient ID encontrado: " + recipientId);

                                    if (recipientId != null) {
                                        long timestamp = System.currentTimeMillis();
                                        Chat newChat = new Chat(senderId, senderName, recipientId, recipientEmail, "", timestamp);

                                        String chatId = FirebaseDatabase.getInstance().getReference().child("chats").push().getKey();
                                        newChat.setId(chatId);

                                        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chats");
                                        chatRef.child(chatId).setValue(newChat).addOnCompleteListener(chatTask -> {
                                            if (chatTask.isSuccessful()) {
                                                Toast.makeText(Home.this, "Conversación iniciada", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(Home.this, ChatActivity.class);
                                                intent.putExtra("chatId", chatId);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(Home.this, "Error al iniciar la conversación", Toast.LENGTH_SHORT).show();
                                                Log.e("FirebaseDebug", "Error al guardar el chat en Firebase", chatTask.getException());
                                            }
                                        });
                                    }
                                } else {
                                    Toast.makeText(Home.this, "No se encontró al usuario con el correo ingresado.", Toast.LENGTH_SHORT).show();
                                    Log.e("FirebaseDebug", "El correo ingresado no corresponde a ningún usuario.");
                                }
                            });
                        } else {
                            Toast.makeText(Home.this, "Error al obtener los datos del remitente", Toast.LENGTH_SHORT).show();
                            Log.e("FirebaseDebug", "El ID o nombre del remitente no se pudo obtener.");
                        }
                    } else {
                        Toast.makeText(Home.this, "No se encontró al usuario con el correo ingresado.", Toast.LENGTH_SHORT).show();
                        Log.e("FirebaseDebug", "El correo ingresado no corresponde a ningún usuario.");
                    }
                } else {
                    Toast.makeText(Home.this, "Error al buscar usuarios en Firebase", Toast.LENGTH_SHORT).show();
                    Log.e("FirebaseDebug", "Error al buscar usuarios en Firebase", task.getException());
                }
            });
        });

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Home.this, Login.class));
            finish();
        });
    }
}
