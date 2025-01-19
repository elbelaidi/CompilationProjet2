import javax.swing.*;
import java.awt.*;
import java.util.regex.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class CommandExecutorFrame extends JFrame {

    public CommandExecutorFrame() {
        setTitle("Command Executor");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 245, 245));

        JLabel headerLabel = new JLabel("Entrez une commande à exécuter:");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setForeground(new Color(50, 50, 50));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        JTextField commandInput = new JTextField(20);
        commandInput.setFont(new Font("Arial", Font.PLAIN, 14));
        commandInput.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        mainPanel.add(commandInput, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton executeButton = new JButton("Exécuter");
        executeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        executeButton.setBackground(new Color(34, 139, 34));
        executeButton.setForeground(Color.WHITE);
        executeButton.setFocusPainted(false);
        executeButton.setPreferredSize(new Dimension(120, 40));

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 14));
        exitButton.setBackground(new Color(220, 50, 50));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setPreferredSize(new Dimension(120, 40));

        buttonPanel.add(executeButton);
        buttonPanel.add(exitButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        executeButton.addActionListener(e -> {
            String command = commandInput.getText();
            if (command != null && !command.trim().isEmpty()) {
                boolean success = executeCommand(command);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Commande exécutée avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "La commande est invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez entrer une commande.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        exitButton.addActionListener(e -> System.exit(0));

        add(mainPanel);
        setVisible(true);
    }

    private boolean executeCommand(String command) {
        if (isValidCommand(command)) {
            System.out.println("Command Executed: " + command);
            return true;
        } else {
            System.out.println("Invalid Command: " + command);
            return false;
        }
    }

    private boolean isValidCommand(String command) {
        List<String> actions = getActionsFromDatabase();
        List<String> appareils = getAppareilsFromDatabase();
        List<String> lieux = getLieuxFromDatabase();

        String actionPattern = String.join("|", actions);  
        String appareilPattern = String.join("|", appareils); 
        String lieuPattern = String.join("|", lieux);     


        String grammar = String.format("^(%s)\\s+(%s)\\s+(%s)\\s*(à\\s\\d{2}:\\d{2})?\\s*$", 
                                      actionPattern, appareilPattern, lieuPattern);

        Pattern pattern = Pattern.compile(grammar, Pattern.CASE_INSENSITIVE);  
        Matcher matcher = pattern.matcher(command);

        return matcher.matches();
    }


    private List<String> getActionsFromDatabase() {
    	 String url = "jdbc:mysql://localhost:3306/domotique?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
         String username = "root";
         String password = "";
        List<String> actions = new ArrayList<>();
        String query = "SELECT nom FROM actions"; 
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                actions.add(rs.getString("nom"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actions;
    }

    private List<String> getAppareilsFromDatabase() {
    	 String url = "jdbc:mysql://localhost:3306/domotique?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
         String username = "root";
         String password = "";
        List<String> appareils = new ArrayList<>();
        String query = "SELECT nom FROM appareils";  
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                appareils.add(rs.getString("nom"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appareils;
    }

    private List<String> getLieuxFromDatabase() {
    	 String url = "jdbc:mysql://localhost:3306/domotique?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
         String username = "root";
         String password = "";
        // Retrieve available lieux from the database
        List<String> lieux = new ArrayList<>();
        // Database query to fetch lieux
        String query = "SELECT nom FROM lieux";  
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                lieux.add(rs.getString("nom"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lieux;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(CommandExecutorFrame::new);
    }
}
