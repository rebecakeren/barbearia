import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JDialog {
    private JTextField tfNome;
    private JPasswordField pfPassword;
    private JButton btnEntrar;
    private JButton btnSair;
    private JButton btnCadastrar;
    private JPanel loginPanel;
    private Menu menu;

    private String userType;
    private static String loggedUser;

    public Login(JFrame parent, Menu menu) {
        super(parent);
        this.menu = menu;
        setTitle("Login");
        setContentPane(getLoginPanel());
        setMinimumSize(new Dimension(800, 500));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        getBtnEntrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = getTfNome().getText();
                String password = new String(getPfPassword().getPassword());
                if (authenticate(username, password)) {
                    loggedUser = username;
                    dispose();
                    menu.setUserType(userType);
                    menu.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(Login.this,
                            "Nome ou senha inválida.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        getBtnSair().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                dispose();
            }
        });

        getBtnCadastrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Cadastre(parent);
            }
        });

        setVisible(true);
    }


    private boolean authenticate(String username, String password) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT tipo FROM usuarios WHERE nome = ? AND senha = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                userType = rs.getString("tipo");
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Métodos Get e Set privados
    private String getUserType() {
        return userType;
    }

    private void setUserType(String userType) {
        this.userType = userType;
    }

    public static String getLoggedUser(){
        return loggedUser;
    }

    private void setLoggedUser(){
        this.loggedUser = loggedUser;
    }

    private JPanel getLoginPanel() {
        return loginPanel;
    }

    private JTextField getTfNome() {
        return tfNome;
    }

    private void setTfNome(JTextField tfNome) {
        this.tfNome = tfNome;
    }

    private JPasswordField getPfPassword() {
        return pfPassword;
    }

    private void setPfPassword(JPasswordField pfPassword) {
        this.pfPassword = pfPassword;
    }

    private JButton getBtnEntrar() {
        return btnEntrar;
    }

    private void setBtnEntrar(JButton btnEntrar) {
        this.btnEntrar = btnEntrar;
    }

    private JButton getBtnSair() {
        return btnSair;
    }

    private void setBtnSair(JButton btnSair) {
        this.btnSair = btnSair;
    }

    private JButton getBtnCadastrar() {
        return btnCadastrar;
    }

    private void setBtnCadastrar(JButton btnCadastrar) {
        this.btnCadastrar = btnCadastrar;
    }

    public static void main(String[] args) {
        JFrame parentFrame = new JFrame();
        Menu menu = new Menu();
        Login login = new Login(parentFrame, menu);
    }
}
