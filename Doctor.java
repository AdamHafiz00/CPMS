

public class Doctor extends Person {
    private String specialist;

    public Doctor(int count, String id, String name, int age, String specialist) {
        super(count, id, name, age);

        this.specialist = specialist;
    }

    public String getSpecialist(){
        return specialist;
    }
    public void setSpecialist(String specialist){
        this.specialist = specialist;
    }
  @Override
      public String toString() {
        return getCount() + "," + getId() + "," + getName() + "," + getAge() + "," + getSpecialist();
    }

}
