import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::showMainMenu);
    }

    public static void showMainMenu() {

        
        JFrame frame = new JFrame("Command Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setResizable(false);  
        frame.setLocationRelativeTo(null);  

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(220, 220, 240));  

        JButton executeButton = new JButton("Exécutez Commande");
        JButton manageButton = new JButton("Gérez Commande");

        executeButton.setPreferredSize(new Dimension(200, 50));
        manageButton.setPreferredSize(new Dimension(200, 50));

        executeButton.setBackground(new Color(102, 205, 170));
        manageButton.setBackground(new Color(70, 130, 180)); 
        executeButton.setForeground(Color.WHITE);  
        manageButton.setForeground(Color.WHITE);

        executeButton.setFont(new Font("Arial", Font.BOLD, 14)); 
        manageButton.setFont(new Font("Arial", Font.BOLD, 14));

        executeButton.addActionListener(e -> {
        	new CommandExecutorFrame();
        });

        manageButton.addActionListener(e -> {
        	new ManageFrame();
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);  
        gbc.gridx = 0;  
        gbc.gridy = 0;  
        panel.add(executeButton, gbc);

        gbc.gridy = 1;  
        panel.add(manageButton, gbc);

        frame.add(panel);
        frame.setVisible(true);  
    }
}
