package RvR;

import javax.swing.event.EventListenerList;
import java.io.File;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

/**
 * Created by Unikaz on 16/01/2016.
 */
public class RvR extends Thread implements EntityEvents {

    ArrayList<Entity> entitiesList;
    ArrayList<Entity> entitiesGarbage;
    EntityEvents listener;
    boolean running = false;
    long time;
    public int ringSize;
    int speed = 30;
    String gameType;
    Map map = null;
    Gamerule gr = null;


    // Constructeur
    public RvR(String gameType) {
        entitiesList = new ArrayList<>();
        entitiesGarbage = new ArrayList<>();
        time = 0;
        this.ringSize = 500;
        gr = new Gamerule(this, gameType);
    }

    // The Game Process
    @Override
    public void run(){
        while(running){
            try {
                tick();
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                echo("ERROR : Je sais pas quoi, mais erreur dans le run de RVR");
                running = false;
                //e.printStackTrace();
            }
        }
        clearRun();
    }
    public void tick(){
        time++;
        //echo("#################### " + String.valueOf(time) + " ####################");
        // On récup les sensors des entités
        testCollision();
        setViewEntities();
        // On éxécute le code des robots
        runEntities();
        // On controle les actions réalisées
        resetSensors();
        // On applique les gamerules
        if(gr != null)
            gr.execGamerule();
        // On nettoie la poubelle à entités
        cleanEntitiesGarbage();
        // On fire le tick pour la màj de l'RvRUI.UI
        fireTick();
    }
    public void runEntities(){
        for(int i=0 ; i<entitiesList.size() ; i++){
            if(!entitiesList.get(i).isDead()) {
                //entitiesList.get(i).internalRun();
                entitiesList.get(i).run();
                entitiesList.get(i).internalRun();
                if(entitiesList.get(i).getVelocity()!=0)
                    entitiesList.get(i).move();
            }
        }
    }

    // Entities related
    public Entity addEntity(Entity e, int x, int y){
        addEntity(e);
        e.setX(x);
        e.setY(y);
        return e;
    }
    public Entity addEntity(Entity e){
        entitiesList.add(e);
        e.setBornTime(time);
        e.addEntityListener(this);
        //echo("New Entity " + e.getEntityName());
        return e;
    }
    public ArrayList<Entity> getEntities(){
        return entitiesList;
    }
    public ArrayList<Entity> getLivingEntities(){
        ArrayList<Entity> le = new ArrayList<>();
        for(Entity e : entitiesList){
            if(!e.isDead()){
                le.add(e);
            }
        }
        return le;
    }
    public ArrayList<Entity> getLivingRobots(){
        ArrayList<Entity> le = new ArrayList<>();
        for(Entity e : entitiesList){
            if(!e.isDead() && e instanceof Robot){
                le.add(e);
            }
        }
        return le;
    }



    // Some functions
    public void startRvr(){
        if(running)
            return;
        running = true;
        presentation();
        time = 0;
        if(map != null)
            map.setMap();
        this.start();
    }
    public void stopRvr(){
        running = false;
    }
    private void clearRun(){
        while(entitiesList.size()>0){
            if(!entitiesList.get(0).isDead()) {
                entitiesList.get(0).removeHealth(1000000);
                // Petit affichage de fin
                if (entitiesList.get(0) instanceof Robot)
                    echo("[RvR] " + entitiesList.get(0).getEntityName() + " survive during " + entitiesList.get(0).getTimeAlive() + " tick");
            }
            entitiesList.remove(0);
        }
        entitiesList.clear();
        fireEnd();
    }
    public void presentation() {
        for(Entity e : entitiesList) {
            if(e instanceof Robot){
                Robot r = (Robot)e;
                r.say(r.getPresentation());
            }
            else
                e.say("I'm not a robot... but hello");
        }
    }
    private double distanceBetween(Entity e1, Entity e2){
        // Pythagore !
        return GameMaths.distance(e1.getX(), e1.getY(), e2.getX(), e2.getY())-(double)(e1.getSize() + e2.getSize());
    }
    private void testCollision(){
        for(int i=0 ; i<entitiesList.size() ; i++){
            for(int j=0 ; j<entitiesList.size(); j++){
                if(i != j && !entitiesList.get(i).isDead() && !entitiesList.get(j).isDead()) {
                    if(distanceBetween(entitiesList.get(i), entitiesList.get(j)) <= 0){
                        entitiesList.get(i).fireHit(entitiesList.get(j));
                    }
                }
            }
        }
    }
    private void setViewEntities(){
        for(int i=0 ; i<entitiesList.size() ; i++){
            if(entitiesList.get(i) instanceof CanSee && !entitiesList.get(i).isDead()) {
                // Calculate view field
                Coord origin = new Coord(entitiesList.get(i).getX(), entitiesList.get(i).getY());
                double orientation = entitiesList.get(i).getOrientation();
                int viewDistance = entitiesList.get(i).getViewDistance();
                int viewField = entitiesList.get(i).getViewField();
                Coord pt1 = GameMaths.pointTo(origin, orientation - (viewField / 2), viewDistance);
                Coord pt2 = GameMaths.pointTo(origin, orientation + (viewField / 2), viewDistance);

                for (int j = 0; j < entitiesList.size(); j++) {
                    if (i != j  && !entitiesList.get(j).isDead()) {
                        Coord target = new Coord(entitiesList.get(j).getX(), entitiesList.get(j).getY());
                        if (GameMaths.pointInTriangle(target, origin, pt1, pt2)) {
                            //echo("See !");
                            entitiesList.get(i).addViewEntity(new TargetEntity(entitiesList.get(j)));
                        }
                    }
                }
            }
        }
    }
    private ArrayList<Entity> inRange(Entity center, int range){
        ArrayList<Entity> entities = new ArrayList<>();
        for(int j=0 ; j<entitiesList.size(); j++) {
            if (center != entitiesList.get(j) && !entitiesList.get(j).isDead()) {
                if (distanceBetween(center, entitiesList.get(j)) <= range) {
                    entities.add(entitiesList.get(j));
                }
            }
        }
        return entities;
    }
    private void cleanEntitiesGarbage(){
        while(entitiesGarbage.size()>0){
            entitiesGarbage.get(0).removeEntityListener(this);
            entitiesList.remove(entitiesGarbage.get(0));
            entitiesGarbage.remove(0);
        }
    }
    private void resetSensors(){
        for(int j=0 ; j<entitiesList.size(); j++){
            if(!entitiesList.get(j).isDead()) {
                entitiesList.get(j).resetSensors();
            }
        }
    }

