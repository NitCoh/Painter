package ModandControl;

import javax.persistence.Entity;

@Entity
public class Triangle extends Shape {

    private Integer size;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
