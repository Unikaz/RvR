package RvRUI;

import RvR.Entity;
import RvR.Robot;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by AlexisB on 27/01/2016.
 */
public class RobotLoader {

    // ---------------------------------------
    // Instanciation dynamique d'un robot
    // ---------------------------------------
    public static Entity dynamicLoadRobot(String robotName) {
        System.out.print("Start loading of " + robotName + "... ");

        // On compile
        int resCompile = 0;
        if((resCompile = dynCompile(robotName+".java")) == 0) {
            // On instancie
            Entity dr = dynLoad(robotName);
            if (dr == null)
                System.out.println("Error");
            else {
                // On met dans le jeu
                System.out.println("Ok");
                return dr;
            }
        }else{
            System.out.println("Error " + resCompile);
        }
        return null;
    }

    public static int dynCompile(String fileAdr){
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
    public static Entity dynLoad(String robotName){
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
