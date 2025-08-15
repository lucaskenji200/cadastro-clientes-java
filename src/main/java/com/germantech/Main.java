package com.germantech;

import javax.swing.SwingUtilities;

import com.germantech.controller.ClienteController;
import com.germantech.model.dao.ClienteDAO;
import com.germantech.model.dao.ClientePostgresDAO;
import com.germantech.view.MainWindow;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow view = new MainWindow();
            ClienteDAO clienteDAO = new ClientePostgresDAO();
            new ClienteController(clienteDAO, view);
            view.setVisible(true);
        });
    }
}