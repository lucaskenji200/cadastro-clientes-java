package com.germantech.controller;

import java.awt.GridLayout;
import java.util.Collections;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.germantech.model.Cliente;
import com.germantech.model.VerificaCPF;
import com.germantech.model.dao.ClienteDAO;
import com.germantech.view.MainWindow;

public class ClienteController {

    private final ClienteDAO clienteDAO;
    private final MainWindow view;

    public ClienteController(ClienteDAO clienteDAO, MainWindow view) {
        this.clienteDAO = clienteDAO;
        this.view = view;
        adicionarListeners();
        atualizarTabela();
    }

    private void adicionarListeners() {
        view.getBotaoCriar().addActionListener(e -> criarClienteAction());
        view.getBotaoEditar().addActionListener(e -> editarClienteAction());
        view.getBotaoRemover().addActionListener(e -> removerClienteAction());
        view.getBotaoPesquisar().addActionListener(e -> pesquisarAction());
        view.getBotaoLimpar().addActionListener(e -> {
            view.setValorPesquisa("");
            atualizarTabela();
        });
    }

    private void atualizarTabela() {
        try {
            view.preencherTabela(clienteDAO.listarTodos());
        } catch (RuntimeException e) {
            view.mostrarMensagem("Erro ao carregar clientes: " + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pesquisarAction() {
        String criterio = view.getCriterioPesquisa();
        String valor = view.getValorPesquisa().trim();

        if (valor.isEmpty()) {
            atualizarTabela();
            return;
        }
        
        try {
            if ("CPF".equals(criterio)) {
                clienteDAO.buscarPorCPF(valor)
                    .ifPresentOrElse(
                        cliente -> view.preencherTabela(Collections.singletonList(cliente)),
                        () -> {
                            view.mostrarMensagem("Nenhum cliente encontrado com este CPF.", "Não Encontrado", JOptionPane.INFORMATION_MESSAGE);
                            view.preencherTabela(Collections.emptyList());
                        }
                    );
            } else if ("Email".equals(criterio)) {
                view.preencherTabela(clienteDAO.buscarPorEmail(valor));
            } else {
                view.preencherTabela(clienteDAO.buscarPorNome(valor));
            }
        } catch (RuntimeException e) {
            view.mostrarMensagem("Erro ao pesquisar clientes: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void criarClienteAction() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField nomeField = new JTextField();
        JTextField telefoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField cpfField = new JTextField();
        JPasswordField senhaField = new JPasswordField();
        panel.add(new JLabel("Nome:")); panel.add(nomeField);
        panel.add(new JLabel("Telefone:")); panel.add(telefoneField);
        panel.add(new JLabel("Email:")); panel.add(emailField);
        panel.add(new JLabel("CPF (xxx.xxx.xxx-xx):")); panel.add(cpfField);
        panel.add(new JLabel("Senha:")); panel.add(senhaField);

        int result = JOptionPane.showConfirmDialog(view, panel, "Criar Novo Cliente", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String nome = nomeField.getText();
                String cpf = cpfField.getText();
                String senha = new String(senhaField.getPassword());
                
                if (nome.trim().isEmpty() || cpf.trim().isEmpty() || senha.trim().isEmpty()) {
                     view.mostrarMensagem("Nome, CPF e Senha são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                     return;
                }
                
                if (!VerificaCPF.isCPF(cpf)) {
                    view.mostrarMensagem("CPF inválido!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (clienteDAO.cpfExiste(cpf)) {
                    view.mostrarMensagem("Este CPF já está cadastrado.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Cliente novoCliente = new Cliente(nome, telefoneField.getText(), emailField.getText(), cpf, senha);
                clienteDAO.criar(novoCliente);
                view.mostrarMensagem("Cliente criado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarTabela();
            } catch (RuntimeException e) {
                view.mostrarMensagem("Erro ao criar cliente: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void editarClienteAction() {
        int selectedRow = view.getTable().getSelectedRow();
        if (selectedRow < 0) {
            view.mostrarMensagem("Por favor, selecione um cliente para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cpf = (String) view.getTable().getValueAt(selectedRow, 4);
        Cliente clienteParaEditar = clienteDAO.buscarPorCPF(cpf).orElse(null);

        if (clienteParaEditar == null) {
            view.mostrarMensagem("Cliente não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            atualizarTabela();
            return;
        }

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField nomeField = new JTextField(clienteParaEditar.getNome());
        JTextField telefoneField = new JTextField(clienteParaEditar.getTelefone());
        JTextField emailField = new JTextField(clienteParaEditar.getEmail());
        JTextField cpfField = new JTextField(clienteParaEditar.getCPF());
        cpfField.setEditable(false);
        JPasswordField senhaField = new JPasswordField();
        panel.add(new JLabel("Nome:")); panel.add(nomeField);
        panel.add(new JLabel("Telefone:")); panel.add(telefoneField);
        panel.add(new JLabel("Email:")); panel.add(emailField);
        panel.add(new JLabel("CPF:")); panel.add(cpfField);
        panel.add(new JLabel("Nova Senha (deixe em branco para manter):")); panel.add(senhaField);

        int result = JOptionPane.showConfirmDialog(view, panel, "Editar Cliente", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String novaSenha = new String(senhaField.getPassword());
                if (novaSenha.trim().isEmpty()) {
                    novaSenha = clienteParaEditar.getSenha();
                }
                Cliente clienteAtualizado = new Cliente(clienteParaEditar.getID(), nomeField.getText(), telefoneField.getText(), emailField.getText(), cpf, novaSenha);
                if (clienteDAO.atualizar(clienteAtualizado)) {
                    view.mostrarMensagem("Cliente atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                     view.mostrarMensagem("Nenhum cliente foi atualizado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
                atualizarTabela();
            } catch (RuntimeException e) {
                view.mostrarMensagem("Erro ao atualizar cliente: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void removerClienteAction() {
        int selectedRow = view.getTable().getSelectedRow();
        if (selectedRow < 0) {
            view.mostrarMensagem("Por favor, selecione um cliente para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cpf = (String) view.getTable().getValueAt(selectedRow, 4);
        String nome = (String) view.getTable().getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(view, "Tem certeza que deseja remover o cliente '" + nome + "'?", "Confirmar Remoção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if(clienteDAO.remover(cpf)) {
                    view.mostrarMensagem("Cliente removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    view.mostrarMensagem("Nenhum cliente foi removido.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
                atualizarTabela();
            } catch (RuntimeException e) {
                view.mostrarMensagem("Erro ao remover cliente: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}