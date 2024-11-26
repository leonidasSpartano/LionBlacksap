package activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lionblacksap.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private EditText usermailInput, passwordInput;
    private Button loginButton,registerButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // Link UI elements
        usermailInput = findViewById(R.id.usermailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // Login button click listener
        loginButton.setOnClickListener(view -> loginUser());

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        String email = usermailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Por favor ingresa tu correo.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Por favor ingresa tu contraseña.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Authenticate the user
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Authentication successful
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid(); // Get the user's UID

                            // Start Home activity and pass the UID
                            Intent intent = new Intent(Login.this, Home.class);
                            intent.putExtra("userId", userId); // Pass UID to Home activity
                            startActivity(intent);
                            finish(); // Close Login activity
                        }
                    } else {
                        // Authentication failed
                        Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
