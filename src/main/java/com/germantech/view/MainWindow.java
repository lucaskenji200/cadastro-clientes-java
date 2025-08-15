package com.germantech.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.germantech.model.Cliente;

public class MainWindow extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> comboPesquisa;
    private JTextField campoPesquisa;
    private JButton botaoCriar, botaoEditar, botaoRemover, botaoPesquisar, botaoLimpar;

    public MainWindow() {
        configurarJanela();
        inicializarComponentes();
    }

    private void configurarJanela() {
        setTitle("German Tech - Cadastro de Clientes");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));
    }

    private void inicializarComponentes() {
        JPanel painelSuperior = new JPanel(new BorderLayout(10, 10));
        painelSuperior.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel text = new JLabel("Cadastro de Clientes");
        text.setFont(new Font("SansSerif", Font.BOLD, 24));
        text.setHorizontalAlignment(SwingConstants.CENTER);
        painelSuperior.add(text, BorderLayout.NORTH);
        
        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        comboPesquisa = new JComboBox<>(new String[]{"Nome", "CPF", "Email"});
        campoPesquisa = new JTextField(20);
        botaoPesquisar = new JButton("Pesquisar");
        botaoLimpar = new JButton("Limpar Filtro");
        painelPesquisa.add(new JLabel("Pesquisar por:"));
        painelPesquisa.add(comboPesquisa);
        painelPesquisa.add(campoPesquisa);
        painelPesquisa.add(botaoPesquisar);
        painelPesquisa.add(botaoLimpar);
        painelSuperior.add(painelPesquisa, BorderLayout.CENTER);
        
        add(painelSuperior, BorderLayout.NORTH);

        JPanel painelCentral = new JPanel(new BorderLayout());

        JPanel painelDeBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        painelDeBotoes.setBorder(new EmptyBorder(10, 10, 20, 10));
        botaoCriar = new JButton("Criar Cliente");
        botaoEditar = new JButton("Editar Cliente");
        botaoRemover = new JButton("Remover Cliente");
        painelDeBotoes.add(botaoCriar);
        painelDeBotoes.add(botaoEditar);
        painelDeBotoes.add(botaoRemover);
        
        painelCentral.add(painelDeBotoes, BorderLayout.NORTH);

        String[] colunas = {"ID", "Nome", "Telefone", "Email", "CPF"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(0, 10, 10, 10));

        painelCentral.add(scrollPane, BorderLayout.CENTER);

        add(painelCentral, BorderLayout.CENTER);
    }
    
    public void preencherTabela(List<Cliente> clientes) {
        tableModel.setRowCount(0);
        for (Cliente cliente : clientes) {
            Object[] rowData = {
                cliente.getID(), cliente.getNome(), cliente.getTelefone(),
                cliente.getEmail(), cliente.getCPF()
            };
            tableModel.addRow(rowData);
        }
    }

    public void mostrarMensagem(String mensagem, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, mensagem, titulo, tipo);
    }

    public JTable getTable() { return table; }
    public String getCriterioPesquisa() { return (String) comboPesquisa.getSelectedItem(); }
    public String getValorPesquisa() { return campoPesquisa.getText(); }
    public void setValorPesquisa(String texto) { campoPesquisa.setText(texto); }
    public JButton getBotaoCriar() { return botaoCriar; }
    public JButton getBotaoEditar() { return botaoEditar; }
    public JButton getBotaoRemover() { return botaoRemover; }
    public JButton getBotaoPesquisar() { return botaoPesquisar; }
    public JButton getBotaoLimpar() { return botaoLimpar; }
}