package com.example.projeto;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CadastroOrganizadorActivity extends AppCompatActivity {

    private EditText etNome, etCpfCnpj, etTelefone, etEmail, etSenha, etConfirmarSenha;
    private Button btnCadastrar;
    private TextView tvJaTemConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_organizador);

        etNome = findViewById(R.id.etNomeOrganizador);
        etCpfCnpj = findViewById(R.id.etCpfCnpj);
        etTelefone = findViewById(R.id.etTelefoneOrganizador);
        etEmail = findViewById(R.id.etEmailOrganizador);
        etSenha = findViewById(R.id.etSenhaOrganizador);
        etConfirmarSenha = findViewById(R.id.etConfirmarSenhaOrganizador);
        btnCadastrar = findViewById(R.id.btnCadastrarOrganizador);
        tvJaTemConta = findViewById(R.id.tvJaTemConta);

        btnCadastrar.setOnClickListener(v -> cadastrarNovoOrganizador());

        tvJaTemConta.setOnClickListener(v -> {
            // Volta para a tela de login
            finish(); // Fecha a activity de cadastro
            // Ou pode iniciar a LoginActivity explicitamente se houver complexidade
            // Intent intent = new Intent(CadastroOrganizadorActivity.this, AreaOrganizadorLoginActivity.class);
            // startActivity(intent);
        });
    }

    private void cadastrarNovoOrganizador() {
        String nome = etNome.getText().toString().trim();
        String cpfouCnpj = etCpfCnpj.getText().toString().trim();
        String telefone = etTelefone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();
        String confirmarSenha = etConfirmarSenha.getText().toString().trim();

        // Validações básicas
        if (TextUtils.isEmpty(nome)) {
            etNome.setError("Nome é obrigatório");
            etNome.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(telefone)) {
            etTelefone.setError("Telefone é obrigatório");
            etTelefone.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("E-mail é obrigatório");
            etEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Insira um e-mail válido");
            etEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(senha)) {
            etSenha.setError("Senha é obrigatória");
            etSenha.requestFocus();
            return;
        }
        if (senha.length() < 6) { // Exemplo de validação de tamanho mínimo da senha
            etSenha.setError("A senha deve ter pelo menos 6 caracteres");
            etSenha.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(confirmarSenha)) {
            etConfirmarSenha.setError("Confirme a senha");
            etConfirmarSenha.requestFocus();
            return;
        }
        if (!senha.equals(confirmarSenha)) {
            etConfirmarSenha.setError("As senhas não coincidem");
            etConfirmarSenha.requestFocus();
            return;
        }

        // Tenta adicionar o organizador ao repositório
        boolean sucesso = RepositorioOrganizadores.adicionarOrganizador(nome, cpfouCnpj, telefone, email, senha);

        if (sucesso) {
            Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();
            // Opcional: Fazer login automaticamente e ir para a home do organizador
            // Ou apenas finalizar e voltar para a tela de login para que o usuário faça login
            finish(); // Volta para a tela de login
        } else {
            etEmail.setError("Este e-mail já está cadastrado.");
            etEmail.requestFocus();
            Toast.makeText(this, "E-mail já cadastrado. Tente outro.", Toast.LENGTH_LONG).show();
        }
    }
}
