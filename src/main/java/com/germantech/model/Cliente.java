package com.germantech.model;

public class Cliente {
    private int id;
    private final String nome;
    private final String telefone;
    private final String email;
    private final String cpf;
    private final String senha;

    public Cliente(String nome, String telefone, String email, String cpf, String senha) {
        this.nome = nome;
        this.telefone = telefone;
    // Construtor para criar um cliente novo (sem ID ainda)
        this.email = email;
        this.cpf = cpf;
        this.senha = senha;
    }

    public Cliente(int id, String nome, String telefone, String email, String cpf, String senha) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.cpf = cpf;
        this.senha = senha;
    }

    public int getID() { return id; }
    public String getNome() { return nome; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }
    public String getCPF() { return cpf; }
    public String getSenha() { return senha; }

}