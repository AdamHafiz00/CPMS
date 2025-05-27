public class Patient extends Person {
    private String diseases;
    private String assignedDoctor;
    private String status;

    public Patient(int count, String id, String name, int age, String diseases,String assignedDoctor,String status) {
        super(count, id, name, age);
        this.diseases = diseases;
        this.assignedDoctor = assignedDoctor;
        this.status = status;
    }

    public String getDiseases() {    
        return diseases;
    }

        public String getAssignedDoctor() {    
        return assignedDoctor;
    }
    
        public String getStatus() {    
        return status;
    }

    public void setDiseases(String diseases) { 
        this.diseases = diseases;
    }

    public void setAssignedDoctor(String assignedDoctor) { 
        this.assignedDoctor = assignedDoctor;
    }

    public void setStatus(String status) { 
        this.status = status;
    }

    @Override
    public String toString() {
        return getCount() + "," + getId() + "," + getName() + "," + getAge() + "," + getDiseases() + "," + getAssignedDoctor() + "," + getStatus();
    }
}
