package Robots;

import RvR.GameMaths;
import RvR.Robot;
import RvR.TargetEntity;

import java.awt.*;

/**
 * Created by Unikaz on 16/01/2016.
 */
public class R2 extends Robot {
    public R2(){
        this.setEntityName("R2");
        this.setColor(Color.blue);
        this.setPresentation("Hello, i attack all the time");

        viewField(10);
    }

    @Override
    public void run(){
        boolean foundTarget = false;
        if(detectEntities().size() > 0){
            for(int i=0 ; i< detectEntities().size() ; i++) {
                if (detectEntities().get(i).getType().equals("Robot")) {
                    if (!detectEntities().get(i).getRobotType().equals("R2")) {
                        foundTarget = true;
                        // On a vu un robot, on s'aligne
                        turnTo(detectEntities().get(i));
                        //say(""+a);
                        if (canFire()) {
                            fire();
                        }
                        break;
                    }
                }
            }
        }
        if(!foundTarget)
            turnLeft(10);
    }
}
