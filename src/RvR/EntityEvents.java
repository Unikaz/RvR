package RvR;

import RvR.Entity;

import java.util.EventListener;

/**
 * Created by Unikaz on 16/01/2016.
 */
public interface EntityEvents extends EventListener{
    // Other
    void onTalk(Entity e, String toSay);
    // Combat related
    void onDamage(Entity target, int damage);
    void onAttack(Entity attacker);
    void onFire(Entity e);
    void onHit(Entity attacker, Entity target);
    void onDeath(Entity e);
    // Movements related
    void onMove(Entity e);
    void onHitWall(Entity e);
}

