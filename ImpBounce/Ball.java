/**
 * Created by erik on 2016-04-24.
 */
public class Ball {
    private double x, y, vx, vy, r;
    public Ball(double x, double y, double vx0, double vy0, double r){
        this.x=x;
        this.y=y;
        this.vx=vx0;
        this.vy=vy0;
        this.r=r;
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


}
