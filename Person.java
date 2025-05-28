
public abstract class Person { 
    private String id;
    private String name;
    private int age;
   

    public Person(String id, String name, int age){
        this.id = id;
        this.name = name;
        this.age = age;
       

    }
    //encapsulation: getter and setter
    
    //getter

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

    public void setId(String id){
        this.id = id;
    
    }
    public void setName(String name){
        this.name = name;
    
    }
    
    public void setAge(String age){
        this.id = age;
    
    }

}