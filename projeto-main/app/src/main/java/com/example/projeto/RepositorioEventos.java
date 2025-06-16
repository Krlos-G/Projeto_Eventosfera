package com.example.projeto;

import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RepositorioEventos {

    private static final List<Evento> listaEventosCompartilhada = Collections.synchronizedList(new ArrayList<>());

    public static List<Evento> getListaEventos() {
        return listaEventosCompartilhada;
    }

    public static void adicionarEvento(Evento evento) {
        if (evento != null) {
            listaEventosCompartilhada.add(0, evento);
        }
    }

    public static void atualizarEvento(Evento eventoAtualizado) {
        if (eventoAtualizado == null) return;
        for (int i = 0; i < listaEventosCompartilhada.size(); i++) {
            Evento eventoExistente = listaEventosCompartilhada.get(i);
            if (eventoExistente.getId() == eventoAtualizado.getId()) {
                listaEventosCompartilhada.set(i, eventoAtualizado);
                Log.d("RepositorioEventos", "Evento com ID " + eventoAtualizado.getId() + " atualizado.");
                return;
            }
        }
    }

    public static void removerEvento(Evento eventoParaRemover) {
        if (eventoParaRemover == null) return;
        boolean removido = listaEventosCompartilhada.removeIf(evento -> evento.getId() == eventoParaRemover.getId());
        if (removido) {
            Log.d("RepositorioEventos", "Evento com ID " + eventoParaRemover.getId() + " removido.");
        } else {
            Log.w("RepositorioEventos", "Tentativa de remover evento não existente: ID " + eventoParaRemover.getId());
        }
    }

    public static Evento getEventoPorId(int idEvento) {
        for (Evento evento : listaEventosCompartilhada) {
            if (evento.getId() == idEvento) {
                return evento;
            }
        }
        return null;
    }
}
