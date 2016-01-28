package RvR;

import java.util.ArrayList;

/**
 * Created by Unikaz on 17/01/2016.
 */
public abstract class OtherEntity extends Entity {

    public static int lootNumber;

    public OtherEntity(){
        this((int)(Math.random()*500), (int)(Math.random()*500));
    }
    public OtherEntity(int x, int y){
        super(0, 0, x, y);
        this.setEntityName("other_"+lootNumber++);
        this.setSize(5);
    }

    @Override
    public void run() {
        // ba rien ^^
    }
    public abstract void action(Entity e);

    // Capacities
    public void setDead(){
        this.removeHealth(100);
    }
    public void addHealth(Entity e, int amount){
        e.internAddHealth(amount);
    }

    // super.Getter
    public ArrayList<Entity> getTouchingEntitiesReal(){
        return super.getTouchingEntitiesReal();
    }
    // super.Setter
    public void setX(double x){
        super.setX(x);
    }
    public void setY(double y){
        super.setY(y);
    }
    public void setOrientation(double orientation){
        super.setOrientation(orientation);
    }
    public void setSize(int size){
        super.setSize(size);
    }
    public void setViewField(int viewField){
        super.setViewField(viewField);
    }
    public void setMaxHealth(int amount){
        super.setMaxHealth(amount);
    }
    public void setHealth(int amount){
        super.setHealth(amount);
    }


}
