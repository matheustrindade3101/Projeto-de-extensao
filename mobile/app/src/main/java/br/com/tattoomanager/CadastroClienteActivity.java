
package br.com.tattoomanager;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CadastroClienteActivity extends AppCompatActivity {

    // Campos do formulário
    private EditText campoNome;
    private EditText campoTelefone;
    private EditText campoEmail;
    private EditText campoNascimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Abrir o layout do cadastro
        setContentView(R.layout.activity_cadastro_cliente);

        // Conectar os campos do XML ao Java
        campoNome = findViewById(R.id.campoNome);
        campoTelefone = findViewById(R.id.campoTelefone);
        campoEmail = findViewById(R.id.campoEmail);
        campoNascimento = findViewById(R.id.campoNascimento);

        // Conectar os botões
        Button botaoVoltar = findViewById(R.id.botaoVoltar);
        Button botaoSalvar = findViewById(R.id.botaoSalvar);

        // Botão Voltar
        botaoVoltar.setOnClickListener(v -> {
            finish();
        });

        // Botão Salvar
        botaoSalvar.setOnClickListener(v -> {
            salvarCliente();
        });
    }

    private void salvarCliente() {

        // Pegar os dados digitados
        String nome = campoNome.getText().toString().trim();
        String telefone = campoTelefone.getText().toString().trim();
        String email = campoEmail.getText().toString().trim();
        String nascimento = campoNascimento.getText().toString().trim();

        boolean valido = true;

        // Limpar erros anteriores
        campoNome.setError(null);
        campoTelefone.setError(null);
        campoEmail.setError(null);
        campoNascimento.setError(null);

        // Validar nome
        if (nome.isEmpty()) {
            campoNome.setError("Informe o nome");
            valido = false;
        }

        // Validar telefone
        if (telefone.isEmpty()) {
            campoTelefone.setError("Informe o telefone");
            valido = false;
        } else if (telefone.replaceAll("\\D", "").length() < 10) {
            campoTelefone.setError("Telefone inválido");
            valido = false;
        }

        // Validar e-mail
        if (email.isEmpty()) {
            campoEmail.setError("Informe o e-mail");
            valido = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            campoEmail.setError("E-mail inválido");
            valido = false;
        }

        // Validar data de nascimento
        if (nascimento.isEmpty()) {
            campoNascimento.setError("Informe a data de nascimento");
            valido = false;
        } else if (!dataValida(nascimento)) {
            campoNascimento.setError("Use uma data válida: DD/MM/AAAA");
            valido = false;
        }

        // Se todos os campos estiverem corretos
        if (valido) {

            // Mostrar mensagem temporária
            Toast.makeText(
                this,
                "Cliente cadastrado com sucesso!",
                Toast.LENGTH_SHORT
            ).show();

            // Voltar para a tela anterior
            finish();
        }
    }

    // Verificar se a data de nascimento é válida
    private boolean dataValida(String texto) {

        SimpleDateFormat formato = new SimpleDateFormat(
            "dd/MM/yyyy", Locale.getDefault()
        );

        formato.setLenient(false);

        ParsePosition posicao = new ParsePosition(0);
        Date data = formato.parse(texto, posicao);

        return data != null
                && posicao.getIndex() == texto.length()
                && !data.after(new Date());
    }
}
