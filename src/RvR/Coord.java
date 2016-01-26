package RvR;

public class Coord{
    public float x, y;
    public Coord(float x, float y){
        this.x = x;
        this.y = y;
    }
    public Coord(double x, double y){
        this.x = (float)x;
        this.y = (float)y;
    }
}
