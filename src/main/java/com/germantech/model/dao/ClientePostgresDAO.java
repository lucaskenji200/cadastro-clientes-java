package com.germantech.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.germantech.model.Cliente;
import com.germantech.model.SQLConnection;

public class ClientePostgresDAO implements ClienteDAO {

    @Override
    public void criar(Cliente cliente) {
        String sql = "INSERT INTO clientes (nome, telefone, email, cpf, senha) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = SQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getTelefone());
            ps.setString(3, cliente.getEmail());
            ps.setString(4, cliente.getCPF());
            ps.setString(5, cliente.getSenha());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar cliente.", e);
        }
    }
    
    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY nome";
        try (Connection conn = SQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clientes.add(criarClienteDoResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar clientes.", e);
        }
        return clientes;
    }

    @Override
    public boolean atualizar(Cliente cliente) {
        String sql = "UPDATE clientes SET nome = ?, telefone = ?, email = ?, senha = ? WHERE cpf = ?";
        try (Connection conn = SQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getTelefone());
            ps.setString(3, cliente.getEmail());
            ps.setString(4, cliente.getSenha());
            ps.setString(5, cliente.getCPF());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cliente.", e);
        }
    }
    
    @Override
    public boolean remover(String cpf) {
        String sql = "DELETE FROM clientes WHERE cpf = ?";
        try (Connection conn = SQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cpf);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover cliente.", e);
        }
    }
    
    @Override
    public Optional<Cliente> buscarPorCPF(String cpf) {
        String sql = "SELECT * FROM clientes WHERE cpf = ?";
        try (Connection conn = SQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(criarClienteDoResultSet(rs));
                }
            }
        } catch (SQLException e) {
             throw new RuntimeException("Erro ao buscar por CPF.", e);
        }
        return Optional.empty();
    }
    
    @Override
    public List<Cliente> buscarPorNome(String nome) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes WHERE nome ILIKE ?";
        try (Connection conn = SQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + nome + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clientes.add(criarClienteDoResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar por nome.", e);
        }
        return clientes;
    }

    @Override
    public List<Cliente> buscarPorEmail(String email) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes WHERE email ILIKE ?";
        try (Connection conn = SQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + email + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clientes.add(criarClienteDoResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar por email.", e);
        }
        return clientes;
    }
    
    @Override
    public boolean cpfExiste(String cpf) {
        String sql = "SELECT COUNT(*) FROM clientes WHERE cpf = ?";
        try (Connection conn = SQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar CPF.", e);
        }
    }
    
    private Cliente criarClienteDoResultSet(ResultSet rs) throws SQLException {
        return new Cliente(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getString("telefone"),
            rs.getString("email"),
            rs.getString("cpf"),
            rs.getString("senha")
        );
    }
}