    // Some Getters
    public boolean isRunning(){
        return running;
    }
    // Some Setters
    public void setSpeed(int speed){
        this.speed = speed;
    }
    public void setEntityOrientation(Entity e, int degrees){
        e.setOrientation(degrees);
    }

    // Gestion EntityEvents
    @Override
    public void onTalk(Entity e, String toSay) {
        echo("["+e.getEntityName()+"] "+toSay);
    }
    @Override
    public void onDamage(Entity target, int damage) {
        echo(target.getEntityName() + " health is now " + target.getHealth());
    }
    @Override
    public void onAttack(Entity attacker) {
        echo(attacker.getEntityName() + " attack ");
        ArrayList<Entity> targets = inRange(attacker, attacker.getDamageRange());
        for(int i=0 ; i<targets.size() ; i++){
            echo(attacker.getEntityName() + " attack " + targets.get(i).getEntityName());
            targets.get(i).removeHealth(attacker.getDamage());
        }
    }
    @Override
    public void onFire(Entity e){
        if(e instanceof Robot){
            // calculate start point
            double decalage = e.getSize() + e.getVelocity() + 1;
            // imprécision dû au champ de vision
            double imprecision = 0;
            if(gr != null){
                if(gr.unprecise > 0){
                    imprecision = (Math.random()*e.getViewField() - e.getViewField()/2)*gr.unprecise;
                }
            }
            Coord c = GameMaths.pointTo(e.getX(), e.getY(), e.getOrientation(), decalage);
            addEntity(new Bullet(e.getOrientation()+imprecision, c.x, c.y));
        }
    }
    @Override
    public void onHit(Entity attacker, Entity target) {
        //echo(attacker.getEntityName() + " HIT " + target.getEntityName());
        attacker.touchEntity(target);
        target.touchEntity(attacker);
        if(attacker instanceof Bullet){
            target.removeHealth(attacker.getDamage());
            attacker.removeHealth(10);
        }else if(attacker instanceof LootEntity){
            ((LootEntity)attacker).action(target);
        }else if(attacker instanceof Robot){
            // rien... ou une explosion !!! ^^
            //echo("Boom !");
        }
    }
    @Override
    public void onDeath(Entity e) {
        if(e instanceof Bullet || e instanceof LootEntity){
            entitiesGarbage.add(e);
        }else{
            echo(e.getEntityName() + " is dead");
            e.setDeathTime(time);
        }

    }
    @Override
    public void onMove(Entity e){
        /*

        if(e.getX()+e.getSize() > ringSize)
            e.setX(ringSize-e.getSize());
        if(e.getX()-e.getSize() < 0)
            e.setX(e.getSize());
        if(e.getY()+e.getSize() > ringSize)
            e.setY(ringSize-e.getSize());
        if(e.getY()-e.getSize() < 0)
            e.setY(e.getSize());
          */

        // Collision avec les bords du ring
        if(e.getX()<e.getSize() || e.getY()<e.getSize() || e.getX()>ringSize-e.getSize() || e.getY()>ringSize-e.getSize())
        {
            // On touche les bords
            e.fireHitWall();

            if(e.getX()+e.getSize() >= ringSize)
                e.setX(ringSize-e.getSize() - 1);
            if(e.getX()-e.getSize() <= 0)
                e.setX(e.getSize() + 1);
            if(e.getY()+e.getSize() >= ringSize)
                e.setY(ringSize-e.getSize() - 1);
            if(e.getY()-e.getSize() <= 0)
                e.setY(e.getSize() + 1);
        }
        //echo(e.getEntityName() + " move to " + e.getX() + "," + e.getY());
    }
    @Override
    public void onHitWall(Entity e){
        //echo(e.getEntityName() + " hit the wall");
        e.setTouchWall(true);
        if(e instanceof Bullet){
            e.fireDeath();
        }
    }

