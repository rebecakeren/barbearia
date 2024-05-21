import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Menu extends JFrame {
    private JPanel menuPanel;
    private JLabel menuLabel;

    public Menu() {
        setTitle("Barbearia do Careca");
        setContentPane(menuPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1920, 1080);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);


        // Configura o menu pop-up
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem1 = new JMenuItem("Agendamento");
        JMenuItem menuItem2 = new JMenuItem("Sair");

        popupMenu.add(menuItem1);
        popupMenu.add(menuItem2);

        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Scheduling();
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
    }

    public static void main(String[] args) {
        new Menu();
    }
}
