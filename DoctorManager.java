
import java.io.*;
import java.util.*;

public class DoctorManager implements PersonOperations<Doctor> {
    private static final String FILE_NAME = "doctor.txt";

    @Override
    public void add(Doctor doctor) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(doctor.toString());
            bw.newLine();
        }
    }

    @Override
    public void update(Doctor doctor) throws IOException {
        List<Doctor> doctors = getAll();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Doctor d : doctors) {
                if (d.getId().equals(doctor.getId())) {
                    bw.write(doctor.toString());
                } else {
                    bw.write(d.toString());
                }
                bw.newLine();
            }
        }
    }

    @Override
    public void delete(String id) throws IOException {
        List<Doctor> doctors = getAll();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Doctor d : doctors) {
                if (!d.getId().equals(id)) {
                    bw.write(d.toString());
                    bw.newLine();
                }
            }
        }
    }

    @Override
    public Doctor searchById(String id) throws IOException {
        for (Doctor d : getAll()) {
            if (d.getId().equals(id)) {
                return d;
            }
        }
        return null;
    }

    @Override
    public List<Doctor> getAll() throws IOException {
        List<Doctor> doctors = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String id = parts[0];
                    String name = parts[1];
                    int age = Integer.parseInt(parts[2]);
                    String specialist = parts[3];
                    String password = parts[4];
                    doctors.add(new Doctor( id, name, age, specialist,password));
                }
            }
        }
        return doctors;
    }
}
