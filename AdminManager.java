import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminManager implements PersonOperations<Admin>{

    private static final String FILE_NAME = "admin.txt";

    @Override
    public void add(Admin person) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(person.toString());
            bw.newLine();
        }
    }

    @Override
    public void update(Admin person) throws IOException {                           //receive the updated admin objact from the AdminGUI
        List<Admin> admins = getAll();                                              // retrieve all existing admins from the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {   // open the file for writing and Iterate through the list of admins
            for (Admin a : admins) {                                                // for each admin in the list
                if (a.getId().equals(person.getId())) {                             // if the admin's ID matches the one we want to update
                    bw.write(person.toString());                                    // write the updated admin object to the file
                } else {
                    bw.write(a.toString());                                         // otherwise, write the existing admin object to the file
                }
                bw.newLine();
            }
        }
    }

    @Override
    public void delete(String id) throws IOException {
        List<Admin> admins = getAll();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Admin a : admins) {
                if (!a.getId().equals(id)) {
                    bw.write(a.toString());
                    bw.newLine();
                }
            }
        }
    }

    @Override
    public Admin searchById(String id) throws IOException {
        for (Admin a : getAll()) {
            if (a.getId().equals(id)) {
                return a;
            }
        }
        return null;
    }

    @Override
    public List<Admin> getAll() throws IOException {
        List<Admin> admin = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    int count = Integer.parseInt(parts[0]);
                    String id = parts[1];
                    String name = parts[2];
                    int age = Integer.parseInt(parts[3]);
                    String password = parts[4];
           
                    
                    admin.add(new Admin(count, id, name, age, password));
                }
            }
            
        }
        return admin;
    }
    
}
