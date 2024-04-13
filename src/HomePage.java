import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JFrame {
    private JPanel homePanel;
    private JButton agendamentoButton;
    private JButton lojaButton;
    private JButton btnSair;

    public HomePage() {
        setTitle("HomePage");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);
        setContentPane(homePanel);
        setVisible(false);

        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}