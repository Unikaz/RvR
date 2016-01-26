package RvR;

/**
 * Created by Unikaz on 17/01/2016.
 */
public class SimpleWallEntity extends Entity {

    public SimpleWallEntity(int x, int y, int s, int orientation){
        super(orientation, 0, x, y);
        this.setSize(s);
    }

    @Override
    public void run() {

    }
}
