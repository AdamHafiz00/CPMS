

public class Doctor extends Person {
    private String specialist;
    private String password;
    public Doctor(int count, String id, String name, int age, String specialist,String password) {
        super(count, id, name, age);
        this.password = password;
        this.specialist = specialist;
    }

    public String getSpecialist(){
        return specialist;
    }
    public void setSpecialist(String specialist){
        this.specialist = specialist;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
  @Override
      public String toString() {
        return getCount() + "," + getId() + "," + getName() + "," + getAge() + "," + getSpecialist() + "," + getPassword();
    }

}
