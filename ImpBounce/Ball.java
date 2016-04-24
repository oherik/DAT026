import java.awt.*;

/**
 * A model class for a ball. It doesn't do anything per se, but keeps track of the different values.
 * Created by erik on 2016-04-24.
 */
public class Ball {
    private double x, y, vx, vy, r;
    private Color color;

    /**
     * Create a new ball
     * @param x The initial x position
     * @param y The initial y position
     * @param vx0   The initial horizontal speed
     * @param vy0   The initial vertical speed
     * @param r The radius of the ball
     * @param color The color of the ball
     */
    public Ball(double x, double y, double vx0, double vy0, double r, Color color){
        this.x=x;
        this.y=y;
        this.vx=vx0;
        this.vy=vy0;
        this.r=r;
        this.color = color;
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
    public Color getColor(){
        return color;
    }


}
