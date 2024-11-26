package activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lionblacksap.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private EditText registerUsernameInput, registerEmailInput, registerPasswordInput, registerConfirmPasswordInput;
    private Button registerButton, backButton;

    // Firebase instances
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // Initialize Firebase instances
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize UI elements
        registerUsernameInput = findViewById(R.id.registerUsernameInput);
        registerEmailInput = findViewById(R.id.registerEmailInput);
        registerPasswordInput = findViewById(R.id.registerPasswordInput);
        registerConfirmPasswordInput = findViewById(R.id.registerConfirmPasswordInput);
        registerButton = findViewById(R.id.registerButton);
        backButton = findViewById(R.id.BackButton);

        // Register button logic
        registerButton.setOnClickListener(view -> registerUser());

        // Back button logic
        backButton.setOnClickListener(view -> finish());
    }

    // Method to validate email format with custom regex
    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailPattern);
    }

    private void registerUser() {
        String username = registerUsernameInput.getText().toString().trim();
        String email = registerEmailInput.getText().toString().trim();
        String password = registerPasswordInput.getText().toString().trim();
        String confirmPassword = registerConfirmPasswordInput.getText().toString().trim();

        // Input validations
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Por favor ingresa un nombre de usuario.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Por favor ingresa un correo electrónico.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Por favor ingresa un correo electrónico válido.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Por favor ingresa una contraseña.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Register the user in Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Firebase Authentication completed successfully
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();

                            // Save additional user data in Realtime Database
                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("id", userId);
                            userMap.put("name", username);
                            userMap.put("mail", email);

                            databaseReference.child(userId).setValue(userMap)
                                    .addOnCompleteListener(dbTask -> {
                                        if (dbTask.isSuccessful()) {
                                            Toast.makeText(Register.this, "Usuario registrado exitosamente.", Toast.LENGTH_SHORT).show();
                                            // Redirect to main activity or login
                                            startActivity(new Intent(Register.this, MainActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(Register.this, "Error al guardar los datos en Realtime Database.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        // Error in Firebase Authentication
                        Toast.makeText(Register.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
