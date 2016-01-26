package RvR;

/**
 * Created by Unikaz on 16/01/2016.
 */
class Bullet extends Entity {
    private static int bulletNumber = 0;
    private Coord target;

    public Bullet(double orientation, float coordX, float coordY){
        super(orientation, 10, coordX, coordY);

        // Définir target
        target = GameMaths.pointTo(new Coord(this.getX()-this.getSize(), this.getY()-this.getSize()),orientation, 1000);


        this.setEntityName("bullet_"+bulletNumber++);
        // On va reset qq valeurs
        this.damage = 25;
    }


    @Override
    public void run() {
        //moveTo((int)target.x, (int)target.x);
        move();

    }
}
