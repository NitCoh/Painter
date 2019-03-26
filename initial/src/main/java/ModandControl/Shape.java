package ModandControl;

import javax.persistence.*;

@Entity
@Inheritance
public abstract class Shape {

    @Id
    @GeneratedValue
    private Integer Id;

    private Integer centerX;

    private Integer centerY;

    public Integer getCenterX() {
        return centerX;
    }

    public Integer getCenterY() {
        return centerY;
    }

    public void setCenterX(Integer centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(Integer centerY) {
        this.centerY = centerY;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }
}
