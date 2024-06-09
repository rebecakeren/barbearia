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
        setTitle("Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(800, 500));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = tfNome.getText();
                String password = new String(pfPassword.getPassword());
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

        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                dispose();
            }
        });

        btnCadastrar.addActionListener(new ActionListener() {
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

    public static String getLoggedUser() {
        return loggedUser;
    }

    public static void main(String[] args) {
        JFrame parentFrame = new JFrame();
        Menu menu = new Menu();
        Login login = new Login(parentFrame, menu);
    }
}