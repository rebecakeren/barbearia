import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Menu extends JFrame {
    private JPanel menuPanel;
    private JLabel menu;
    private String userType;

    public Menu() {
        setTitle("Barbearia do Careca");
        setContentPane(menuPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1920, 1080);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public void setUserType(String userType) {
        this.userType = userType;
        configureMenu();
    }

    private void configureMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItemAgendamento = new JMenuItem("Agendamento");
        JMenuItem menuItemServico = new JMenuItem("Serviço");
        JMenuItem menuItemSair = new JMenuItem("Sair");

        popupMenu.add(menuItemAgendamento);
        if ("BARBEIRO".equalsIgnoreCase(userType)) {
            popupMenu.add(menuItemServico);
        }
        popupMenu.add(menuItemSair);

        menuItemAgendamento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Fecha a janela atual
                new Scheduling();
            }
        });

        menuItemServico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Service(null);
            }
        });

        menuItemSair.addActionListener(new ActionListener() {
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
    }

    public static void main(String[] args) {
        new Menu();
    }
}
