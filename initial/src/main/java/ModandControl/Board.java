
package ModandControl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Entity
public class Board implements Serializable {
    @Id
    private Integer id=1;

    @Transient
    private Integer[][] board = new Integer[Constants.WIDTH][Constants.HEIGHT];

    private Integer maxShapes;

    @ElementCollection
    @MapKeyColumn(name="ip")
    @Column(name="ShapesCounter")
    @CollectionTable
    private Map<String,Integer> shapesCounter=new HashMap<String,Integer>(); //Maps User's IP -> How many shapes he has drawn today.

    @ManyToOne
    @JoinColumn(name = "ip", referencedColumnName = "ip")
    private User admin;

    private long lastUpdateTime=new Date().getTime(); // represented by milliSeconds.

    @OneToMany(targetEntity = User.class,cascade = CascadeType.ALL)
    private List<User> users;

    @OneToMany(targetEntity = Shape.class,cascade =CascadeType.ALL)
    private List<Shape> shapes;

    /**
     * @param user not null
     * @return @true if added, else @false.
     */
    public Boolean addUser(User user){
        if(user!=null){
            shapesCounter.putIfAbsent(user.getIp(),0);
            users.add(user);
            return true;
        }
        return false;
    }

    /**
     * @param user not null
     * @return @true if removed, else @false.
     */
    public Boolean removeUser(User user){
        if(user!=null){
            shapesCounter.remove(user.getIp());
            users.remove(user);
            return true;
        }
        return false;
    }

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
     * Assuming: System registers the user straight when he connects to the board's home-page.
     */
    public Boolean addShape(Shape shape,User user){
        if(shape!=null && shapesCounter.get(user.getIp())<maxShapes) {
            shapesCounter.put(user.getIp(),shapesCounter.get(user.getIp())+1);
            shapes.add(shape);
            board[shape.getX()][shape.getY()]=1;
            return true;
        }
        else
            return false;
    }

    /**
     * @param shape
     * @return True if success, False if not contains @shape else NULL if @shape isn't valid.
     */
    public Boolean removeShape(Shape shape,User user){
        if(shape !=null && user.getIp().equals(admin.getIp()) && containsNRemoveById(shape.getId())) {
            if(shapesCounter.get(user.getIp())>0)
                shapesCounter.put(user.getIp(),shapesCounter.get(user.getIp())-1);
            board[shape.getX()][shape.getY()]=0;
            return true;
        }
        else
            return false;

    }
    private Boolean checkDayPassed(){
        long curTime= new Date().getTime();
        if(curTime - lastUpdateTime >= Constants.MILLISECONDSADAY){
            lastUpdateTime=curTime;
            return true;
        }
        return false;
    }

    public void resetAccordingTheDay(){
        if(checkDayPassed()){
            for(String userip : shapesCounter.keySet())
                shapesCounter.put(userip,0);
        }
    }

    /**
     * @return JSON of current board shapes.
      */
    public String stringfyShapes(){
        String toRet;
        ObjectMapper ow= new ObjectMapper();
        ow.enable(SerializationFeature.INDENT_OUTPUT);
        try{
            toRet=ow.writeValueAsString(shapes);
        }catch(JsonProcessingException e){
            toRet="Bad shapes Conversion";
        }
        return toRet;
    }
    private Boolean containsNRemoveById(Integer id){
        for(Shape shp : shapes){
            if(shp.getId()==id) {
                shapes.remove(shp);
                return true;
            }
        }
        return false;
    }

    public Boolean containsUser(User user){
        if(users!=null && users.contains(user))
            return true;
        else
            return false;
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

    public User getAdmin(){return admin;}

    public void setAdmin(User user){admin=user;}


}
