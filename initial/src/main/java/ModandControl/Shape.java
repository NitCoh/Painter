package ModandControl;

import javax.persistence.*;

@Entity
@Inheritance
public abstract class Shape {

    @Id
    @GeneratedValue
    private Integer Id;

    private Integer x;

    private Integer y;

    public Integer getX(){return x;}

    public void setX(Integer x){this.x=x;}

    public Integer getY(){return y;}

    public void setY(Integer x){this.y=x;}

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }
}
