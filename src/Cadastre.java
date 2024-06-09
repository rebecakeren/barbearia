import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Cadastre extends JDialog {
    private JPanel cadastrePanel;
    private JTextField name;
    private JTextField fone;
    private JPasswordField password;
    private JButton btnCadastre;
    private JComboBox<String> comboBox;
    private JButton btnCancel;

    public Cadastre(JFrame parent) {
        setTitle("Cadastro");
        setContentPane(cadastrePanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(parent);

        comboBox.addItem("CLIENTE");
        comboBox.addItem("BARBEIRO");

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Login(parent, new Menu());
            }
        });

        btnCadastre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = name.getText();
                String telefone = fone.getText();
                String senha = new String(password.getPassword());
                String tipo = (String) comboBox.getSelectedItem();

                if (nome.isEmpty() || telefone.isEmpty() || senha.isEmpty() || tipo == null) {
                    JOptionPane.showMessageDialog(Cadastre.this, "Todos os campos devem ser preenchidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                } else {
                    try (Connection conn = DatabaseConnection.getConnection()) {
                        String query = "INSERT INTO usuarios (nome, telefone, senha, tipo) VALUES (?, ?, ?, ?)";
                        PreparedStatement stmt = conn.prepareStatement(query);
                        stmt.setString(1, nome);
                        stmt.setString(2, telefone);
                        stmt.setString(3, senha);
                        stmt.setString(4, tipo);
                        stmt.executeUpdate();

                        JOptionPane.showMessageDialog(Cadastre.this, "Cadastro realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        new Login(parent, new Menu());
                        dispose();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(Cadastre.this, "Erro ao acessar o banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Cadastre(null));
    }
}