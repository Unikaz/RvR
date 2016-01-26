package RvR;

/**
 * Created by Unikaz on 17/01/2016.
 */
public class WallEntity extends Entity {

    /*
            Alors ça ce serait le mur nikel, on lui donne coord de départ et d'arriver, et il se trace et gère sa hitbox
            ...
            sauf que je ne vos pas comment faire..; va faloir faire des maths sur ce coup...

     */
    int x1, x2, y1, y2; // coord begin and end

    public WallEntity(int x1, int y1, int x2, int y2){
        super(x1, y1);
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    @Override
    public void run() {

    }
}
