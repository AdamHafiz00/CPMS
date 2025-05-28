import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PatientStatusGUI extends JFrame {
    private JTextField txtId, txtName, txtAge, txtDiseases, txtSearchId;
    
    private JTable table;
    private DefaultTableModel tableModel;
    private PatientManager patientManager;
    private JComboBox<String> cmbDoctor;
    private JComboBox<String> cmbStatus;
    private final String userId;
    public PatientStatusGUI(String userId) {
        this.userId = userId;
        patientManager = new PatientManager();
        initComponents();
        loadTableData();
    }

    private void initComponents() {
        setTitle("Patient Status Management");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        cmbDoctor = new JComboBox<>();
        cmbStatus = new JComboBox<>(new String[] {"Appointment Scheduled", "Completed"});
        
        // Panel for input fields
        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 5, 5));


    inputPanel.add(new JLabel("ID:"));
    txtId = new JTextField();
    txtId.setEditable(false);
    inputPanel.add(txtId);

    inputPanel.add(new JLabel("Name:"));
    txtName = new JTextField();
    txtName.setEditable(false);
    inputPanel.add(txtName);

    inputPanel.add(new JLabel("Age:"));
    txtAge = new JTextField();
    txtAge.setEditable(false);
    inputPanel.add(txtAge);

    inputPanel.add(new JLabel("Diseases:"));
    txtDiseases = new JTextField();
    inputPanel.add(txtDiseases);

    inputPanel.add(new JLabel("Assigned Doctor:"));
    cmbDoctor.setEditable(false); // Make it non-editable
    inputPanel.add(cmbDoctor); 

    inputPanel.add(new JLabel("Status:"));
    inputPanel.add(cmbStatus); 

    inputPanel.add(new JLabel("Search by ID:"));
    txtSearchId = new JTextField();
    inputPanel.add(txtSearchId);

        // Add a menu item for "Sign Out"
        // Add Sign Out menu to the menu bar on the right
        JMenuBar menuBar = new JMenuBar();
        JMenu signOutMenu = new JMenu("Sign Out");
        signOutMenu.addMenuListener(new javax.swing.event.MenuListener() {
        @Override
        public void menuSelected(javax.swing.event.MenuEvent e) {
            dispose();
            new SigninGUI();
            }
            @Override
            public void menuDeselected(javax.swing.event.MenuEvent e) {}
            @Override
            public void menuCanceled(javax.swing.event.MenuEvent e) {}
        });
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(signOutMenu);
        setJMenuBar(menuBar);// Add the input panel to the frame 




        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 6, 5, 5));
        JButton btnUpdate = new JButton("Update");
        JButton btnSearch = new JButton("Search");
        JButton btnRefresh = new JButton("Refresh");


        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnRefresh);


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
        btnUpdate.addActionListener(e -> updatePatient());
        btnSearch.addActionListener(e -> searchPatient());
        btnRefresh.addActionListener(e -> loadTableData());loadTableData();


        // When table row clicked, populate input fields
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
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

    
    public String docName; 

    private void loadDoctors() { //LOAD DOCTOR FOR ASSIGNATION
        cmbDoctor.removeAllItems();
        try {
            List<Doctor> doctors = new DoctorManager().getAll(); 
            for (Doctor doc : doctors) {
                if (doc.getId().equals(this.userId)){
                    cmbDoctor.addItem(doc.getName());
                    docName = doc.getName();
                }
                
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading doctors: " + ex.getMessage());
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
            JOptionPane.showMessageDialog(this, "Age must be integers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error updating patient: " + ex.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
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
                if (p.getAssignedDoctor().equals(docName)) {
                    tableModel.addRow(new Object[]{
                        tableModel.getRowCount() +1, p.getId(), p.getName(), p.getAge(), p.getDiseases(), p.getAssignedDoctor(), p.getStatus()
                    });
                }
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
