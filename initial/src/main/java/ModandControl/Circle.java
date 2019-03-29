package ModandControl;

import javax.persistence.Entity;

@Entity
public class Circle extends Shape {

    private Integer radius;

    private static final String name="Circle";

    public Integer getRadius() {
        return radius;
    }
    public String getName(){return name;}
    public void setName(String name){}; //Used for auto-deserialization

    public void setRadius(Integer radius) {
        this.radius = radius;
    }
}
