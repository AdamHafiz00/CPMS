class SigninGUI extends javax.swing.JFrame {

    private javax.swing.JButton signinButton;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JTextField usernameField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JPasswordField passwordField;

    public SigninGUI() {
        initComponents();
    }

    private void initComponents() {

        usernameLabel = new javax.swing.JLabel("Username:");
        usernameField = new javax.swing.JTextField(20);
        passwordLabel = new javax.swing.JLabel("Password:");
        passwordField = new javax.swing.JPasswordField(20);
        signinButton = new javax.swing.JButton("Sign In");


        setLayout(new java.awt.GridLayout(3, 2));           // Set layout manager to GridLayout for better alignment
        add(usernameLabel);       // Add components to the frame                    
        add(usernameField);         
        add(passwordLabel);
        add(passwordField);
        add(signinButton);

        setTitle("Sign In");
        setSize(300, 150);
        setLocationRelativeTo(null); // Center the window

        setResizable(false);
        setVisible(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        signinButton.addActionListener(new java.awt.event.ActionListener() {                        // Add action listener to the sign-in button
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {                           // Handle sign-in button click
                String username = usernameField.getText();                                          // Get the username from the text field
                String password = new String(passwordField.getPassword());                          // Get the password from the password field
                boolean authenticated = false;                                                      // Initialize authentication status         
                try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("admin.txt"))) {     // Read the admin data from the file
                    String line;                                                                    // Initialize a variable to hold each line read from the file
                    while ((line = br.readLine()) != null) {                                        // Read each line until the end of the file
                        String[] parts = line.split(",");                                     // Split the line into parts using comma as a delimiter
                        if (parts.length == 6) {                                                    // Check if the line has the expected number of parts                   
                            String fileUsername = parts[4];                                         // Get the username from the file
                            String filePassword = parts[5];                                         // Get the password from the file                
                            if (username.equals(fileUsername) && password.equals(filePassword)) {   // Compare the input username and password with the file data
                                authenticated = true;                                               // If they match, set authenticated to true                    
                                break;                                                              // Exit the loop as we found a match                            
                            }
                        }
                    }
                } catch (Exception e) {                                                             // Handle any exceptions that occur while reading the file
                    javax.swing.JOptionPane.showMessageDialog(SigninGUI.this, "Error reading admin data.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (authenticated) {                                                                // If authenticated is true, show success message and open AdminMainMenu                    
                    javax.swing.JOptionPane.showMessageDialog(SigninGUI.this, "Sign in successful!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    // Open AdminMainMenu and close this window
                    new AdminMainMenu();
                    dispose();
                } else {
                    javax.swing.JOptionPane.showMessageDialog(SigninGUI.this, "Invalid username or password.", "Sign In Failed", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        pack();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }
public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {                     // Create a new thread to run the GUI 
        public void run() {
            new SigninGUI();
        }
    });
}

}
