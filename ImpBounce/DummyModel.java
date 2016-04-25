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
		addBall(1,9,2.0,1,1,1);
		addBall(6,5,-2.0,1,1.3,2);
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

			// Check ball collision
			checkCollision();

			//Handle edge collision
			double vx = b.getVx();
			double vy = b.getVy();

			if (x < r || x > areaWidth - r) {
				vx *= -1;
				b.setSpeed(vx,vy);
			}
			if (y < r || y > areaHeight - r) {
				vy *= -1;
				b.setSpeed(vx,vy);
			} else {
				// Change vertical velocity based on gravity
				applyGravity(b, deltaT);
			}
			x += b.getVx() * deltaT;
			y += b.getVy() * deltaT;

			b.setPos(x,y);
		}
	}

	/**
	 * Checks the collision between all balls in the system
	 */
	private boolean checkCollision(){
		Ball ball1, ball2;
		double distance;
		for(int i = 0; i<ballList.size(); i++){
			ball1 = ballList.get(i);
			for(int j = i+1; j<ballList.size(); j++) {
				ball2 = ballList.get(j);
				distance = Math.hypot(Math.abs(ball1.getX()-ball2.getX()),Math.abs(ball1.getY()-ball2.getY()));
				if(distance<=ball1.getR()+ball2.getR()){
					collide(ball1,ball2);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Fires when two balls collide
     */
	private void collide(Ball ball1, Ball ball2){
		double m1 = ball1.getM();
		double m2 = ball2.getM();
		double vx1 = ball1.getVx();
		double vy1 = ball1.getVy();
		double vx2 = ball2.getVx();
		double vy2 = ball2.getVy();
		double x1 = ball1.getX();
		double y1 = ball1.getY();
		double x2 = ball2.getX();
		double y2 = ball2.getY();

		//Calculate the Collision Angle, according to x.
		double deltaX = x1-x2;
		double deltaY = y1-y2;
		double[] colVect = rectToPolar(deltaX, deltaY);
		double colAngle = colVect[1];

		//Calculate vector in collision  angle. OBS: Not sure if correct!
		//Ball 1
		double[] xCol1 = polarToRect(colAngle,Math.abs(vx1));
		double[] yCol1 = polarToRect(90-colAngle, Math.abs(vy1));

		// sum
		double xColVec1 = xCol1[0];
		double yColVec1 = yCol1[0];

		double u1 = xColVec1 + yColVec1;

		//Ball 2
		double[] xCol2 = polarToRect(colAngle,Math.abs(vx2));
		double[] yCol2 = polarToRect(90-colAngle, Math.abs(vy2));

		// sum
		double xColVec2 = xCol2[0];
		double yColVec2 = yCol2[0];

		double u2 = xColVec2 + yColVec2;

		//Calculation of collision
		double I = m1*u1 + m2*u2;
		double R =  u1 - u2;
		double v1;
		double v2;

		v1 = I/((m2*R)*(m1-m2));
		v2 = R + v1;

		// Two-dimensionals after Collision
		//Ball 1
		double[] afterCol1 = polarToRect(colAngle,v1);
		double[] neutralCol1 = polarToRect(90+colAngle, xCol1[1] + yCol1[1]);
		vx1 = afterCol1[0] + neutralCol1[0];
		vy1 = afterCol1[1] + neutralCol1[1];
		//Ball 2
		double[] afterCol2 = polarToRect(colAngle,v2);
		double[] neutralCol2 = polarToRect(90+colAngle, xCol2[1] + yCol2[1]);
		vx2 = afterCol2[0] + neutralCol2[0];
		vy2 = afterCol2[1] + neutralCol2[1];


		//ball1.setSpeed(vx1, vy2);
		//ball2.setSpeed(vx2, vy2);
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
		double angle = Math.atan(y/x);
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
		double x = Math.cos(angle)*length;
		double y = Math.sin(angle)*length;
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
