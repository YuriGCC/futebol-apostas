import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    
    // Componentes Semânticos do Swing
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private JLabel lblMensagem;

    public LoginView() {
        setTitle("Sistema de Apostas - Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Usando null layout para simplificar o exemplo, mas recomendo LayoutManagers
        
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JLabel lblLogin = new JLabel("Login:");
        lblLogin.setBounds(20, 20, 80, 25);
        add(lblLogin);

        txtLogin = new JTextField();
        txtLogin.setBounds(100, 20, 160, 25);
        add(txtLogin);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setBounds(20, 60, 80, 25);
        add(lblSenha);

        txtSenha = new JPasswordField();
        txtSenha.setBounds(100, 60, 160, 25);
        add(txtSenha);

        btnEntrar = new JButton("Entrar");
        btnEntrar.setBounds(100, 100, 100, 25);
        add(btnEntrar);
        
        lblMensagem = new JLabel("");
        lblMensagem.setBounds(20, 130, 250, 25);
        add(lblMensagem);

        // O Evento exigido no requisito 8
        btnEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
    }

    private void realizarLogin() {
        String login = txtLogin.getText();
        String senha = new String(txtSenha.getPassword());

        // View chamando o Backend (DAO)
        UsuarioDAO dao = new UsuarioDAO();
        Usuario user = dao.buscarPorLoginESenha(login, senha);

        if (user != null) {
            JOptionPane.showMessageDialog(this, "Bem-vindo, " + user.getNome() + "!");
            this.dispose(); // Fecha tela de login
            
            // Lógica de roteamento baseada no banco de dados
            if (user.isEhAdmin()) {
                new DashboardAdminView().setVisible(true);
            } else {
                new DashboardApostadorView(user).setVisible(true);
            }
        } else {
            lblMensagem.setText("Credenciais inválidas!");
        }
    }
}