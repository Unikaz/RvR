package Robots;

import RvR.Robot;

/**
 * Created by AlexisB on 22/01/2016.
 */
public class R3 extends Robot {
    @Override
    public void run() {
        viewField(10);
        if(detectEntities().size()>0) {
            turnTo(detectEntities().get(0));
            fire();
        }else{
            turnLeft(20);
        }
    }
}
