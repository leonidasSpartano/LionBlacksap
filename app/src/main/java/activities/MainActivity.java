package activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lionblacksap.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Utilizar un Handler para retrasar la acción
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Iniciar la siguiente actividad después de 3 segundos
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                // Finalizar la MainActivity para evitar que se regrese a ella al presionar el botón de atrás
                finish();
            }
        }, 1500); // 3000 milisegundos = 3 segundos
    }
}
