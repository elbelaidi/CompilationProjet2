import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class ManageFrame extends JFrame {
    private Connection connection;
    private JList<String> recordList;
    private JComboBox<String> tableSelector;

    public ManageFrame() {
        
        setTitle("Gestion des Tables");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel headerLabel = new JLabel("Gestion des Commandes");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(10, 10));

        String[] tables = {"actions", "lieux", "appareils"};
        tableSelector = new JComboBox<>(tables);
        tableSelector.setFont(new Font("Arial", Font.PLAIN, 16)); 
        tableSelector.setPreferredSize(new Dimension(200, 30));   
        tableSelector.setMaximumSize(tableSelector.getPreferredSize()); 
        tableSelector.setAlignmentX(Component.LEFT_ALIGNMENT);   
        tableSelector.setSelectedItem("actions");
        tableSelector.addActionListener(e -> updateRecordList((String) tableSelector.getSelectedItem()));

        JPanel selectorPanel = new JPanel();
        selectorPanel.setLayout(new BoxLayout(selectorPanel, BoxLayout.Y_AXIS));
        selectorPanel.setBorder(new EmptyBorder(10, 0, 10, 0)); 
        selectorPanel.add(new JLabel("Sélectionnez une table:"));
        selectorPanel.add(Box.createVerticalStrut(5)); 
        selectorPanel.add(tableSelector);

        centerPanel.add(selectorPanel, BorderLayout.NORTH);


        recordList = new JList<>();
        recordList.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(recordList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Données de la Table"));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4, 10, 10));
        Color addColor = new Color(76, 175, 80);   
        Color deleteColor = new Color(244, 67, 54); 
        Color editColor = new Color(255, 193, 7);   
        Color backColor = new Color(33, 150, 243);  

        JButton addButton = createStyledButton("Ajouter");
        addButton.setBackground(addColor);
        addButton.setForeground(Color.WHITE); 

        JButton deleteButton = createStyledButton("Supprimer");
        deleteButton.setBackground(deleteColor);
        deleteButton.setForeground(Color.WHITE); 

        JButton editButton = createStyledButton("Modifier");
        editButton.setBackground(editColor);
        editButton.setForeground(Color.WHITE); 

        JButton backButton = createStyledButton("Exit");
        backButton.setBackground(backColor);
        backButton.setForeground(Color.WHITE); 


        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(backButton);

        backButton.addActionListener(e -> {
            this.dispose();
             
        });

        addButton.addActionListener(e -> ajouterRecord((String) tableSelector.getSelectedItem()));
        deleteButton.addActionListener(e -> supprimerRecord((String) tableSelector.getSelectedItem()));
        editButton.addActionListener(e -> modifierRecord((String) tableSelector.getSelectedItem()));


        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        updateRecordList("actions");

        add(mainPanel);
        setVisible(true);
    }
    private void ajouterRecord(String tableName) {
        String newValue = JOptionPane.showInputDialog(this, "Entrez la nouvelle valeur:", "Ajouter", JOptionPane.PLAIN_MESSAGE);
        if (newValue != null && !newValue.trim().isEmpty()) {
            try {
                connectToDatabase();
                String query = "INSERT INTO " + tableName + " (nom) VALUES (?)"; 
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, newValue);
                statement.executeUpdate();
                statement.close();
                connection.close();
                JOptionPane.showMessageDialog(this, "Enregistrement ajouté avec succès!");
                updateRecordList(tableName);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Valeur non valide.");
        }
    }

    private void supprimerRecord(String tableName) {
        String selectedValue = recordList.getSelectedValue();
        if (selectedValue != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer '" + selectedValue + "' ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    connectToDatabase();
                    String query = "DELETE FROM " + tableName + " WHERE nom = ?"; 
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, selectedValue);
                    statement.executeUpdate();
                    statement.close();
                    connection.close();
                    JOptionPane.showMessageDialog(this, "Enregistrement supprimé avec succès!");
                    updateRecordList(tableName); 
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un enregistrement à supprimer.");
        }
    }

    private void modifierRecord(String tableName) {
        String selectedValue = recordList.getSelectedValue();
        if (selectedValue != null) {
            String newValue = JOptionPane.showInputDialog(this, "Entrez la nouvelle valeur pour '" + selectedValue + "':", "Modifier", JOptionPane.PLAIN_MESSAGE);
            if (newValue != null && !newValue.trim().isEmpty()) {
                try {
                    connectToDatabase();
                    String query = "UPDATE " + tableName + " SET nom = ? WHERE nom = ?"; 
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, newValue);
                    statement.setString(2, selectedValue);
                    statement.executeUpdate();
                    statement.close();
                    connection.close();
                    JOptionPane.showMessageDialog(this, "Enregistrement modifié avec succès!");
                    updateRecordList(tableName); 
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la modification : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Valeur non valide.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un enregistrement à modifier.");
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(120, 40));
        return button;
    }

    private void updateRecordList(String tableName) {
        try {	
            ArrayList<String> records = getTableRecords(tableName);
            recordList.setListData(records.toArray(new String[0]));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des données : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ArrayList<String> getTableRecords(String tableName) throws SQLException {
        ArrayList<String> records = new ArrayList<>();
        connectToDatabase();

        String query = "SELECT * FROM " + tableName;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            records.add(resultSet.getString(2)); 
        }

        resultSet.close();
        statement.close();
        connection.close();
        return records;
    }

    private void connectToDatabase() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/domotique?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String username = "root";
        String password = "";
        connection = DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ManageFrame::new);
        
    }
}
