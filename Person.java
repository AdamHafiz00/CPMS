
public abstract class Person { 
    private String id;
    private String name;
    private int age;
    private int count;

    public Person(int count, String id, String name, int age){
        this.id = id;
        this.name = name;
        this.age = age;
        this.count = count;

    }
    //encapsulation: getter and setter
    
    //getter
    public int getCount(){
        return count;
    }
    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }
        public int getAge(){
        return age;
    }

    //setter
    public void setCount(int count){
        this.count = count;

    }
    public void setId(String id){
        this.id = id;
    
    }
    public void setname(String name){
        this.name = name;
    
    }
    
    public void setAge(String age){
        this.id = age;
    
    }

}