package com.example.projeto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class AreaOrganizadorHomeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CRIAR_EVENTO_ORGANIZADOR = 2;
    public static final int REQUEST_CODE_EDITAR_EVENTO = 3;
    private static final String TAG = "AreaOrganizadorHome";

    private ListView lvEvents;
    private Button btn_criar_evento;
    private TextView tvNomeOrganizadorLogado;

    private List<Evento> listaDeEventosOrganizador;
    private EventoAdapter adapterOrganizador;

    private int idOrganizadorLogado;
    private String nomeOrganizadorLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_organizador_home);

        Intent intentRecebido = getIntent();
        idOrganizadorLogado = intentRecebido.getIntExtra(AreaOrganizadorLoginActivity.EXTRA_ORGANIZADOR_ID, -1);
        nomeOrganizadorLogado = intentRecebido.getStringExtra(AreaOrganizadorLoginActivity.EXTRA_ORGANIZADOR_NOME);

        tvNomeOrganizadorLogado = findViewById(R.id.tvUserName);
        if (nomeOrganizadorLogado != null && !nomeOrganizadorLogado.isEmpty()) {
            tvNomeOrganizadorLogado.setText(nomeOrganizadorLogado);
        } else {
            tvNomeOrganizadorLogado.setText("Organizador");
        }

        lvEvents = findViewById(R.id.lvEvents);
        btn_criar_evento = findViewById(R.id.btnNovoEvento);

        // Carrega apenas os eventos do organizador logado
        carregarEventosOrganizador();

        btn_criar_evento.setOnClickListener(v -> {
            Intent intent = new Intent(AreaOrganizadorHomeActivity.this, CriarEventoActivity.class);
            intent.putExtra("ID_ORGANIZADOR_LOGADO", idOrganizadorLogado);
            startActivityForResult(intent, REQUEST_CODE_CRIAR_EVENTO_ORGANIZADOR);
        });
    }

    private void carregarEventosOrganizador() {
        // Obtém todos os eventos do repositório
        List<Evento> todosEventos = RepositorioEventos.getListaEventos();

        // Filtra apenas os eventos do organizador logado
        listaDeEventosOrganizador = new ArrayList<>();
        for (Evento evento : todosEventos) {
            if (evento.getIdOrganizador() == idOrganizadorLogado) {
                listaDeEventosOrganizador.add(evento);
            }
        }

        // Configura o adapter com a lista filtrada
        adapterOrganizador = new EventoAdapter(this, R.layout.item_event, listaDeEventosOrganizador, true);
        lvEvents.setAdapter(adapterOrganizador);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_CRIAR_EVENTO_ORGANIZADOR) {
                Evento novoEvento = (Evento) data.getSerializableExtra(CriarEventoActivity.EXTRA_NOVO_EVENTO);
                if (novoEvento != null) {
                    RepositorioEventos.adicionarEvento(novoEvento);
                    Toast.makeText(this, "Evento '" + novoEvento.getNome() + "' adicionado!", Toast.LENGTH_LONG).show();
                    // Recarrega a lista após adicionar um novo evento
                    carregarEventosOrganizador();
                }
            } else if (requestCode == REQUEST_CODE_EDITAR_EVENTO) {
                Evento eventoAtualizado = (Evento) data.getSerializableExtra(CriarEventoActivity.EXTRA_NOVO_EVENTO);
                if (eventoAtualizado != null) {
                    RepositorioEventos.atualizarEvento(eventoAtualizado);
                    Toast.makeText(this, "Evento '" + eventoAtualizado.getNome() + "' atualizado!", Toast.LENGTH_LONG).show();
                    // Recarrega a lista após editar um evento
                    carregarEventosOrganizador();
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.d(TAG, "Criação/Edição de evento cancelada.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarrega os eventos sempre que a activity retornar ao primeiro plano
        carregarEventosOrganizador();
    }
}