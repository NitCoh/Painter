package ModandControl;

import javax.persistence.Entity;

@Entity
public class Circle extends Shape {

    private Integer radius;


    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }
}
