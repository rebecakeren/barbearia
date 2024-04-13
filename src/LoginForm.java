import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JDialog {
    private JTextField tfNome;
    private JPasswordField pfPassword;
    private JButton btnEntrar;
    private JButton btnSair;
    private JButton btnCadastrar;
    private JPanel loginPanel;
    private HomePage homePage;


    public LoginForm(JFrame parent, HomePage homePage) {
        super(parent);
        this.homePage = homePage;
        setTitle("Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(570, 470));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = tfNome.getText();
                String password = new String(pfPassword.getPassword());
                if (username.equals("user") && password.equals("pass")) {
                    dispose();
                    homePage.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Invalid username or password",
                            "Error",
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

        setVisible(true);
    }

    public static void main(String[] args) {
        JFrame parentFrame = new JFrame();
        HomePage homePage = new HomePage();
        LoginForm loginForm = new LoginForm(parentFrame, homePage);
    }
}