    // Gestion RvREvents
    // records of listeners
    private final EventListenerList rvrListeners = new EventListenerList();
    public void addRvRListener(RvREvents gl){
        rvrListeners.add(RvREvents.class, gl);
    }
    public void removeRvRListener(RvREvents gl){
        rvrListeners.remove(RvREvents.class, gl);
    }
    public RvREvents[] getRvRListeners(){
        return rvrListeners.getListeners(RvREvents.class);
    }
    // fires
    protected void fireTick(){
        for(RvREvents listener : getRvRListeners()) {
            listener.onTick();
        }
    }
    protected void fireEnd(){
        for(RvREvents listener : getRvRListeners()) {
            listener.onEnd();
        }
    }


    // Utils
    public void echo(String s){
        System.out.println(s);
    }


    // ---------------------------------------
    // Instanciation dynamique d'un robot
    // ---------------------------------------
    public void dynamicLoadRobot(String robotName) {
        System.out.println("Start loading of " + robotName + "...");

        // On compile
        int resCompile = 0;
        if((resCompile = dynCompile(robotName+".java")) == 0) {
            // On instancie
            Robot dr = dynLoad(robotName);
            if (dr == null)
                System.out.println("Erreur de chargement de la class");
            else {
                // On met dans le jeu
                this.addEntity(dr);
                System.out.println("Load of " + robotName + " completed !");
            }
        }else{
            System.out.println("Error during dynamic compilation : " + resCompile);
        }


    }

    public int dynCompile(String fileAdr){
        fileAdr = "bots/"+fileAdr;
        // Attention, ce ne sera pas le même classpath quand le programme sera en jar... et je ne sais pas ce que ça donnera
        // Une solution pourrait être de choper le dir actuel, et d'extraire les .class nécessaires dedans, mais peut-être que
        // javac pourrait se servir directement dans le .jar... à voir
        String classPath = System.getProperty("user.dir")+"/out/production/rvr/";

        return com.sun.tools.javac.Main.compile(new String[] {
                "-classpath", classPath,
                "-d", System.getProperty("user.dir")+"/bots_compiled/",
                fileAdr });
    }
    public Robot dynLoad(String robotName){
        // The dir contains the compiled classes.
        File classesDir = new File(System.getProperty("user.dir")+"/bots_compiled/");

        // The parent classloader
        ClassLoader parentLoader = Robot.class.getClassLoader();

        // Load class with our own classloader.
        try {
            URLClassLoader loader1 = new URLClassLoader(new URL[]{classesDir.toURL()}, parentLoader);
            Class cls1 = loader1.loadClass("Robots."+robotName);
            return (Robot) cls1.newInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}






/*
        File file = new File(System.getProperty("user.dir") + "/bots/");
        try {
            // Convert File to a URL
            URL url = file.toURL();          // file:/c:/myclasses/
            URL[] urls = new URL[]{url};


            // Create a new class loader with the directory
            ClassLoader cl = new URLClassLoader(urls);


            // Load in the class; MyClass.class should be located in the directory file:/c:/myclasses/com/mycompany
            //Class cls = cl.loadClass("Robot");
            //Class cls = cl.loadClass(nom);
            Object r = cl.loadClass("RvR.RVR."+nom).newInstance();

            // On l'ajoute au jeu
            addEntity((Robot)r);
                    //loadClass("Main", true).newInstance();


        } catch (Exception e) {
            e.printStackTrace();
        }
        */



        /*
        try {
            //On crée un objet Class
            Class cl = Class.forName(nom);

            //Nouvelle instance de la classe Paire
            Object o = cl.newInstance();

            //On crée les paramètres du constructeur
            //Class[] types = new Class[]{String.class, String.class};

            //On récupère le constructeur avec les deux paramètres
            //Constructor ct = cl.getConstructor(types);
            Constructor ct = cl.getConstructor();

            //On instancie l'objet avec le constructeur surchargé !
            Object o2 = ct.newInstance(new String[]{"valeur 1 ", "valeur 2"} );

            // On l'ajoute au jeu
            addEntity((Robot)o2);

        } catch (Exception e) {
            e.printStackTrace();
        }
        */

