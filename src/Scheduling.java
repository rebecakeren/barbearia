import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;


class Scheduling extends JDialog {
    private JPanel schedulingPanel;
    private JLabel menuLabel;
    private JComboBox<String> comboServico;
    private JComboBox<String> comboBarbeiro;
    private JTextField dia;
    private JTextField mes;
    private JTextField ano;
    private JTextField valor;
    private JButton btnAgendar;

    public Scheduling() {
        setTitle("Agendamento");
        setContentPane(schedulingPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1080, 900);
        setLocationRelativeTo(null);

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem1 = new JMenuItem("Menu");
        JMenuItem menuItem2 = new JMenuItem("Sair");

        popupMenu.add(menuItem1);
        popupMenu.add(menuItem2);

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
            }
        });

        menuLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                popupMenu.show(menuLabel, e.getX(), e.getY());
            }
        });

        // Initialize storePanel and components
        schedulingPanel = new JPanel();
        comboServico = new JComboBox<>();
        comboBarbeiro = new JComboBox<>();
        dia = new JTextField(10);
        mes = new JTextField(10);
        ano = new JTextField(10);
        valor = new JTextField(10);
        btnAgendar = new JButton("Agendar");

        // Adding components to the panel
        schedulingPanel.add(comboServico);
        schedulingPanel.add(comboBarbeiro);
        schedulingPanel.add(new JLabel("Dia:"));
        schedulingPanel.add(dia);
        schedulingPanel.add(new JLabel("Mês:"));
        schedulingPanel.add(mes);
        schedulingPanel.add(new JLabel("Ano:"));
        schedulingPanel.add(ano);
        schedulingPanel.add(new JLabel("Valor:"));
        schedulingPanel.add(valor);
        schedulingPanel.add(btnAgendar);

        // Exemplo de adição de serviços ao combobox (pode ser substituído por dados do banco)
        comboServico.addItem("Serviço 1");
        comboServico.addItem("Serviço 2");
        comboServico.addItem("Serviço 3");

        // Add action listener to comboServico
        comboServico.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedService = (String) comboServico.getSelectedItem();
                fetchServiceValue(selectedService);
            }
        });

        // Add action listener to agendar button
        btnAgendar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerAppointment();
            }
        });

        setVisible(true);
    }

    private void fetchServiceValue(String serviceName) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT valor FROM servicos WHERE nome = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, serviceName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String valorServico = rs.getString("valor");
                valor.setText(valorServico);
            } else {
                valor.setText("Serviço não encontrado");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            valor.setText("Erro ao acessar o banco de dados");
        }
    }

    private void registerAppointment() {
        String dayText = dia.getText();
        String monthText = mes.getText();
        String yearText = ano.getText();
        String service = (String) comboServico.getSelectedItem();
        String barber = (String) comboBarbeiro.getSelectedItem();
        String price = valor.getText();

        // Validate the fields
        if (dayText.isEmpty() || monthText.isEmpty() || yearText.isEmpty() || service == null || barber == null || price.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.");
            return;
        }

        try {
            int day = Integer.parseInt(dayText);
            int month = Integer.parseInt(monthText);
            int year = Integer.parseInt(yearText);

            // Validate the date
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateFormat.setLenient(false);
            dateFormat.parse(dayText + "/" + monthText + "/" + yearText);

            // Insert the appointment into the database
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO agendamentos (servico, barbeiro, dia, mes, ano, valor, usuario_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, service);
                stmt.setString(2, barber);
                stmt.setInt(3, day);
                stmt.setInt(4, month);
                stmt.setInt(5, year);
                stmt.setString(6, price);
                stmt.setInt(7, getCurrentUserId()); // Assumindo que você tem um método para obter o ID do usuário logado

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Agendamento realizado com sucesso.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao acessar o banco de dados.");
            }
        } catch (NumberFormatException | ParseException ex) {
            JOptionPane.showMessageDialog(this, "Data inválida.");
        }
    }

    private int getCurrentUserId() {
        // Implementação para obter o ID do usuário logado
        // Esta é uma implementação de exemplo. Adapte conforme necessário.
        return 1; // Retorne o ID do usuário logado
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Scheduling();
            }
        });
    }
}
