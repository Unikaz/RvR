package Robots;

import RvR.Robot;
import RvR.TargetEntity;

import java.awt.*;

/**
 * Created by AlexisB on 21/01/2016.
 */
public class Patrol extends Robot {

    boolean b = false;

    public Patrol(){
        this.setColor(Color.red);
        this.setEntityName("Police_"+(int)(Math.random()*100));
        this.setPresentation("POLICE !");
        viewField(90);
    }
    @Override
    public void run() {
        mission_01();
    }

    public void mission_01 (){


        boolean targeting = false;
        int j = -1;

        if(this.detectEntities().size()>0){
            targeting = false;
            for(int i=0 ; i<detectEntities().size() ; i++) {
                if (!detectEntities().get(i).getRobotType().equals("Patrol") && detectEntities().get(i).getType().equals("Robot")){
                    targeting = true;
                    j = i;
                }
            }
        }

        if(targeting) {
            this.turnTo(this.detectEntities().get(j));
            this.setSpeed(0);
            this.viewField(5);
            fire();
        }else{
            setSpeed(100000);
            this.viewField(90);
            if(!b){
                b=true;
                moveTo(50,50);
            }else if(this.x()==50 && this.y()==50){
                moveTo(450, 50);
            }else if(this.x()==450 && this.y()==50){
                moveTo(450, 450);
            }else if(this.x()==450 && this.y()==450){
                moveTo(50, 450);
            }else if(this.x()==50 && this.y()==450){
                b = false;
            }
        }

    }
}
