import java.awt.*;

public class Bounce extends Animation {

  protected double X, Y, Vx, Vy, deltaT, pixelsPerMeter;
  protected int radius, firstTime=1, pixelX, pixelY;
  protected Color color = Color.red;

  protected void initAnimator() {
    deltaT=0.005; // simulation time interval in seconds
    setDelay((int)(1000*deltaT)); // needed for Animation superclass

    X = 3; // in meters
    Y = 3; // Y reference direction downwards!
    Vx = 2; // in m/s
    Vy = -1.3;
    pixelsPerMeter = 40;

    radius = 25; // in pixels!
    pixelX = (int) (pixelsPerMeter * X); // screen position
    pixelY = (int) (pixelsPerMeter * Y);
  }

  protected void paintAnimator(Graphics g) {
    g.setColor(Color.white);
    if(firstTime==1) {g.fillRect(0,0,d.width,d.height); firstTime=0;}
    //g.fillRect(0,0,d.width,d.height); // slower?
    g.fillOval(pixelX - radius, pixelY - radius, radius * 2, radius * 2);

    if (pixelX < radius || pixelX > d.width - radius) {
      Vx = -Vx;
    }
    if (pixelY < radius || pixelY > d.height - radius) {
      Vy  =  -Vy;
    }

    X += Vx * deltaT;
    Y += Vy * deltaT;

    pixelX = (int) (pixelsPerMeter * X);
    pixelY = (int) (pixelsPerMeter * Y);

    g.setColor(color);
    g.fillOval( pixelX - radius, pixelY - radius, radius * 2, radius * 2);
  }

}
