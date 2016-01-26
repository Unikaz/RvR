package RvR;

/**
 * Created by AlexisB on 18/01/2016.
 */
public class GameMaths {

    public static Coord pointTo(double coordOriginX, double coordOriginY, double orientation, double distance) {
        double radians = Math.toRadians(orientation);
        coordOriginX = coordOriginX + (distance * Math.sin(radians));
        coordOriginY = coordOriginY - (distance * Math.cos(radians));
        return new Coord(coordOriginX, coordOriginY);
    }
    public static Coord pointTo(Coord origin, double orientation, double distance){
        return pointTo(origin.x, origin.y, orientation, distance);
    }

    public static boolean pointInTriangle(Coord p, Coord a, Coord b, Coord c){
        double apb = getAngle(a, p, b);
        double bpc = getAngle(b, p, c);
        double cpa = getAngle(c, p, a);
        double total = apb+bpc+cpa;
        int marge = 2;
        if(total >= 360-marge && total <= 360+marge)
            return true;
        return false;
    }

    public static double distance(double x1, double y1, double x2, double y2){
        return Math.sqrt(((x1-x2)*(x1-x2))+((y1-y2)*(y1-y2)));
    }
    public static double distance(Coord coord1, Coord coord2){
        return distance(coord1.x, coord1.y, coord2.x, coord2.y);
    }

    public static double getAngle(double ax, double ay, double bx, double by, double cx, double cy){
        double c = GameMaths.distance(ax, ay, bx, by);
        double a = GameMaths.distance(bx, by, cx, cy);
        double b = GameMaths.distance(cx, cy, ax, ay);
        return getAngle(a,b,c);
    }
    public static double getAngle(double dA, double dB, double dC){
        double distA = dA;
        double distB = dB;
        double distC = dC;
        return Math.toDegrees(Math.acos(((distA*distA-distB*distB+distC*distC)/(2.0*distA*distC))));
    }
    public static double getAngle(Coord a, Coord b, Coord c){
        return getAngle(a.x, a.y, b.x, b.y, c.x, c.y);
    }

}
