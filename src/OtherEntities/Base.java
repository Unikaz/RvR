package OtherEntities;

import RvR.Entity;
import RvR.OtherEntity;
import RvR.Robot;
import RvR.TargetEntity;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by AlexisB on 28/01/2016.
 */
public class Base extends OtherEntity {


    public Base(){
        this.setSize(30);
        this.setEntityName("Base");
        this.setColor(Color.LIGHT_GRAY);
        this.setMaxHealth(1000000);
        this.setHealth(100000);

    }

    @Override
    public void run(){
        for(Entity e : this.getTouchingEntitiesReal()){
            addHealth(e, 1);
        }
    }
    @Override
    public void action(Entity e){

    }

}
