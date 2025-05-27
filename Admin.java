
public class Admin extends Person{
    private String level;
    private String password;
    public Admin(String id,String name, int age, String password,String level) {
        super(id, name, age);    // Call the constructor of the superclass Person
        this.password = password;
        this.level = level;      // Initialize the level of the admin
    }
    
    public String getLevel() { 
        return level; 
    }

    public void setLevel(String level) { 
        this.level = level;
    }

    public String getPassword() { 
        return password;
     }
     
    public void setPassword(String password) { 
        this.password = password;
     }

      @Override
    public String toString() {
        return getId() + "," + getName() + "," + getAge() + ","+ getPassword();    
        // Return a string representation of the Admin object example: "1,admin1,John Doe,30,admin,password123"
    }



}
