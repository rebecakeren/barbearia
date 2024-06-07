import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Service extends JDialog {
    private JPanel servicePanel;
    private JLabel menu;
    private JTextField nome;
    private JTextField valor;
    private JButton cadastrar;

    public Service(JFrame parent) {
        setTitle("Cadastro de Serviço");
        setContentPane(servicePanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(parent);

        // Configuração do menu popup
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem1 = new JMenuItem("Menu");
        JMenuItem menuItem2 = new JMenuItem("Agendamento");
        JMenuItem menuItem3 = new JMenuItem("Sair");

        popupMenu.add(menuItem1);
        popupMenu.add(menuItem2);
        popupMenu.add(menuItem3);

        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Menu();
            }
        });

        menuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Scheduling();
            }
        });

        menuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        menu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                popupMenu.show(menu, e.getX(), e.getY());
            }
        });

        // Adiciona o listener do botão cadastrar
        cadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeServico = nome.getText();
                String valorServico = valor.getText();

                if (nomeServico.isEmpty() || valorServico.isEmpty()) {
                    JOptionPane.showMessageDialog(Service.this, "Todos os campos devem ser preenchidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                } else {
                    try (Connection conn = DatabaseConnection.getConnection()) {
                        String query = "INSERT INTO service (nome, valor) VALUES (?, ?)";
                        PreparedStatement stmt = conn.prepareStatement(query);
                        stmt.setString(1, nomeServico);
                        stmt.setString(2, valorServico);
                        stmt.executeUpdate();

                        JOptionPane.showMessageDialog(Service.this, "Serviço cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        dispose(); // Fecha a janela de cadastro
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(Service.this, "Erro ao acessar o banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Service(null));
    }
}
