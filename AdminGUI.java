import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class AdminGUI extends JFrame{
    
    private JTextField  txtId,txtName,txtAge,txtSearchId;
    private JPasswordField txtPassword;
    private JTable table;
    private DefaultTableModel tableModel;
    private AdminManager adminManager;

    public AdminGUI() {
        adminManager = new AdminManager();
        initComponents();
        loadTableData();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void initComponents() {
        setTitle("Admin Management System");
        setSize(800, 600);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridBagLayout());                                // Creating a panel for input fields
        inputPanel.setBorder(BorderFactory.createTitledBorder("Admin Details"));      // Setting a titled border for the input panel
        GridBagConstraints gbc = new GridBagConstraints();                                  // Creating a GridBagConstraints object for layout management
        gbc.insets = new Insets(5, 5, 5, 5);                          // Setting insets for the grid bag constraints
        gbc.fill = GridBagConstraints.HORIZONTAL;                                           // Allowing components to fill the horizontal space             


        JLabel lblId = new JLabel("ID:");
        txtId = new JTextField(15);
        txtId.setEditable(false);

        JLabel lblName = new JLabel("Name:");
        txtName = new JTextField(15);

        JLabel lblAge = new JLabel("Age:");
        txtAge = new JTextField(15);

        JLabel lblPassword = new JLabel("Password:");
        txtPassword = new JPasswordField(15);                                       // Creating a password field for password input

        JLabel lblSearchId = new JLabel("Search by ID:");
        txtSearchId = new JTextField(15);

        int row = 0;                                                                        // Initializing row for grid bag layout
                                                                            // Incrementing the row for the next component                              
        row++;
        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(lblId, gbc);
        gbc.gridx = 1; inputPanel.add(txtId, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(lblName, gbc);
        gbc.gridx = 1; inputPanel.add(txtName, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(lblAge, gbc);
        gbc.gridx = 1; inputPanel.add(txtAge, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(lblPassword, gbc);
        gbc.gridx = 1; inputPanel.add(txtPassword, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(lblSearchId, gbc);
        gbc.gridx = 1; inputPanel.add(txtSearchId, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));  // Creating a panel for buttons with centered layout
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnSearch = new JButton("Search");
        JButton btnRefresh = new JButton("Refresh");
        JButton btnBack = new JButton("Back to Main Menu");
        
        buttonPanel.add(btnAdd);                                    // Adding buttons to the button panel           
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnBack);

        tableModel = new DefaultTableModel(new String[]{"No.", "ID", "Name", "Age","Password"}, 0) { // Creating a table model with column names
            public boolean isCellEditable(int row, int column) {                                                   // Making the table cells non-editable
                return false;
            }
        };

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);                                // Creating a scroll pane for the table to allow scrolling if the content exceeds the visible area
        scrollPane.setBorder(BorderFactory.createTitledBorder("Admin List"));     // Setting a titled border for the scroll pane
        add(inputPanel, BorderLayout.NORTH);                                            // Adding the input panel to the north region of the layout
        add(buttonPanel, BorderLayout.CENTER);                                          // Adding the button panel to the center region of the layout
        add(scrollPane, BorderLayout.SOUTH);                                            // Adding the scroll pane to the south region of the layout                                


        btnAdd.addActionListener(e -> addAdmin());                                      // Adding action listener for the Add button to call addAdmin method
        btnUpdate.addActionListener(e -> updateAdmin());                                // Adding action listener for the Update button to call updateAdmin method
        btnDelete.addActionListener(e -> deleteAdmin());                                // Adding action listener for the Delete button to call deleteAdmin method                  
        btnSearch.addActionListener(e -> searchAdmin());                                // Adding action listener for the Search button to call searchAdmin method
        btnRefresh.addActionListener(e -> loadTableData());                             // Adding action listener for the Refresh button to call loadTableData method
        btnBack.addActionListener(e -> {
            dispose();                                                                  // Dispose of the current window
            new AdminMainMenu();                                                        // Open the AdminMainMenu                           
        });

        table.addMouseListener(new MouseAdapter() {                                     // Adding a mouse listener to the table to handle row selection
            public void mouseClicked(MouseEvent e) {                                    // When a row is clicked
                int row = table.getSelectedRow();                                       // Get the selected row index   
                if (row >= 0) {                                                         // If a valid row is selected   
                        // Populate the text fields with the selected row data
                    txtId.setText(tableModel.getValueAt(row, 1).toString());        
                    txtName.setText(tableModel.getValueAt(row, 2).toString());
                    txtAge.setText(tableModel.getValueAt(row, 3).toString());
                    txtPassword.setText(tableModel.getValueAt(row, 4).toString());
                  
                    
                }
            }
        });









        pack();                               // Auto-resize based on content
        setResizable(true);         // Allow user to resize
        setLocationRelativeTo(null);        // Center the window on the screen




    }

    private String generateId() {
        // Generate a unique ID for the patient
              try {
            int maxId = 0;
            for (Admin a : adminManager.getAll()) { // Get all patients and find the max ID
                String a_id = a.getId();
                if (a_id != null && a_id.startsWith("A")) { //a_id is not null and starts with "A"
                    try {
                        int num = Integer.parseInt(a_id.substring(1)); //SUBSTRING IS REMOVING THE "A" FROM THE ID AND CONVERTING TO INT, TO FIND THE MAX ID
                        if (num > maxId) {
                            maxId = num;
                        }
                    } catch (NumberFormatException ignore) {}
                }
            }
            int nextId = maxId + 1; // Increment the max ID to get the next ID
            return String.format("A%03d", nextId);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error generating patient ID: " + ex.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
            return "A0";
        }
    }

    

    private void addAdmin() {
        try {
            String id = generateId();                                                // Generating a unique ID for the admin
            String name = txtName.getText().trim();
            String ageText = txtAge.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();

            if (name.isEmpty() || ageText.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int age = Integer.parseInt(ageText);                                    // converting to int for passing to arg Admin Constructor
            Admin admin = new Admin( id, name, age, password);      // Creating Admin object and passing the data to the constructor and assigning to the admin variable
            adminManager.add(admin);                                                // Adding the admin object to the file using the add method from AdminManager class

            JOptionPane.showMessageDialog(this, "Admin added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadTableData();
            clearFields();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error adding admin: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
   private void updateAdmin() {
        try {
                                                        // Collecting data from the text fields
            String id = txtId.getText().trim();                              
            String name = txtName.getText().trim();
            int age = Integer.parseInt(txtAge.getText().trim());
            String password = new String(txtPassword.getPassword()).trim();

            Admin admin = new Admin(id, name, age,password);
            adminManager.update(admin);
            JOptionPane.showMessageDialog(this, "Admin updated.");
            clearFields();                                                      // Clearing the text fields after updating
            loadTableData();                                                    // Reloading the table data to reflect the changes
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    private void deleteAdmin() {
        String id = txtId.getText().trim();                                      // Collecting ID from the text field
        
        if (id.isEmpty()) {                                                      // Checking if the ID is empty
            JOptionPane.showMessageDialog(this, "Enter ID to delete.");
            return;
        }
        try {
            adminManager.delete(id);                                            // Deleting the admin using the delete method from AdminManager class
            JOptionPane.showMessageDialog(this, "Admin deleted.");
            clearFields();
            loadTableData();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
        private void searchAdmin() {
        String id = txtSearchId.getText().trim();                                   // Collecting ID from the search text field
        try {
            Admin admin = adminManager.searchById(id);                              // Searching for the admin using the searchById method from AdminManager class
            if (admin == null) {                                                    // If admin is not found, show a message
                JOptionPane.showMessageDialog(this, "Admin not found.");
            } else {                                                                // If admin is found, populate the text fields with the admin data
                txtId.setText(admin.getId());
                txtName.setText(admin.getName());
                txtAge.setText(String.valueOf(admin.getAge()));

                txtPassword.setText(admin.getPassword());
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
        private void loadTableData() {
        try {
            List<Admin> admin = adminManager.getAll();                              // Getting all admins from the file using the getAll method from AdminManager class
            tableModel.setRowCount(0);                                     // Clearing the table model before adding new data   
            for (Admin d : admin) {                                                 // Iterating through the list of admins
                tableModel.addRow(new Object[]{                                     // Adding a new row to the table model with the admin data
                        tableModel.getRowCount(), d.getId(), d.getName(), d.getAge(), d.getPassword()
                });
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clearFields() {
       
        txtId.setText("");
        txtName.setText("");
        txtAge.setText("");
        txtPassword.setText("");
        txtSearchId.setText("");
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminGUI().setVisible(true));    // Launch the Admin GUI
        
    }


    
}
