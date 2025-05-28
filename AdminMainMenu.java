import javax.swing.*;

public class AdminMainMenu extends JFrame {

    public AdminMainMenu(String UserId) {
        super("Admin Control Panel");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();

        JMenu manageMenu = new JMenu("Manage");

        JMenuItem patientMenuItem = new JMenuItem("Patient");
        JMenuItem doctorMenuItem = new JMenuItem("Doctor");
        JMenuItem adminMenuItem = new JMenuItem("Admin");

        patientMenuItem.addActionListener(e -> {
            dispose();
            new PatientGUI(UserId).setVisible(true);
        });
        doctorMenuItem.addActionListener(e -> {
            dispose();
            new DoctorGUI(UserId).setVisible(true);
        });
        adminMenuItem.addActionListener(e -> {
            dispose();
            new AdminGUI(UserId).setVisible(true);
        });

         

        manageMenu.add(patientMenuItem);
        manageMenu.add(doctorMenuItem);

        AdminManager adminManager = new AdminManager();
        String level,UserName = "";
        try {   
            Admin admin = adminManager.searchById(UserId);
            level = admin.getLevel();
            UserName = admin.getName(); // Get the name of the admin
             // Get the level of the admin
        } catch (java.io.IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading admin data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            level = "";
        }

        if (level.equals("1")) { // Assuming level "1" is for SuperAdmin
            adminMenuItem.setEnabled(true);
            doctorMenuItem.setEnabled(true);
        } else {
            adminMenuItem.setEnabled(false);
            doctorMenuItem.setEnabled(false);
        }   
        manageMenu.add(adminMenuItem);

        menuBar.add(manageMenu);
        setJMenuBar(menuBar);

        JLabel welcome = new JLabel("Welcome "+ UserName +" Use the menu to manage records.", JLabel.CENTER);
        add(welcome);

        // Add Sign Out menu to the menu bar on the right
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

        setVisible(true);
    }

}
