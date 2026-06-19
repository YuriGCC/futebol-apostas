package view;

import dao.EventoDAO;
import model.Evento;
import model.StatusEvento;
import model.TipoPalpite;
import service.FinanceiroService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DashboardAdminView extends JFrame {
    
    private JLabel lblTitulo;
    private JButton btnCadastrarEvento;
    private JButton btnEncerrarEvento;
    private JButton btnSair;

    private EventoDAO eventoDAO;
    private FinanceiroService financeiroService;

    public DashboardAdminView() {
        this.eventoDAO = new EventoDAO();
        this.financeiroService = new FinanceiroService();

        setTitle("Painel Administrativo");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        lblTitulo = new JLabel("Gestão de Eventos e Pagamentos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setBounds(80, 20, 300, 25);
        add(lblTitulo);

        btnCadastrarEvento = new JButton("Cadastrar Novo Jogo");
        btnCadastrarEvento.setBounds(100, 70, 200, 35);
        add(btnCadastrarEvento);

        btnEncerrarEvento = new JButton("Encerrar Jogo e Pagar");
        btnEncerrarEvento.setBounds(100, 120, 200, 35);
        add(btnEncerrarEvento);
        
        btnSair = new JButton("Sair");
        btnSair.setBounds(150, 180, 100, 30);
        add(btnSair);

        btnCadastrarEvento.addActionListener(e -> abrirTelaCadastroEvento());
        btnEncerrarEvento.addActionListener(e -> abrirTelaEncerramentoEvento());
        
        btnSair.addActionListener(e -> {
            this.dispose();
            new LoginView().setVisible(true); 
        });
    }

    private void abrirTelaCadastroEvento() {
        JTextField txtCasa = new JTextField();
        JTextField txtFora = new JTextField();
        JTextField txtOddCasa = new JTextField();
        JTextField txtOddEmpate = new JTextField();
        JTextField txtOddFora = new JTextField();

        Object[] message = {
            "Time da Casa:", txtCasa,
            "Time de Fora:", txtFora,
            "Odd Casa (ex: 1.5):", txtOddCasa,
            "Odd Empate (ex: 3.2):", txtOddEmpate,
            "Odd Fora (ex: 4.0):", txtOddFora
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Cadastrar Novo Evento", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            try {
                Evento e = new Evento();
                e.setTimeCasa(txtCasa.getText());
                e.setTimeFora(txtFora.getText());
                e.setOddCasa(Double.parseDouble(txtOddCasa.getText()));
                e.setOddEmpate(Double.parseDouble(txtOddEmpate.getText()));
                e.setOddFora(Double.parseDouble(txtOddFora.getText()));
                e.setStatus(StatusEvento.ABERTO);

                eventoDAO.inserir(e);
                JOptionPane.showMessageDialog(this, "Evento cadastrado com sucesso!");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Erro: Verifique se as odds foram digitadas como números válidos.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar evento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void abrirTelaEncerramentoEvento() {
        ArrayList<Evento> eventosAbertos = eventoDAO.listarEventosAbertos();
        
        if (eventosAbertos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há eventos abertos no momento.");
            return;
        }

        String[] opcoesEventos = new String[eventosAbertos.size()];
        for (int i = 0; i < eventosAbertos.size(); i++) {
            Evento e = eventosAbertos.get(i);
            opcoesEventos[i] = e.getId() + " - " + e.getTimeCasa() + " x " + e.getTimeFora();
        }

        JComboBox<String> comboEventos = new JComboBox<>(opcoesEventos);
        JComboBox<TipoPalpite> comboResultado = new JComboBox<>(TipoPalpite.values());

        Object[] message = {
            "Selecione o Evento:", comboEventos,
            "Resultado Oficial:", comboResultado
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Encerrar Evento", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                int indexSelecionado = comboEventos.getSelectedIndex();
                Evento eventoSelecionado = eventosAbertos.get(indexSelecionado);
                TipoPalpite resultadoFinal = (TipoPalpite) comboResultado.getSelectedItem();

            
                financeiroService.encerrarEvento(eventoSelecionado, resultadoFinal);
                
                JOptionPane.showMessageDialog(this, "Evento encerrado e apostas processadas com sucesso!");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao encerrar evento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}