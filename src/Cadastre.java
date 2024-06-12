import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Cadastre extends JDialog {
    private JPanel cadastrePanel;
    private JTextField nome;
    private JTextField fone;
    private JPasswordField password;
    private JButton btnCadastre;
    private JComboBox<String> comboBox;
    private JButton btnCancel;

    public Cadastre(JFrame parent) {
        setTitle("Cadastro");
        setContentPane(getCadastrePanel());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(parent);

        getComboBox().addItem("CLIENTE");
        getComboBox().addItem("BARBEIRO");

        getBtnCancel().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Login(parent, new Menu());
            }
        });

        getBtnCadastre().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = getNome().getText();
                String telefone = getFone().getText();
                String senha = new String(getPassword().getPassword());
                String tipo = (String) getComboBox().getSelectedItem();

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

    // Atributos privados
    private JPanel getCadastrePanel() {
        return cadastrePanel;
    }

    private void setCadastrePanel(JPanel cadastrePanel) {
        this.cadastrePanel = cadastrePanel;
    }

    private JTextField getNome() {
        return nome;
    }

    private void setNome(JTextField nome) {
        this.nome = nome;
    }

    private JTextField getFone() {
        return fone;
    }

    private void setFone(JTextField fone) {
        this.fone = fone;
    }

    private JPasswordField getPassword() {
        return password;
    }

    private void setPassword(JPasswordField password) {
        this.password = password;
    }

    private JButton getBtnCadastre() {
        return btnCadastre;
    }

    private void setBtnCadastre(JButton btnCadastre) {
        this.btnCadastre = btnCadastre;
    }

    private JComboBox<String> getComboBox() {
        return comboBox;
    }

    private void setComboBox(JComboBox<String> comboBox) {
        this.comboBox = comboBox;
    }

    private JButton getBtnCancel() {
        return btnCancel;
    }

    private void setBtnCancel(JButton btnCancel) {
        this.btnCancel = btnCancel;
    }
}
