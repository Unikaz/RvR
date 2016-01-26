package LootEntities;

import RvR.Entity;
import RvR.LootEntity;

import java.awt.*;

/**
 * Created by AlexisB on 19/01/2016.
 */
public class HealthEntity extends LootEntity {
    public HealthEntity(){
        super();
        this.setColor(Color.red);
    }
    public HealthEntity(int x, int y) {
        super(x, y);
        this.setColor(Color.red);
    }

    @Override
    public void action(Entity e) {
        // Seulement pour les robots
        if(e instanceof RvR.Robot) {
            addHealth(e, 50);
            say("+50 to " + e.getEntityName());
            setDead();
        }
    }

}
