package RvR;

/**
 * Created by Unikaz on 17/01/2016.
 */
public class TargetEntity {
    private Entity e;


    public TargetEntity(Entity e){
        this.e = e;
    }

    // Getter
    public String getEntityName(){
        return e.getEntityName();
    }
    public double getX(){
        return e.getX();
    }
    public double getY(){
        return e.getY();
    }
    public String getType(){
        return e.getType();
    }
    public String getRobotType(){
        return e.getRobotType();
    }

}
