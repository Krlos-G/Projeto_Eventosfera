package com.example.projeto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RepositorioOrganizadores {

    private static final List<Organizador> listaOrganizadores = Collections.synchronizedList(new ArrayList<>());
    private static final Random random = new Random();

    // Adiciona um novo organizador à lista
    public static boolean adicionarOrganizador(String nome, String cpfouCpnj, String telefone, String email, String senha) {
        // Verifica se o email já existe para evitar duplicados
        for (Organizador org : listaOrganizadores) {
            if (org.getEmail().equalsIgnoreCase(email)) {
                return false; // Email já cadastrado
            }
        }
        // Gera um ID simples (em um sistema real, seria um ID de banco de dados)
        int id = random.nextInt(100000) + 1;
        Organizador novoOrganizador = new Organizador(id, nome, cpfouCpnj, email, senha, telefone); // CPF/CNPJ vazio por enquanto
        listaOrganizadores.add(novoOrganizador);
        return true;
    }

    // Verifica as credenciais de login
    public static Organizador validarLogin(String email, String senha) {
        for (Organizador org : listaOrganizadores) {
            if (org.getEmail().equalsIgnoreCase(email) && org.getSenha().equals(senha)) {
                return org; // Retorna o objeto Organizador se o login for válido
            }
        }
        return null; // Login inválido
    }

    // Método para obter um organizador por ID (pode ser útil)
    public static Organizador getOrganizadorPorId(int id) {
        for (Organizador org : listaOrganizadores) {
            if (org.getId() == id) {
                return org;
            }
        }
        return null;
    }

    // Apenas para depuração ou testes, para ver os organizadores cadastrados
    public static List<Organizador> getListaOrganizadores() {
        return listaOrganizadores;
    }
}

