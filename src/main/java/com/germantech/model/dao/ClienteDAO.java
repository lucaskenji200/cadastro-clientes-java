package com.germantech.model.dao;

import java.util.List;
import java.util.Optional;

import com.germantech.model.Cliente;

public interface ClienteDAO {
    void criar(Cliente cliente);
    List<Cliente> listarTodos();
    boolean atualizar(Cliente cliente);
    boolean remover(String cpf);
    Optional<Cliente> buscarPorCPF(String cpf);
    List<Cliente> buscarPorNome(String nome);
    List<Cliente> buscarPorEmail(String email);
    boolean cpfExiste(String cpf);
}