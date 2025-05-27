import javax.swing.*;

public class AdminMainMenu extends JFrame {

    public AdminMainMenu() {
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
            new PatientGUI().setVisible(true);
        });
        doctorMenuItem.addActionListener(e -> {
            dispose();
            new DoctorGUI().setVisible(true);
        });
        adminMenuItem.addActionListener(e -> {
            dispose();
            new AdminGUI().setVisible(true);
        });


        manageMenu.add(patientMenuItem);
        manageMenu.add(doctorMenuItem);
        manageMenu.add(adminMenuItem);

        menuBar.add(manageMenu);
        setJMenuBar(menuBar);

        JLabel welcome = new JLabel("Welcome Admin! Use the menu to manage records.", JLabel.CENTER);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminMainMenu::new);
    }
}
