package RvR;

import java.util.ArrayList;

/**
 * Created by Unikaz on 16/01/2016.
 */
public abstract class Robot extends Entity implements CanSee{

    protected String presentation;
    int fireCooldown = 15;
    int timeBeforeFire = 0;

    // les objectifs : les valeurs que le robot veut atteindre mais qui ne sont pas instantanée
    int xWanted = -1, yWanted = -1;
    double orientationWanted = -1;
    int    viewFieldWanted = -1;
    double velocityWanted = -1;

    // les accélérations
    int viewFieldAcceleration = 10;
    int velocityAcceleration = 1;
    int orientationAcceleration = 15;

    public Robot(){
        // Spawn aléatoire
        this((int)(Math.random()*500), (int)(Math.random()*500));

    }
    public Robot(int posX, int posY){
        super((int)(Math.random()*359), 0, posX, posY);
    }

    // Méthodes internes (non-accessible au robot final)
    void internalRun(){
        if(timeBeforeFire>0)
            timeBeforeFire--;

        internalOrientation();
        internalMoveTo();
        internalViewField();
    }
    private void internalMoveTo(){
        if(xWanted == -1 || yWanted == -1)
            return;

        // Vitesse
        double dist = GameMaths.distance(this.getX(), this.getY(), xWanted, yWanted);
        if(dist < this.getVelocity()) {
            setSpeed(0);
            this.setX(xWanted);
            this.setY(yWanted);
            xWanted = yWanted = -1;
        }else{
            // Orientation
            double angle = Math.toDegrees(Math.atan2(yWanted -getY(), xWanted -getX()));
            angle = angle + 90;
            turnTo(angle);
            //setSpeed(this.getMaxSpeed());
        }
    }
    private void internalViewField(){
        if(viewFieldWanted >= 1 && viewFieldWanted!=this.viewField()){
            if(Math.abs(viewFieldWanted-viewField()) < viewFieldAcceleration){
                setViewField(viewFieldWanted);
                viewFieldWanted = -1;
            }else if(viewField()<viewFieldWanted){
                setViewField(viewField()+viewFieldAcceleration);
            }else{
                setViewField(viewField()-viewFieldAcceleration);
            }
        }
    }
    private void internalOrientation(){
        if(orientationWanted != -1 && orientationWanted!=this.getOrientation()){
            if(Math.abs(orientationWanted-getOrientation()) < orientationAcceleration){
                setOrientation(orientationWanted);
                orientationWanted = -1;
            }else if(((orientationWanted-getOrientation())+360)%360 < 180){
                setOrientation(getOrientation()+orientationAcceleration);
            }else{
                setOrientation(getOrientation()-orientationAcceleration);
            }
        }
    }
    // Actionneurs
    public void goAhead(){
        setSpeed(this.getMaxSpeed());
    }
    public void moveTo(int x, int y){
        // Objectif
        this.xWanted = x;
        this.yWanted = y;
    }
    public void setSpeed(double speed){
        setVelocity(speed);
    }
    public boolean goBack(){
        return false;
    }
    public boolean goLeft(){
        return false;
    }
    public boolean goRight(){
        return false;
    }
    public void turnTo(double degrees){
        this.orientationWanted = (degrees+360.0)%360.0;;
    }
    public void turnTo(TargetEntity target){
        double angle = Math.toDegrees(Math.atan2(target.getY()-getY(),target.getX()-getX()));
        //angle = Math.toDegrees(Math.atan2(getY()-target.getY(),getX()-target.getX()));
        //angle = GameMaths.getAngle(target.getX(), target.getY(), getX(), 0, getX(), getY());
        angle = angle +90;
        turnTo(angle);
    }
    public void turnLeft(int degrees){
        this.turnTo(this.getOrientation()-degrees);
    }
    public void turnRight(int degrees){
        this.turnTo(this.getOrientation()+degrees);
    }
    public void turnLeft(){
        turnLeft(90);
    }
    public void turnRight(){
        turnRight(90);
    }
    public void fire(){
        if(timeBeforeFire == 0) {
            //say("Fire !");
            timeBeforeFire = fireCooldown;
            fireFire();
        }else{
            //say("I can't shoot right now !");
        }
    }
    public void attack(){
        say("Attack !");
        fireAttack();
    }
    public int viewField(int degree){ // Getter and Setter for Robots usage
        if(degree <= 90 && degree >0) {
            viewFieldWanted = degree;
        }
        return getViewField();
    }
    public int viewField(){ // Getter for Robots usage
        return getViewField();
    }

    // Senseurs
    public boolean canFire(){
        return timeBeforeFire == 0;
    }
    public ArrayList<TargetEntity> detectEntities(){
        return getViewEntities();
    }
    public int x(){
        return (int)Math.round(this.getX());
    }
    public int y(){
        return (int)Math.round(this.getY());
    }



    // GETTER
    public String getPresentation(){
        return this.presentation;
    }
    public Coord[] getFieldViewPoints(){
        Coord[] res = new Coord[2];
        res[0] = GameMaths.pointTo(this.getCoord(), this.getOrientation() - (this.getViewField() / 2), this.getViewDistance());
        res[1] = GameMaths.pointTo(this.getCoord(), this.getOrientation() + (this.getViewField() / 2), this.getViewDistance());
        return res;
    }

    // SETTER
    public void setPresentation(String s){
        this.presentation = s;
    }
}
