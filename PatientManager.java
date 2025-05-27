import java.io.*;
import java.util.*;

public class PatientManager implements PersonOperations<Patient> {
    private static final String FILE_NAME = "patient.txt";

 
    @Override
    public void add(Patient patient) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(patient.toString());
            bw.newLine();
        }
    }

   
    @Override
    public void update(Patient patient) throws IOException {
        List<Patient> patients = getAll();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Patient d : patients) {
                if (d.getId().equals(patient.getId())) {
                    bw.write(patient.toString());
                } else {
                    bw.write(d.toString());
                }
                bw.newLine();
            }
        }
    }

   
    @Override
    public void delete(String id) throws IOException {
        List<Patient> patients = getAll();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Patient d : patients) {
                if (!d.getId().equals(id)) {
                    bw.write(d.toString());
                    bw.newLine();
                }
            }
        }
    }


    @Override
    public Patient searchById(String id) throws IOException {
        for (Patient d : getAll()) {
            if (d.getId().equals(id)) {
                return d;
            }
        }
        return null;
    }

  
    @Override
    public List<Patient> getAll() throws IOException {
        List<Patient> patients = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String id = parts[0];
                    String name = parts[1];
                    int age = Integer.parseInt(parts[2]);
                    String diseases = parts[3];
                    String assignedDoctor = parts[4];
                    String status = parts[5];
                    patients.add(new Patient(id, name, age, diseases,assignedDoctor,status));
                }
            }
        }
        return patients;
    }
}