package br.com.tattoomanager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import br.com.tattoomanager.data.AppDatabase;
import br.com.tattoomanager.data.Cliente;
import br.com.tattoomanager.data.ClienteDao;

public class ClientesActivity extends AppCompatActivity {

    private ClienteAdapter clientAdapter;
    private View emptyState;
    private MaterialButton filterAll;
    private MaterialButton filterActive;
    private MaterialButton filterInactive;
    private ClienteDao clientDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clientes);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
            );
            return insets;
        });

        clientDao = AppDatabase.getInstance(this).clienteDao();
        emptyState = findViewById(R.id.emptyClientsState);

        configureClientList();
        configureActions();
        configureSearch();
        configureFilters();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadClients();
    }

    private void configureClientList() {
        RecyclerView clientList = findViewById(R.id.recyclerClients);
        clientList.setLayoutManager(new LinearLayoutManager(this));

        clientAdapter = new ClienteAdapter(visibleCount -> {
            emptyState.setVisibility(
                    visibleCount == 0 ? View.VISIBLE : View.GONE
            );
        });

        clientList.setAdapter(clientAdapter);
    }

    private void configureActions() {
        findViewById(R.id.buttonBack).setOnClickListener(view -> finish());

        findViewById(R.id.buttonNewClient).setOnClickListener(view -> {
            Intent intent = new Intent(
                    ClientesActivity.this,
                    CadastroClienteActivity.class
            );
            startActivity(intent);
        });
    }

    private void configureSearch() {
        EditText searchInput = findViewById(R.id.editSearchClient);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence text,
                    int start,
                    int count,
                    int after
            ) {
                // Nenhuma ação necessária.
            }

            @Override
            public void onTextChanged(
                    CharSequence text,
                    int start,
                    int before,
                    int count
            ) {
                clientAdapter.setSearchQuery(text.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Nenhuma ação necessária.
            }
        });
    }

    private void configureFilters() {
        filterAll = findViewById(R.id.buttonFilterAll);
        filterActive = findViewById(R.id.buttonFilterActive);
        filterInactive = findViewById(R.id.buttonFilterInactive);

        filterAll.setOnClickListener(view -> {
            clientAdapter.setActiveFilter(null);
            selectFilter(filterAll);
        });

        filterActive.setOnClickListener(view -> {
            clientAdapter.setActiveFilter(true);
            selectFilter(filterActive);
        });

        filterInactive.setOnClickListener(view -> {
            clientAdapter.setActiveFilter(false);
            selectFilter(filterInactive);
        });

        selectFilter(filterAll);
    }

    private void selectFilter(MaterialButton selectedButton) {
        int selectedColor = ContextCompat.getColor(this, R.color.brick_red);
        int defaultColor = ContextCompat.getColor(this, R.color.deep_teal);

        filterAll.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
        filterActive.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
        filterInactive.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
        selectedButton.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
    }

    private void loadClients() {
        AppDatabase.databaseExecutor.execute(() -> {
            List<Cliente> clients = clientDao.listarTodos();
            runOnUiThread(() -> clientAdapter.submitList(clients));
        });
    }
}
