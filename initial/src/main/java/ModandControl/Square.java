package ModandControl;

import javax.persistence.Entity;

@Entity
public class Square extends Shape {

    private static final String name="Square";

    private Integer size;

    public Integer getSize() {
        return size;
    }

    public String getName(){return name;}

    public void setSize(Integer size) {
        this.size = size;
    }
}
