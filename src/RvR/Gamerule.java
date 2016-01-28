package RvR;

/**
 * Created by Unikaz on 17/01/2016.
 */
public class Gamerule {
    String gametype;
    RvR rvr;

    // Différents paramètres appelé par RvR
    public double unprecise = 1; // taux d'imprécision du tir en fonction du champ de vision (0 = tir parfait, 1 = champ de vision)

    public Gamerule(RvR rvr, String name) {
        this.rvr = rvr;
        this.gametype = name;
    }

    public void execGamerule() {
        if (gametype.equals("Combat")) {
            // Le jeu se termine quand il ne reste qu'une entité
            if (rvr.getLivingRobots().size() <= 1) {
                rvr.stopRvr();
            }
        } else if (gametype.equals("Survie")) { // Survivre individuel, les robots perdent 1PV/tick, peuvent ramasser des PV
            int nbLoot = 10;
            for (Entity e : rvr.getLivingEntities()) {
                if (e instanceof Robot) {
                    // perte de 1PV par tour
                    e.removeHealth(1);
                } else if (e instanceof Bullet) {
                    // heu... si ils font feu dans une coop, c'est leur problème ^^
                } else if (e instanceof OtherEntity) {
                    // On va compter le nombre d'entité lootable pour avoir toujours X loot sur la map
                    nbLoot--;
                }
            }
            if (rvr.getLivingEntities().size() == 0) {
                // Tout le monde est mort.... fnif
                rvr.stopRvr();
            }
        } else if (gametype.equals("SurvieCoop")) { // Survivre en coop, les robots perdent 1PV/tick, peuvent ramasser et ramener du loot à la base pour gagner collectivement des PV
            int nbLoot = 10;
            for (Entity e : rvr.getLivingEntities()) {
                if (e instanceof Robot) {
                    // perte de 1PV par tour
                    e.removeHealth(1);
                } else if (e instanceof Bullet) {
                    // heu... si ils font feu dans une coop, c'est leur problème ^^
                } else if (e instanceof OtherEntity) {
                    // On va compter le nombre d'entité lootable pour avoir toujours X loot sur la map
                    nbLoot--;
                }
            }
            if (rvr.getLivingEntities().size() == 0) {
                // Tout le monde est mort.... fnif
                rvr.stopRvr();
            }
        } else if (gametype.equals("Course")) { // Course où il faut passer des checkpoints

        } else if (gametype.equals("Base")) { // Récupération d'item et les ramener à la base

        } else if (gametype.equals("CombatSurvie")) { // Survivre à des bots, pouvoir récup de la vie, peut-être des boosters

        } else if (gametype.equals("CombatTeam")) { // Le combat s'arrête lorsqu'il n'y a plus qu'un type robot
            boolean b = false;
            String team = rvr.getLivingEntities().get(0).getRobotType();
            for(Entity e : rvr.getLivingEntities()){
                if(e instanceof Robot)
                    if(!e.getRobotType().equals(team))
                        b = true;
            }
            if(!b){
                rvr.stopRvr();
            }


        } else {// Bocal : pas de rêgle, le jeu s'arrête si il n'y a plus personne
            if (rvr.getLivingRobots().size() <= 0) {
                rvr.stopRvr();
            }
        }
    }
}
