package ModandControl;

import javax.persistence.Entity;

@Entity
public class Triangle extends Shape {

    private static final String name="Triangle";

    private Integer size;

    public Integer getSize() {
        return size;
    }
    public void setName(String name){}; //Used for auto-deserialization
    public String getName(){return name;}

    public void setSize(Integer size) {
        this.size = size;
    }
}
