package view;

import dao.ApostaDAO;
import dao.EventoDAO;
import dao.UsuarioDAO;
import model.Aposta;
import model.Evento;
import model.TipoPalpite;
import model.Usuario;
import service.FinanceiroService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DashboardApostadorView extends JFrame {
    
    private Usuario usuarioLogado;
    
    private JLabel lblBoasVindas;
    private JLabel lblSaldo;
    private JButton btnApostar;
    private JButton btnMinhasApostas;
    private JButton btnSair;

    private EventoDAO eventoDAO;
    private ApostaDAO apostaDAO;
    private UsuarioDAO usuarioDAO;
    private FinanceiroService financeiroService;

    public DashboardApostadorView(Usuario usuario) {
        this.usuarioLogado = usuario;
        
        // Inicializa as dependências
        this.eventoDAO = new EventoDAO();
        this.apostaDAO = new ApostaDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.financeiroService = new FinanceiroService();
        
        setTitle("Área do Apostador");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null); 
        
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        lblBoasVindas = new JLabel("Olá, " + usuarioLogado.getNome());
        lblBoasVindas.setFont(new Font("Arial", Font.BOLD, 14));
        lblBoasVindas.setBounds(20, 20, 300, 25);
        add(lblBoasVindas);

        lblSaldo = new JLabel();
        atualizarLabelSaldo(); 
        lblSaldo.setBounds(20, 50, 300, 25);
        add(lblSaldo);

        btnApostar = new JButton("Nova Aposta");
        btnApostar.setBounds(20, 90, 150, 30);
        add(btnApostar);
        
        btnMinhasApostas = new JButton("Minhas Apostas");
        btnMinhasApostas.setBounds(20, 130, 150, 30);
        add(btnMinhasApostas);

        btnSair = new JButton("Sair");
        btnSair.setBounds(20, 180, 100, 30);
        add(btnSair);

        // Eventos de clique
        btnApostar.addActionListener(e -> abrirTelaNovaAposta());
        btnMinhasApostas.addActionListener(e -> abrirTelaMinhasApostas());
        btnSair.addActionListener(e -> {
            this.dispose();
            new LoginView().setVisible(true); 
        });
    }

    private void atualizarLabelSaldo() {
        usuarioLogado = usuarioDAO.buscar(usuarioLogado.getId());
        lblSaldo.setText("Saldo Atual: R$ " + String.format("%.2f", usuarioLogado.getSaldo()));
    }

    private void abrirTelaNovaAposta() {
        ArrayList<Evento> eventosAbertos = eventoDAO.listarEventosAbertos();

        if (eventosAbertos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há eventos abertos para apostar no momento.");
            return;
        }

        // Monta as opções do ComboBox mostrando os times e as odds
        String[] opcoesEventos = new String[eventosAbertos.size()];
        for (int i = 0; i < eventosAbertos.size(); i++) {
            Evento e = eventosAbertos.get(i);
            opcoesEventos[i] = e.getId() + " - " + e.getTimeCasa() + " x " + e.getTimeFora() + 
                               " (1: " + e.getOddCasa() + " | X: " + e.getOddEmpate() + " | 2: " + e.getOddFora() + ")";
        }

        JComboBox<String> comboEventos = new JComboBox<>(opcoesEventos);
        JComboBox<TipoPalpite> comboPalpite = new JComboBox<>(TipoPalpite.values());
        JTextField txtValor = new JTextField();

        Object[] message = {
            "Selecione o Evento:", comboEventos,
            "Seu Palpite:", comboPalpite,
            "Valor da Aposta (R$):", txtValor
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Realizar Nova Aposta", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                int indexSelecionado = comboEventos.getSelectedIndex();
                Evento eventoSelecionado = eventosAbertos.get(indexSelecionado);
                TipoPalpite palpite = (TipoPalpite) comboPalpite.getSelectedItem();

                double valor = Double.parseDouble(txtValor.getText().replace(",", "."));

                if (valor <= 0) {
                    JOptionPane.showMessageDialog(this, "O valor da aposta deve ser maior que zero.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

              
                boolean sucesso = financeiroService.processarAposta(usuarioLogado, eventoSelecionado, valor, palpite);

                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Aposta realizada com sucesso!");
                    atualizarLabelSaldo(); // Atualiza a view com o novo saldo debitado
                } else {
                    JOptionPane.showMessageDialog(this, "Saldo insuficiente ou evento já encerrado.", "Operação Recusada", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valor inválido. Digite um número corretamente.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao processar aposta: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void abrirTelaMinhasApostas() {
        ArrayList<Aposta> minhasApostas = apostaDAO.listarApostasPorUsuario(usuarioLogado.getId());

        if (minhasApostas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Você ainda não realizou nenhuma aposta.");
            return;
        }


        StringBuilder sb = new StringBuilder();
        sb.append("ID | Evento ID | Valor | Palpite | Status | Retorno Potencial\n");
        sb.append("----------------------------------------------------------------------\n");

        for (Aposta a : minhasApostas) {
            sb.append(String.format("%d | %d | R$ %.2f | %s | %s | R$ %.2f\n",
                    a.getId(), a.getEventoId(), a.getValorApostado(),
                    a.getPalpite().name(), a.getStatus().name(), a.getGanhoPotencial()));
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); 
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 200));

        JOptionPane.showMessageDialog(this, scrollPane, "Meu Histórico de Apostas", JOptionPane.INFORMATION_MESSAGE);
    }
}