package com.germantech.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection {

    private static final String URL_BANCO = "jdbc:postgresql://localhost:5432/customers";
    private static final String USER = "lucas";
    private static final String PASSWORD = "admin123";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL_BANCO, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Erro na conexão com o banco de dados. Verifique a URL, utilizador e senha.", e);
        }
    }
}