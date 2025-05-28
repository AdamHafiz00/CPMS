import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PatientGUI extends JFrame {
    private JTextField txtId, txtName, txtAge, txtDiseases, txtSearchId;
    
    private JTable table;
    private DefaultTableModel tableModel;
    private PatientManager patientManager; 
    private JComboBox<String> cmbDoctor;
    private JComboBox<String> cmbStatus;
    private String UserId; // Store the level as a field

    public PatientGUI(String UserId) {
        this.UserId = UserId; // Assign constructor parameter to field
        patientManager = new PatientManager();
        initComponents();
        loadTableData();
    }

    private void initComponents() {
        setTitle("Patient Manager");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        cmbDoctor = new JComboBox<>();
        cmbStatus = new JComboBox<>(new String[] {"New Patient", "Appointment Scheduled", "Completed"});

        // Panel for input fields
        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 5, 5));






    inputPanel.add(new JLabel("ID:"));
    txtId = new JTextField();
    txtId.setEditable(false);
    inputPanel.add(txtId);

    inputPanel.add(new JLabel("Name:"));
    txtName = new JTextField();
    inputPanel.add(txtName);

    inputPanel.add(new JLabel("Age:"));
    txtAge = new JTextField();
    inputPanel.add(txtAge);

    inputPanel.add(new JLabel("Diseases:"));
    txtDiseases = new JTextField();
    inputPanel.add(txtDiseases);

    inputPanel.add(new JLabel("Assigned Doctor:"));
    inputPanel.add(cmbDoctor); 

    inputPanel.add(new JLabel("Status:"));
    inputPanel.add(cmbStatus); 

    inputPanel.add(new JLabel("Search by ID:"));
    txtSearchId = new JTextField();
    inputPanel.add(txtSearchId);



        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 6, 5, 5));

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

        // Table
        tableModel = new DefaultTableModel(new String[]{"No.", "ID", "Name", "Age", "Diseases", "Doctor", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only table
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Add components to frame
        setLayout(new BorderLayout(10, 10));
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Button actions
        btnAdd.addActionListener(e -> addPatient());
        btnUpdate.addActionListener(e -> updatePatient());
        btnDelete.addActionListener(e -> deletePatient());
        btnSearch.addActionListener(e -> searchPatient());
        btnRefresh.addActionListener(e -> {clearFields();loadTableData();     // Reload the table data
});
        btnBack.addActionListener(e -> {
            dispose();
            new AdminMainMenu(this.UserId);
        });

        // When table row clicked, populate input fields
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                   // wait
                    txtId.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    txtName.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    txtAge.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    txtDiseases.setText(tableModel.getValueAt(selectedRow, 4).toString());
                    cmbDoctor.setSelectedItem(tableModel.getValueAt(selectedRow, 5).toString());
                    cmbStatus.setSelectedItem(tableModel.getValueAt(selectedRow, 6).toString());

                }
            }
        });
        loadDoctors(); 
        pack();                  // Auto-resize based on content
        setResizable(true);      // Allow user to resize
        setLocationRelativeTo(null); 
        
    }


    private void loadDoctors() { //LOAD DOCTOR FOR ASSIGNATION
        cmbDoctor.removeAllItems();
        try {
            List<Doctor> doctors = new DoctorManager().getAll(); 
            for (Doctor doc : doctors) {
                cmbDoctor.addItem(doc.getName());
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading doctors: " + ex.getMessage());
        }
    }

    private String generateId() {
        // Generate a unique ID for the patient
              try {
            int maxId = 0;
            for (Patient p : patientManager.getAll()) { // Get all patients and find the max ID
                String pid = p.getId();
                if (pid != null && pid.startsWith("P")) { //pid is not null and starts with "P"
                    try {
                        int num = Integer.parseInt(pid.substring(1)); //SUBSTRING IS REMOVING THE "P" FROM THE ID AND CONVERTING TO INT, TO FIND THE MAX ID
                        if (num > maxId) {
                            maxId = num;
                        }
                    } catch (NumberFormatException ignore) {}
                }
            }
            int nextId = maxId + 1; // Increment the max ID to get the next ID
            return String.format("P%03d", nextId);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error generating patient ID: " + ex.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
            return "P0";
        }
    }
    private void addPatient() {
        try {
            String id = generateId(); // Generate a new ID
            String name = txtName.getText().trim();
            String ageText = txtAge.getText().trim();
            String diseases = txtDiseases.getText().trim();
            String doctor = (String) cmbDoctor.getSelectedItem();
            String status = (String) cmbStatus.getSelectedItem();
            

            if (ageText.isEmpty() || name.isEmpty() || diseases.isEmpty()) {
                JOptionPane.showMessageDialog(this, "age, Name and Diseases cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int age = Integer.parseInt(ageText); //converting to int for passing to arg Patient Constructor
            Patient patient = new Patient( id, name, age, diseases, doctor, status);
            patientManager.add(patient);
            JOptionPane.showMessageDialog(this, "Patient added successfully!");
            clearFields();
            loadTableData();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Count and Age must be integers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving patient: " + ex.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePatient() {
        try {
            String id = txtId.getText().trim();
            String name = txtName.getText().trim();
            int age = Integer.parseInt(txtAge.getText().trim());
            String diseases = txtDiseases.getText().trim();
            String doctor = (String) cmbDoctor.getSelectedItem();
            String status = (String) cmbStatus.getSelectedItem();

            if (id.isEmpty() || name.isEmpty() || diseases.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID, Name and Diseases cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Patient patient = new Patient(id, name, age, diseases, doctor, status);

            patientManager.update(patient);
            JOptionPane.showMessageDialog(this, "Patient updated successfully!");
            clearFields();
            loadTableData();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Count and Age must be integers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error updating patient: " + ex.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePatient() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an ID to delete.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            patientManager.delete(id);
            JOptionPane.showMessageDialog(this, "Patient deleted successfully!");
            clearFields();
            loadTableData();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting patient: " + ex.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchPatient() {
        String id = txtSearchId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an ID to search.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Patient patient = patientManager.searchById(id);
            if (patient == null) {
                JOptionPane.showMessageDialog(this, "Patient not found.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // populate fields
                txtId.setText(patient.getId());
                txtName.setText(patient.getName());
                txtAge.setText(String.valueOf(patient.getAge()));
                txtDiseases.setText(patient.getDiseases());
                cmbDoctor.setSelectedItem(patient.getAssignedDoctor());
                cmbStatus.setSelectedItem(patient.getStatus());
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error searching patient: " + ex.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTableData() {
               try {
            List<Patient> patients = patientManager.getAll();
            tableModel.setRowCount(0); // clear table
            for (Patient p : patients) {
                tableModel.addRow(new Object[]{
                        tableModel.getRowCount() + 1, p.getId(), p.getName(), p.getAge(), p.getDiseases(), p.getAssignedDoctor(), p.getStatus()
                });
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading patients: " + ex.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {

        txtId.setText("");
        txtName.setText("");
        txtAge.setText("");
        txtDiseases.setText("");
        txtSearchId.setText("");
        if (cmbDoctor.getItemCount() > 0) {
    cmbDoctor.setSelectedIndex(0); // or any default index
}
    }


}