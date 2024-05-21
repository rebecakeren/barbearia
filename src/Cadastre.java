import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Cadastre {
    private JPanel cadastreForm;
    private JTextField textField1;
    private JTextField textField2;
    private JPasswordField passwordField1;
    private JButton btnCadastre;
    private JComboBox<String> comboBox;
    private JButton btnCancel;

    public Cadastre(JFrame parent) {
        // Inicialize os componentes
        cadastreForm = new JPanel();
        textField1 = new JTextField(20);
        textField2 = new JTextField(20);
        passwordField1 = new JPasswordField(20);
        btnCadastre = new JButton("Cadastrar");
        btnCancel = new JButton("Cancelar");

        // Inicialize e configure o JComboBox
        comboBox = new JComboBox<>();
        comboBox.addItem("CLIENTE");
        comboBox.addItem("BARBEIRO");

        // Adicione os componentes ao painel
        cadastreForm.add(new JLabel("Nome:"));
        cadastreForm.add(textField1);
        cadastreForm.add(new JLabel("Telefone:"));
        cadastreForm.add(textField2);
        cadastreForm.add(new JLabel("Senha:"));
        cadastreForm.add(passwordField1);
        cadastreForm.add(new JLabel("Tipo:"));
        cadastreForm.add(comboBox);
        cadastreForm.add(btnCadastre);
        cadastreForm.add(btnCancel);

        // Configurações adicionais do JFrame
        JFrame frame = new JFrame("Cadastro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(cadastreForm);
        frame.pack();
        frame.setVisible(true);

        // Adiciona os listeners dos botões
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Fecha a janela atual
                new Login(parent, new Menu()); // Abre a janela de login
            }
        });

        btnCadastre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = textField1.getText();
                String telefone = textField2.getText();
                String senha = new String(passwordField1.getPassword());
                String tipo = (String) comboBox.getSelectedItem();

                if (nome.isEmpty() || telefone.isEmpty() || senha.isEmpty() || tipo == null) {
                    JOptionPane.showMessageDialog(frame, "Todos os campos devem ser preenchidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                } else {
                    try (Connection conn = DatabaseConnection.getConnection()) {
                        String query = "INSERT INTO usuarios (nome, telefone, senha, tipo) VALUES (?, ?, ?, ?)";
                        PreparedStatement stmt = conn.prepareStatement(query);
                        stmt.setString(1, nome);
                        stmt.setString(2, telefone);
                        stmt.setString(3, senha);
                        stmt.setString(4, tipo);
                        stmt.executeUpdate();

                        JOptionPane.showMessageDialog(frame, "Cadastro realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        new Login(parent, new Menu());
                        frame.dispose(); // Fecha a janela de cadastro
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(frame, "Erro ao acessar o banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        // Executa a aplicação
        SwingUtilities.invokeLater(() -> new Cadastre(null));
    }
}
