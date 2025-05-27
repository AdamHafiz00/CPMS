
public class Admin extends Person{
    private String username;
    private String password;
    public Admin(int count, String id,String name, int age, String username,String password) {
        super(count,id, name, age);    // Call the constructor of the superclass Person
        this.username = username;
        this.password = password;
    }
    
    public String getUsername() { 
        return username;
     }

    public String getPassword() { 
        return password;
     }
    public void setUsername(String username) { 
        this.username = username;
     }


    public void setPassword(String password) { 
        this.password = password;
     }

      @Override
    public String toString() {
        return getCount() + "," + getId() + "," + getName() + "," + getAge() + "," + getUsername() + "," + getPassword();    
        // Return a string representation of the Admin object example: "1,admin1,John Doe,30,admin,password123"
    }



}
