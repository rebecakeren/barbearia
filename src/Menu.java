import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Menu extends JFrame {
    private JPanel menuPanel;
    private JLabel menuLabel;

    private String userType;

    public Menu() {
        setTitle("Barbearia do Careca");
        setContentPane(getMenuPanel());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    // Método set privado
    void setUserType(String userType) {
        this.userType = userType;
        configureMenu();
    }

    // Métodos Get e Set privados
    private String getUserType() {
        return userType;
    }

    private JPanel getMenuPanel() {
        return menuPanel;
    }

    private JLabel getMenuLabel() {
        return menuLabel;
    }

    private void configureMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItemAgendamento = new JMenuItem("Agendamento");
        JMenuItem menuItemServico = new JMenuItem("Serviço");
        JMenuItem menuItemSair = new JMenuItem("Sair");

        popupMenu.add(menuItemAgendamento);
        if ("BARBEIRO".equalsIgnoreCase(getUserType())) {
            popupMenu.add(menuItemServico);
        }
        popupMenu.add(menuItemSair);

        menuItemAgendamento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Scheduling schedulingDialog = new Scheduling();
                schedulingDialog.setVisible(true);
            }
        });

        menuItemServico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Service serviceDialog = new Service(null);
                serviceDialog.setVisible(true);
            }
        });

        menuItemSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        getMenuLabel().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                popupMenu.show(getMenuLabel(), e.getX(), e.getY());
            }
        });
    }
}
