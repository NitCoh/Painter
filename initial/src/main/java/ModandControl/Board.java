
package ModandControl;
import javax.persistence.*;

import java.io.Serializable;
import java.util.List;


@Entity
public class Board implements Serializable {
    @Id
    private Integer id=1;

    @Transient
    private Integer[][] board = new Integer[Constants.WIDTH][Constants.HEIGHT];

    private Integer maxShapes;


    @OneToMany(targetEntity = Shape.class,cascade =CascadeType.ALL)
    private List<Shape> shapes;

    /**
     * Intializing the board to zero's.
     */
    public void startBoard(){
        for(int i=0;i<Constants.WIDTH;i++)
            for(int j=0;j<Constants.HEIGHT;j++)
                board[i][j]=0;
    }

    /**
     * @param shape
     * @return True if success, False if exceeding @maxShapes else NULL if @shape isn't valid.
     */
    public Boolean addShape(Shape shape){
        if(shape!=null && shapes.size()<maxShapes) {
            shapes.add(shape);
            board[shape.getCenterX()][shape.getCenterY()]=1;
            return true;
        }
        else if(shapes.size()>=maxShapes)
            return false;
        else
            return null;
    }

    /**
     * @param shape
     * @return True if success, False if not contains @shape else NULL if @shape isn't valid.
     */
    public Boolean removeShape(Shape shape){
        if(shape !=null && shapes.contains(shape)) {
            shapes.remove(shape);
            board[shape.getCenterX()][shape.getCenterY()]=0;
            return true;
        }
        else if(!shapes.contains(shape))
            return false;
        else
            return null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMaxShapes() {
        return maxShapes;
    }

    public void setMaxShapes(Integer maxShapes) {
        this.maxShapes = maxShapes;
    }

    // Future Attributes : board-admin.


}
