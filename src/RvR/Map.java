package RvR;

import java.util.ArrayList;

/**
 * Created by Unikaz on 17/01/2016.
 */
public abstract class Map {

    private RvR rvrRef;
    private ArrayList<Entity> entities;
    private String mapName;

    public Map(RvR rvr){
        this.rvrRef = rvr;
        setMap();
    }
    public abstract ArrayList<Entity> getMap();
    public void setMap(){
        for(Entity e : getMap()){
            rvrRef.addEntity(e);
        }
    }
}
