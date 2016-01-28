package RvR;

import javax.swing.event.EventListenerList;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Unikaz on 16/01/2016.
 */
public abstract class Entity {

    private String name = "Unknown";
    private double x; // X coord
    private double y; // Y coord
    private double velocity; // Velocity
    private float maxVelocity; // Max Velocity
    private double o; // Orientation
    private int size; // RvR.Entity radius
    private int health; // Health
    private int maxHealth;
    int damage; // amount of damage by attack
    private int damageRange; // range of the attack
    private int viewDistance; // Distance de vision
    private int viewField; // champ de vision
    private Color color = null; // Color of the entity, only for RvRUI.GameUI
    private long bornTime = -1; // in ticks
    private long deathTime = -1; // in ticks
    ArrayList<TargetEntity> touchingEntities;
    ArrayList<TargetEntity> viewEntities;

    // Etat
    private boolean touchWall;
    private boolean dead = false;
    private boolean touch = false;


    public Entity(){
        this(0, 0, 0, 0, 1, 1, 1, 1);
    }
    public Entity(int x, int y){
        this(0, 0, x, y, 1, 1, 1, 1);
    }
    public Entity(double orientation, int velocity, float x, float y){
        this(orientation, velocity, x, y, 1, 1, 1, 1);
    }
    public Entity(double orientation, int velocity, float x, float y, int size, int health, int damage, int damageRange){
        // on accepte tout de base, on modifiera après ^^
        this.velocity = velocity;
        this.x = x;
        this.y = y;
        this.o = orientation;
        this.size = size;
        this.health = health;
        this.damage = damage;
        this.damageRange = damageRange;
        //
        this.touchWall = false;

        if(this instanceof Robot){
            this.velocity = 0;
            this.maxVelocity = 5;
            this.size = 15;
            this.health = 100;
            this.damageRange = 15;
            this.damage = 200;
        }else if(this instanceof Bullet){
            this.velocity = 10;
            this.size = 2;
        }
        //
        maxHealth = 100;
        //
        viewDistance = 1000;
        viewField = 30;
        //
        touchingEntities = new ArrayList<TargetEntity>();
        viewEntities = new ArrayList<TargetEntity>();
    }




    // Getter
    public String getType(){
        if(this instanceof Robot)
            return "Robot";
        else
            return this.getClass().getSimpleName();
    }
    public String getRobotType(){
        String res = this.getClass().getName();
        if(res.contains("."))
            res = res.split("\\.")[1];
        else
            res = null;
        return res;
    }
    public String getEntityName(){
        return name;
    }
    public int getHealth(){
        return health;
    }
    public int getDamage(){
        return damage;
    }
    public int getDamageRange(){
        return damageRange;
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public Coord getCoord(){
        return new Coord(x, y);
    }
    public double getOrientation(){
        return o;
    }
    public int getSize(){
        return size;
    }
    public double getVelocity(){
        return velocity;
    }
    public boolean isTouchWall(){
        return touchWall;
    }
    public boolean isDead(){
        return dead;
    }
    public boolean isTouch(){
        /*
        boolean b = touch;
        touch = false;
        return b;
        */
        return touch;
    }
    public Color getColor(){
        return this.color;
    }
    public long getTimeAlive(){
        if(deathTime!=-1)
            return deathTime-bornTime;
        else
            return -bornTime;
    }
    public ArrayList<TargetEntity> getTouchingEntities(){
        return touchingEntities;
    }
    public int getViewDistance() {
        return viewDistance;
    }
    public int getViewField() {
        return viewField;
    }
    ArrayList<TargetEntity> getViewEntities(){
        return viewEntities;
    }
    public float getMaxSpeed(){
        return maxVelocity;
    }

    // SETTER
    void setX(double x){
        this.x = x;
    }
    void setY(double y){
        this.y = y;
    }
    void setOrientation(double orientation){
        this.o = (orientation+360.0)%360.0;
    }
    void setTouchWall(boolean b){
        this.touchWall = b;
    }
    void setTouch(boolean b){
        touch = b;
    }
    void setBornTime(long tick){
        this.bornTime = tick;
    }
    void setDeathTime(long time){
        this.deathTime = time;
    }
    void touchEntity(Entity e){
        touch = true;
        touchingEntities.add(new TargetEntity(e));
    }
    void setSize(int size){
        this.size = size;
    }
    void addViewEntity(TargetEntity lte){
        viewEntities.add(lte);
    }
    void setViewField(int viewField){
        this.viewField = viewField;
    }
    // SETTER accessible au robot
    public void setEntityName(String name){
        this.name = name;
    }
    public void setColor(Color c){
        this.color = c;
    }


    // Functions
    public abstract void run();
    void internalRun(){}; // à redéfinir seulement si besoin (= pour les robots)
    void removeHealth(int amount){
        this.health -= amount;
        if(this.health<0){
            fireDeath();
            dead = true;
        }
    }
    void internAddHealth(int amount){
        this.health += amount;
        if(this.health > maxHealth)
            health = maxHealth;
    }
    void move(){
        this.touchWall = false;

        Coord end = GameMaths.pointTo(this.x, this.y, this.o, this.velocity);
        this.x = end.x;
        this.y = end.y;
        fireMove();
    }
    void setVelocity(double speed){
        if(speed <= maxVelocity && speed>=0)
            this.velocity = speed;
        else
            this.velocity = maxVelocity;
    }
    void resetSensors(){
        touchingEntities.clear();
        viewEntities.clear();
        touch = false;
        touchWall = false;
    }
    // Functions for final user
    public void say(String toSay){
        this.fireTalkEvent(toSay);
    }


    //  EVENTS

    // records of listeners
    private final EventListenerList entityListeners = new EventListenerList();
    void addEntityListener(EntityEvents gl){
       // System.out.println("addListener");
        entityListeners.add(EntityEvents.class, gl);
    }
    void removeEntityListener(EntityEvents gl){
        entityListeners.remove(EntityEvents.class, gl);
    }
    EntityEvents[] getEntityListeners(){
        return entityListeners.getListeners(EntityEvents.class);
    }

    // Events
    void fireTalkEvent(String toSay){
        for(EntityEvents listener : getEntityListeners()) {
            listener.onTalk(this, toSay);
        }
    }
    // Events Combat
    void fireDamage(int damage){
        for(EntityEvents listener : getEntityListeners()) {
            listener.onDamage(this, damage);
        }
    }
    void fireAttack(){
        for(EntityEvents listener : getEntityListeners()) {
            listener.onAttack(this);
        }
    }
    void fireFire(){
        for(EntityEvents listener : getEntityListeners()) {
            listener.onFire(this);
        }
    }
    void fireHit(Entity target){
        for(EntityEvents listener : getEntityListeners()) {
            listener.onHit(this, target);
        }
    }
    void fireDeath(){
        this.dead = true;
        for(EntityEvents listener : getEntityListeners()) {
            listener.onDeath(this);
        }
    }
    // Event Move
    void fireHitWall(){
        for(EntityEvents listener : getEntityListeners()) {
            listener.onHitWall(this);
        }
    }
    void fireMove(){
        for(EntityEvents listener : getEntityListeners()) {
            listener.onMove(this);
        }
    }

}

