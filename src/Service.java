import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.math.BigDecimal;

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

        // Configura o menu pop-up
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem1 = new JMenuItem("Agendamento");
        JMenuItem menuItem2 = new JMenuItem("Sair");

        popupMenu.add(menuItem1);
        popupMenu.add(menuItem2);

        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                // new Scheduling(); // Redireciona para a tela de agendamento
            }
        });

        menuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                // new Menu(); // Redireciona para a tela de menu
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
                String valorServicoStr = valor.getText();

                if (nomeServico.isEmpty() || valorServicoStr.isEmpty()) {
                    JOptionPane.showMessageDialog(Service.this, "Todos os campos devem ser preenchidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        BigDecimal valorServico = new BigDecimal(valorServicoStr);

                        try (Connection conn = DatabaseConnection.getConnection()) {
                            String query = "INSERT INTO servicos (nome, valor) VALUES (?, ?)";
                            PreparedStatement stmt = conn.prepareStatement(query);
                            stmt.setString(1, nomeServico);
                            stmt.setBigDecimal(2, valorServico);
                            stmt.executeUpdate();

                            JOptionPane.showMessageDialog(Service.this, "Serviço cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                            dispose(); // Fecha a janela de cadastro
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(Service.this, "O valor do serviço deve ser um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
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
