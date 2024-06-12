import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.math.BigDecimal;

public class Service extends JDialog {
    private JPanel servicePanel;
    private JTextField nome;
    private JTextField valor;
    private JButton btnCadastrar;
    private JButton btnCancelar;

    public Service(JFrame parent) {
        setTitle("Cadastro de Serviço");
        setContentPane(getServicePanel());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(parent);

        getBtnCadastrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeServico = getNome().getText();
                String valorServicoStr = getValor().getText();

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
                            dispose();
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

        getBtnCancelar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    // Atributos privados
    private JPanel getServicePanel() {
        return servicePanel;
    }

    private void setServicePanel(JPanel servicePanel) {
        this.servicePanel = servicePanel;
    }

    private JTextField getNome() {
        return nome;
    }

    private void setNome(JTextField nome) {
        this.nome = nome;
    }

    private JTextField getValor() {
        return valor;
    }

    private void setValor(JTextField valor) {
        this.valor = valor;
    }

    private JButton getBtnCadastrar() {
        return btnCadastrar;
    }

    private void setBtnCadastrar(JButton btnCadastrar) {
        this.btnCadastrar = btnCadastrar;
    }

    private JButton getBtnCancelar() {
        return btnCancelar;
    }

    private void setBtnCancelar(JButton btnCancelar) {
        this.btnCancelar = btnCancelar;
    }
}
