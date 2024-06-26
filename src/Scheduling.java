import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Scheduling extends JDialog {
    private JPanel schedulingPanel;
    private JTextField barbeiro;
    private JTextField servico;
    private JTextField dia;
    private JTextField mes;
    private JTextField ano;
    private JLabel valor;
    private JButton btnAgendar;
    private JButton btnCancelar;

    public Scheduling() {
        setTitle("Agendamento");
        setContentPane(getSchedulingPanel());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);

        getServico().addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    atualizarValorServico();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(Scheduling.this, "Erro ao buscar valor do serviço: " + ex.getMessage());
                }
            }
        });

        getBtnAgendar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    agendar();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(Scheduling.this, "Erro ao conectar ao banco de dados: " + ex.getMessage());
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
    private JPanel getSchedulingPanel() {
        return schedulingPanel;
    }

    private void setSchedulingPanel(JPanel schedulingPanel) {
        this.schedulingPanel = schedulingPanel;
    }

    private JTextField getBarbeiro() {
        return barbeiro;
    }

    private void setBarbeiro(JTextField barbeiro) {
        this.barbeiro = barbeiro;
    }

    private JTextField getServico() {
        return servico;
    }

    private void setServico(JTextField servico) {
        this.servico = servico;
    }

    private JTextField getDia() {
        return dia;
    }

    private void setDia(JTextField dia) {
        this.dia = dia;
    }

    private JTextField getMes() {
        return mes;
    }

    private void setMes(JTextField mes) {
        this.mes = mes;
    }

    private JTextField getAno() {
        return ano;
    }

    private void setAno(JTextField ano) {
        this.ano = ano;
    }

    private JLabel getValor() {
        return valor;
    }

    private void setValor(JLabel valor) {
        this.valor = valor;
    }

    private JButton getBtnAgendar() {
        return btnAgendar;
    }

    private void setBtnAgendar(JButton btnAgendar) {
        this.btnAgendar = btnAgendar;
    }

    private JButton getBtnCancelar() {
        return btnCancelar;
    }

    private void setBtnCancelar(JButton btnCancelar) {
        this.btnCancelar = btnCancelar;
    }

    private void atualizarValorServico() throws SQLException {
        String servicoText = getServico().getText();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT valor FROM servicos WHERE nome = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, servicoText);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        double valorServico = resultSet.getDouble("valor");
                        getValor().setText(String.format("R$ %.2f", valorServico));
                    } else {
                        getValor().setText("Serviço não encontrado");
                    }
                }
            }
        }
    }

    private void agendar() throws SQLException {
        String barbeiroText = getBarbeiro().getText();
        String servicoText = getServico().getText();
        String diaText = getDia().getText();
        String mesText = getMes().getText();
        String anoText = getAno().getText();

        if (barbeiroText.isEmpty() || servicoText.isEmpty() || diaText.isEmpty() || mesText.isEmpty() || anoText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.");
            return;
        }

        String dataStr = String.format("%s-%s-%s", anoText, mesText, diaText);
        java.sql.Date dataSql;
        try {
            java.util.Date dataUtil = new SimpleDateFormat("dd-MM-yyyy").parse(dataStr);
            dataSql = new java.sql.Date(dataUtil.getTime());
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Data inválida. Use o formato YYYY-MM-DD.");
            return;
        }

        double valorServico;
        try {
            valorServico = Double.parseDouble(getValor().getText().replace("R$", "").trim().replace(",", "."));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor do serviço inválido.");
            return;
        }

        if (!isBarbeiroExistente(barbeiroText)) {
            JOptionPane.showMessageDialog(this, "O barbeiro não está registrado.");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO agendamentos (barbeiro, servico, data, usuario, valor) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, barbeiroText);
                statement.setString(2, servicoText);
                statement.setDate(3, dataSql);
                statement.setString(4, Login.getLoggedUser());
                statement.setDouble(5, valorServico);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Agendamento realizado com sucesso!");
                    dispose();
                }
            }
        }
    }

    private boolean isBarbeiroExistente(String barbeiroNome) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT COUNT(*) AS count FROM usuarios WHERE nome = ? AND tipo = 'BARBEIRO'";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, barbeiroNome);
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        }
    }
}
