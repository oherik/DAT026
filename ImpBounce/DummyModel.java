import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.List;

public class DummyModel implements IBouncingBallsModel {

	private final double areaWidth;
	private final double areaHeight;

	private final double g = 9.81;

	private List<Ball> ballList = new LinkedList<>();

	public DummyModel(double width, double height) {
		this.areaWidth = width;
		this.areaHeight = height;
		addBall(1,1,2.0,1,1,1);
		addBall(6,1,-2.0,1,1,1);
		setRelativeColor();
	}

	/**
	 * The default ball is here changed so that the color is a shade of red, depending on the mass of the ball.
	 * This is to simplify visual debugging.
	 */
	private void setRelativeColor(){
		double maxMass = 0;
		for (Ball b : ballList){
			if (b.getM()>maxMass)
				maxMass = b.getM();
		}

		for(Ball b : ballList){
			double tmp = b.getM()/maxMass;
			float saturation = (float) tmp;
			Color newColor = 	Color.getHSBColor(0, saturation, 1);
			b.setColor(newColor);
		}
	}

	/**
	 * Adds a ball to the list of balls
     */
	public void addBall(double x,double y,double vx,double vy,double r,double m){
		ballList.add(new Ball(x,y,vx,vy,r,m));
	}

	@Override
	public void tick(double deltaT) {
		for(Ball b : ballList) {
			double x = b.getX();
			double y = b.getY();
			double r = b.getR();

			// Change vertical velocity based on gravity
			applyGravity(b, deltaT);

			// Check ball collision
			checkCollision();

			//Handle edge collision
			double vx = b.getVx();
			double vy = b.getVy();

			if (x < r || x > areaWidth - r) {
				vx *= -1;
			}
			if (y < r || y > areaHeight - r) {
				vy *= -1;
			}
			x += vx * deltaT;
			y += vy * deltaT;

			b.setSpeed(vx,vy);
			b.setPos(x,y);
		}
	}

	/**
	 * Checks the collision between all balls in the system
	 */
	private void checkCollision(){
		Ball ball1, ball2;
		double distance;
		for(int i = 0; i<ballList.size(); i++){
			ball1 = ballList.get(i);
			for(int j = i+1; j<ballList.size(); j++) {
				ball2 = ballList.get(j);
				distance = Math.hypot(Math.abs(ball1.getX()-ball2.getX()),Math.abs(ball1.getY()-ball2.getY()));
				if(distance<=ball1.getR()+ball2.getR()){
					collide(ball1,ball2);
				}
			}
		}
	}

	/**
	 * Fires when two balls collide
     */
	private void collide(Ball ball1, Ball ball2){

		//TODO lös kollision här

		System.out.println("Pang");
	}

	/**
	 * Converts an x and y value to polar coordinates
	 * @param x
	 * @param y
     * @return [length, angle]
     */
	private double[] rectToPolar(double x, double y){
		double length = Math.hypot(x,y);
		double angle = Math.atan(x/y);
		double array[] = {length,angle};
		return array;
	}

	/**
	 * Converts polar coordinates to x and y values
	 * @param angle
	 * @param length
     * @return [x,y]
     */
	private double[] polarToRect(double angle, double length){
		double x = Math.sin(angle)*length;
		double y = Math.cos(angle)*length;
		double array[] = {x,y};
		return array;
	}

	//rectToPolar and polarToRect

	/**
	 * Updates vertical velocity based on gravity, using Euler's method
	 * @param b	The ball
	 * @param deltaT	The change in time
     */
	private void applyGravity(Ball b, double deltaT){
		b.setSpeed(b.getVx(),b.getVy()-g*deltaT);
	}

	@Override
	public List<ColoredEllipse> getBalls() {
		List<ColoredEllipse> myBalls = new LinkedList<ColoredEllipse>();
		for(Ball b : ballList) {
			myBalls.add(new ColoredEllipse(new Ellipse2D.Double(b.getX() - b.getR(), b.getY() - b.getR(), 2 * b.getR(), 2 * b.getR()), b.getColor()));
		}
		return myBalls;
	}
}
