import java.awt.*;

/**
 * A model class for a ball. It doesn't do anything per se, but keeps track of the different values.
 */
public class Ball {
    private double x, y, vx, vy, m, r;
    private Color color;

    /**
     * Create a new ball
     * @param x The initial x position
     * @param y The initial y position
     * @param vx0   The initial horizontal speed
     * @param vy0   The initial vertical speed
     * @param r The radius of the ball
     * @param m The mass of the ball
     * @param color The color of the ball
     */
    public Ball(double x, double y, double vx0, double vy0, double r, double m, Color color){
        this.x=x;
        this.y=y;
        this.vx=vx0;
        this.vy=vy0;
        this.r=r;
        this.m=m;
        this.color = color;

    }
    public Ball(double x, double y, double vx0, double vy0, double r, double m) {
        this(x, y, vx0, vy0, r, m, Color.RED);
    }
    public void setPos(double x, double y){
        this.x = x;
        this.y=y;
    }
    public void setSpeed(double vx, double vy){
        this.vx = vx;
        this.vy=vy;
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public double getVx(){
        return vx;
    }
    public double getVy(){
        return vy;
    }
    public double getR() {
        return r;
    }
    public double getM(){
        return m;
    }
    public void setColor(Color color){
        this.color = color;
    }
    public Color getColor(){
        return color;
    }


}
