package br.com.tattoomanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class ClientesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clientes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.buttonBack).setOnClickListener(v -> finish());
        findViewById(R.id.buttonNewClient).setOnClickListener(v -> {
            Intent intent = new Intent(
                    ClientesActivity.this,
                    CadastroClienteActivity.class
            );
            startActivity(intent);
        });

        EditText editSearchClient = findViewById(R.id.editSearchClient);

        editSearchClient.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                CharSequence text,
                int start,
                int count,
                int after
            ) {
                // Nenhuma ação necessária antes da alteração.
            }

            @Override
            public void onTextChanged(
                CharSequence text,
                int start,
                int before,
                int count
            ) {
                filterClients(text.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Nenhuma ação necessária depois da alteração.
            }
        });
    }

    private void filterClients(String query) {
        String searchTerm = query
                .trim()
                .toLowerCase(Locale.ROOT);

        updateClientVisibility(
                R.id.cardClientLucas,
                getString(R.string.client_lucas_name),
                getString(R.string.client_lucas_phone),
                searchTerm
        );

        updateClientVisibility(
                R.id.cardClientMariana,
                getString(R.string.client_mariana_name),
                getString(R.string.client_mariana_phone),
                searchTerm
        );

        updateClientVisibility(
                R.id.cardClientCarlos,
                getString(R.string.client_carlos_name),
                getString(R.string.client_carlos_phone),
                searchTerm
        );
    }

    private void updateClientVisibility(
            int cardId,
            String clientName,
            String clientPhone,
            String searchTerm
    ) {
        View clientCard = findViewById(cardId);

        String searchableContent = (
                clientName + " " + clientPhone
        ).toLowerCase(Locale.ROOT);

        boolean matchesSearch = searchTerm.isEmpty()
                || searchableContent.contains(searchTerm);

        clientCard.setVisibility(
                matchesSearch ? View.VISIBLE : View.GONE
        );
    }
}
