package Robots;

import LootEntities.HealthEntity;
import RvR.Entity;
import RvR.GameMaths;
import RvR.Robot;
import RvR.TargetEntity;

import java.awt.*;

/**
 * Created by Unikaz on 16/01/2016.
 */
public class R1 extends Robot {

    int r;

    public R1(){
        this.setEntityName("R1_"+(int)(Math.random()*1000));
        this.setColor(Color.green);
        this.setPresentation("Hello, I just know how to move randomly");
        r = 0;
        this.viewField(20);

    }
    /*
    public R1(int x, int y){
        super(x,y);
        this.setEntityName("R1_"+(int)(Math.random()*1000));
        this.setColor(Color.green);
        this.setPresentation("Hello, I just know how to move randomly");
        r = 0;
        this.viewField(20);
    }
    */

    @Override
    public void run(){

        coloration();

        mission1();
    }

    public void coloration (){
        int h = this.getHealth();
        if(h >= 99){
            this.setColor(Color.green);
        }else if(h >= 50){
            this.setColor(Color.yellow);
        }else{
            this.setColor(Color.orange);
        }
    }
    public void mission1 (){
        boolean objective = false;
        for(int i=0 ; i<detectEntities().size() ; i++){
            //say(detectEntities().get(i).getType());

            //turnTo(detectEntities().get(i));
            if(detectEntities().get(i).getType().equals("HealthEntity")){

                TargetEntity t = detectEntities().get(i);
                double dist = GameMaths.distance(this.getX(), this.getY(), t.getX(), t.getY());
                turnTo(t);

                objective = true;
                goAhead();
                break;
            }else if(detectEntities().get(i).getType().equals("Robot")) {
                turnTo(detectEntities().get(i));
                //fire();
                objective = true;
                break;
            }
        }

        if(!objective)
            turnRight(20);


    }
    public void mission2(){
        //say(this.x() + "-" + this.y());
        boolean objective = false;
        if(!objective) {
            say("0");
            moveTo(50, 50);
            objective = true;
        }else if(this.x() == 50 && this.y() == 50){
            say("1");
            moveTo(250, 450);
        }else if(this.x() == 250 && this.y() == 450){
            say("2");
            moveTo(50, 450);
        }else if(this.x() == 50 && this.y() == 450){
            say("3");
            objective = false;
        }
    }
    public void mission3(){
        if(getTouchingEntities().size()>0){
            if(getTouchingEntities().get(0).getType().equals("Robots")){
                if (!getTouchingEntities().get(0).getRobotType().equals("R1") || true){
                    //say("I'm gonna kill you " + getTouchingEntities().get(0).getEntityName());
                    attack();
                }else{
                    //say("Hello friend");
                }
            }
        }
    }

}
