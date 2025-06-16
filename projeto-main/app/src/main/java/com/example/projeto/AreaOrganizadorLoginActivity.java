package com.example.projeto;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView; // Importação adicionada
import android.widget.Toast;    // Importação adicionada

import androidx.appcompat.app.AppCompatActivity;

public class AreaOrganizadorLoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private EditText etEmailLogin, etSenhaLogin; // Adicionados EditTexts para login
    private TextView tvTextRegister;             // Adicionado TextView para link de cadastro

    public static final String EXTRA_ORGANIZADOR_ID = "com.example.projeto.ORGANIZADOR_ID";
    public static final String EXTRA_ORGANIZADOR_NOME = "com.example.projeto.ORGANIZADOR_NOME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_organizador_login);

        etEmailLogin = findViewById(R.id.editEmail); // Assumindo que os IDs no XML são editEmail e editPassword
        etSenhaLogin = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvTextRegister = findViewById(R.id.textRegister); // ID do TextView "Cadastre-se"

        btnLogin.setOnClickListener(v -> realizarLogin());

        tvTextRegister.setOnClickListener(v -> {
            // Navega para a tela de cadastro de organizador
            Intent intent = new Intent(AreaOrganizadorLoginActivity.this, CadastroOrganizadorActivity.class);
            startActivity(intent);
        });
    }

    private void realizarLogin() {
        String email = etEmailLogin.getText().toString().trim();
        String senha = etSenhaLogin.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmailLogin.setError("E-mail é obrigatório");
            etEmailLogin.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(senha)) {
            etSenhaLogin.setError("Senha é obrigatória");
            etSenhaLogin.requestFocus();
            return;
        }

        Organizador organizadorLogado = RepositorioOrganizadores.validarLogin(email, senha);

        if (organizadorLogado != null) {
            Toast.makeText(this, "Login bem-sucedido! Bem-vindo, " + organizadorLogado.getNome(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(AreaOrganizadorLoginActivity.this, AreaOrganizadorHomeActivity.class);
            // Passar dados do organizador para a próxima tela
            intent.putExtra(EXTRA_ORGANIZADOR_ID, organizadorLogado.getId());
            intent.putExtra(EXTRA_ORGANIZADOR_NOME, organizadorLogado.getNome());
            startActivity(intent);
            finish(); // Fecha a tela de login após o sucesso
        } else {
            Toast.makeText(this, "E-mail ou senha inválidos.", Toast.LENGTH_LONG).show();
        }
    }
}
