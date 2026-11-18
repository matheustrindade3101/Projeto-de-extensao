package br.com.tattoomanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.com.tattoomanager.data.AppDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.cardClients).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ClientesActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        AppDatabase.databaseExecutor.execute(() -> {
            int totalClients = AppDatabase.getInstance(this)
                    .clienteDao()
                    .contarTodos();

            runOnUiThread(() -> {
                TextView clientCount = findViewById(
                        R.id.textRegisteredClientsCount
                );
                clientCount.setText(String.valueOf(totalClients));
            });
        });
    }
}
