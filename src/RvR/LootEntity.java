package RvR;

/**
 * Created by Unikaz on 17/01/2016.
 */
public abstract class LootEntity extends Entity {

    public static int lootNumber;

    public LootEntity(){
        this((int)(Math.random()*500), (int)(Math.random()*500));
    }
    public LootEntity(int x, int y){
        super(0, 0, x, y);
        this.setEntityName("loot_"+lootNumber++);
        this.setSize(5);
    }

    @Override
    public void run() {
        // ba rien ^^
    }
    public abstract void action(Entity e);

    // Capacities
    public void setDead(){
        this.removeHealth(100);
    }
    public void addHealth(Entity e, int amount){
        e.internAddHealth(amount);
    }
}
