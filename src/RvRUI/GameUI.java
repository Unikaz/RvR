package RvRUI;

import RvR.Robot;
import RvR.Entity;
import RvR.*;
import RvR.Coord;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Unikaz on 16/01/2016.
 */
public class GameUI extends JPanel{

    RvR theGame;
    Graphics g;
    int bounds = 2;
    long lastTime; // pour les fps
    String message = null;

    public GameUI(RvR theGame){
        this.setBackground(Color.gray);
        this.theGame = theGame;
        this.setSize(theGame.ringSize+2*bounds, theGame.ringSize+2*bounds);
        // FPS 1
        lastTime = System.currentTimeMillis();
    }

    public void paintComponent(Graphics g){
        this.g = g;
        setBounds(this.getX(), this.getY(), theGame.ringSize+2*bounds, theGame.ringSize+2*bounds);
        // Wall
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, theGame.ringSize+2*bounds, theGame.ringSize+2*bounds);
        // Background
        g.setColor(Color.white);
        g.fillRect(bounds, bounds, theGame.ringSize, theGame.ringSize);
        // Draw Entities
        drawEntities();
        // affichage des fps
        drawFps();
        // drawMessage
        if(message!=null)
            drawMessage();
    }

    public void drawEntity(Entity e){
        if (e.isDead())
            return;
        if(e.getColor() != null)
            g.setColor(e.getColor());
        else
            g.setColor(Color.black);

        g.fillOval((int)e.getX()-e.getSize(), (int)e.getY()-e.getSize(), e.getSize()*2, e.getSize()*2);
        if(e instanceof Robot) {
            g.setColor(Color.red);

            int markerSize = 5;
            double x = e.getX()-markerSize/2;
            double y = e.getY()-markerSize/2;
            int s = e.getSize();
            double radians = Math.toRadians(e.getOrientation());
            x = x + (s*Math.sin(radians));
            y = y - (s*Math.cos(radians));
            g.fillOval((int)x, (int)y, markerSize, markerSize);

            // orientation line
            /*
            g.setColor(Color.MAGENTA);
            double x2 = e.getX();
            double y2 = e.getY();
            s = 1000;
            radians = Math.toRadians(e.getOrientation());
            x2 = x2 + (s*Math.sin(radians));
            y2 = y2 - (s*Math.cos(radians));//(Math.abs(this.y-posY)*Math.tan((double)getOrientation()));
            g.drawLine((int)e.getX(), (int)e.getY(), (int)x2, (int)y2);
            */

            // View field
            Coord[] viewFieldPoints = ((Robot) e).getFieldViewPoints();
            g.drawLine((int)e.getX(), (int)e.getY(), (int)viewFieldPoints[0].x, (int)viewFieldPoints[0].y);
            g.drawLine((int)e.getX(), (int)e.getY(), (int)viewFieldPoints[1].x, (int)viewFieldPoints[1].y);
        }
    }
    public void drawEntities(){
        for(int i=0 ; i<theGame.getEntities().size() ; i++){
            drawEntity(theGame.getEntities().get(i));
        }
    }

    public void drawFps(){
        int tps = (int)(1000.0/(System.currentTimeMillis() - lastTime));
        lastTime = System.currentTimeMillis();

        g.setColor(Color.MAGENTA);
        g.setFont(new Font("Courier", Font.BOLD, 20));
        g.drawString(String.valueOf(tps), 10, 20);
    }

    public void drawMessage(){
        g.setColor(Color.MAGENTA);
        g.setFont(new Font("Courier", Font.BOLD, 20));
        g.drawString(message, theGame.ringSize/2, theGame.ringSize/2);
        message = null;
    }
    public void sendMessage(String message) {
        this.message = message;
        repaint();
    }
}

