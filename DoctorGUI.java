import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class DoctorGUI extends JFrame {
    private JTextField txtCount, txtId, txtName, txtAge, txtSearchId;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbSpecialist;
    private JTable table;
    private DefaultTableModel tableModel;
    private DoctorManager doctorManager;
    private String UserId; // Variable to store user type (Admin or Doctor)
    public DoctorGUI(String UserId) {
        this.UserId = UserId; // Store the user level
        doctorManager = new DoctorManager();
        initComponents();
        loadTableData();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Doctor Management System");
        setSize(800, 600);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(new TitledBorder("Doctor Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblCount = new JLabel("Count:");
        txtCount = new JTextField();
        txtCount.setEditable(false);

        JLabel lblId = new JLabel("ID:");
        txtId = new JTextField();
        txtId.setEditable(false);

        JLabel lblPassword = new JLabel("Password:");
        txtPassword = new JPasswordField(); 
        

        JLabel lblName = new JLabel("Name:");
        txtName = new JTextField();

        JLabel lblAge = new JLabel("Age:");
        txtAge = new JTextField();

        JLabel lblSpecialist = new JLabel("Specialist:");
        String[] specialists = {"Cardiology", "Neurology", "Orthopedics", "Pediatrics", "General"};
        cmbSpecialist = new JComboBox<>(specialists);

        JLabel lblSearchId = new JLabel("Search by ID:");
        txtSearchId = new JTextField();

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(lblCount, gbc);
        gbc.gridx = 1; inputPanel.add(txtCount, gbc);
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
        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(lblSpecialist, gbc);
        gbc.gridx = 1; inputPanel.add(cmbSpecialist, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(lblPassword, gbc);
        gbc.gridx = 1; inputPanel.add(txtPassword, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(lblSearchId, gbc);
        gbc.gridx = 1; inputPanel.add(txtSearchId, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnSearch = new JButton("Search");
        JButton btnRefresh = new JButton("Refresh");
        JButton btnBack = new JButton("Back to Main Menu");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnBack);

        tableModel = new DefaultTableModel(new String[]{"No.", "ID", "Name", "Age", "Specialist","Password"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new TitledBorder("Doctor Records"));

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addDoctor());
        btnUpdate.addActionListener(e -> updateDoctor());
        btnDelete.addActionListener(e -> deleteDoctor());
        btnSearch.addActionListener(e -> searchDoctor());
        btnRefresh.addActionListener(e -> loadTableData());
        btnBack.addActionListener(e -> {
            dispose();
            new AdminMainMenu(this.UserId);
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtCount.setText(tableModel.getValueAt(row, 0).toString());
                    txtId.setText(tableModel.getValueAt(row, 1).toString());
                    txtName.setText(tableModel.getValueAt(row, 2).toString());
                    txtAge.setText(tableModel.getValueAt(row, 3).toString());
                    cmbSpecialist.setSelectedItem(tableModel.getValueAt(row, 4).toString());
                    txtPassword.setText(tableModel.getValueAt(row, 5).toString()); 
                }
            }
        });
        pack();                  // Auto-resize based on content
        setResizable(true);      // Allow user to resize
        setLocationRelativeTo(null); 
    }

    private String generateId() {
        // Generate a unique ID for the patient
              try {
            int maxId = 0;
            for (Doctor d : doctorManager.getAll()) { // Get all patients and find the max ID
                String d_id = d.getId();
                if (d_id != null && d_id.startsWith("D")) { //d_id is not null and starts with "D"
                    try {
                        int num = Integer.parseInt(d_id.substring(1)); //SUBSTRING IS REMOVING THE "P" FROM THE ID AND CONVERTING TO INT, TO FIND THE MAX ID
                        if (num > maxId) {
                            maxId = num;
                        }
                    } catch (NumberFormatException ignore) {}
                }
            }
            int nextId = maxId + 1; // Increment the max ID to get the next ID
            return String.format("D%03d", nextId);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error generating patient ID: " + ex.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
            return "D0";
        }
    }

    private void addDoctor() {
        try {

            String id = generateId();  
            String name = txtName.getText().trim();
            String ageText = txtAge.getText().trim();
            String specialist = (String) cmbSpecialist.getSelectedItem();
            String password = new String(txtPassword.getPassword()).trim(); // Get password from JPasswordField

            if (ageText.isEmpty() || name.isEmpty() || specialist.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID, Name and Specialist cannot be empty.");
                return;
            }
            int age = Integer.parseInt(ageText); //converting to int for passing to arg Doctor Constructor

            Doctor doctor = new Doctor( id, name, age, specialist,password);
            doctorManager.add(doctor);
            JOptionPane.showMessageDialog(this, "Doctor added.");
            clearFields();
            loadTableData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateDoctor() {
        try {
           
            String id = txtId.getText().trim();
            String name = txtName.getText().trim();
            int age = Integer.parseInt(txtAge.getText().trim());
            String specialist = (String) cmbSpecialist.getSelectedItem();
            String password = new String(txtPassword.getPassword()).trim(); // Get password from JPasswordField

            Doctor doctor = new Doctor(id, name, age, specialist,password);
            doctorManager.update(doctor);
            JOptionPane.showMessageDialog(this, "Doctor updated.");
            clearFields();
            loadTableData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteDoctor() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter ID to delete.");
            return;
        }
        try {
            doctorManager.delete(id);
            JOptionPane.showMessageDialog(this, "Doctor deleted.");
            clearFields();
            loadTableData();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void searchDoctor() {
        String id = txtSearchId.getText().trim();
        try {
            Doctor doctor = doctorManager.searchById(id);
            if (doctor == null) {
                JOptionPane.showMessageDialog(this, "Doctor not found.");
            } else {
                txtId.setText(doctor.getId());
                txtName.setText(doctor.getName());
                txtAge.setText(String.valueOf(doctor.getAge()));
                cmbSpecialist.setSelectedItem(doctor.getSpecialist());
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void loadTableData() {
        try {
            List<Doctor> doctors = doctorManager.getAll();
            tableModel.setRowCount(0);
            for (Doctor d : doctors) {
                tableModel.addRow(new Object[]{
                        tableModel.getRowCount() + 1, d.getId(), d.getName(), d.getAge(), d.getSpecialist(), d.getPassword()
                });
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        txtCount.setText("");
        txtId.setText("");
        txtName.setText("");
        txtAge.setText("");
        cmbSpecialist.setSelectedIndex(0);
        txtSearchId.setText("");
        txtPassword.setText(""); 
    }

    public static void main(String[] args) {

       
    }
}
