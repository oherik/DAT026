import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.List;

public class DummyModel implements IBouncingBallsModel {

	private final double areaWidth;
	private final double areaHeight;

	private final double g = 9.82;

	private List<Ball> ballList = new LinkedList<>();

	public DummyModel(double width, double height) {
		this.areaWidth = width;
		this.areaHeight = height;

		//Add three balls with different mass and radii
		addBall(1,5,3 ,2,1,2);
		addBall(9,9,-2,1,0.7,1);
		addBall(9,5,-2.0,0,1.3,3);
		setRelativeColor();
	}

	/**
	 * The default ball is here changed so that the color is a shade of red, depending on the mass of the ball.
	 * This is to simplify visual debugging. A lighter shade means lighter mass
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
		ballList.add(new Ball(x, y, vx, vy, r, m));
	}

	@Override
	public void tick(double deltaT) {
		// Check ball collision
		checkCollision();

		for(Ball b : ballList) {
			double x = b.getX();
			double y = b.getY();
			double r = b.getR();

			//Handle edge collision. Makes sure that the balls are actually moving into the wall, in order to ensure
			//that they don't get stuck
			double vx = b.getVx();
			double vy = b.getVy();

			if ((x < r) && (vx<0) || (x > areaWidth - r) && (vx>0)) {
				vx *= -1;
				b.setSpeed(vx,vy);
			}
			if ((y < r) && (vy<0) || (y > areaHeight - r) && (vy>0)) {
				vy *= -1;
				b.setSpeed(vx, vy);
			} else {
				// Change vertical velocity based on gravity. This needs to be done here in order to conserve all energy
				applyGravity(b, deltaT);
			}
			
			// Update position
			x += b.getVx() * deltaT;
			y += b.getVy() * deltaT;
			b.setPos(x,y);
		}
	}

	/**
	 * Checks the collision between all balls in the system
	 */
	private void checkCollision(){
		Ball ball1, ball2;
		double distance;
		for(int i = 0; i<ballList.size()-1; i++){		//Goes to size-1, since the last ball doesn't need a check if it collides with itself
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
	 * Calculate the dot product between two vectors
	 */
	private double dotProduct(double x1, double y1, double x2, double y2){
		return x1*x2+y1*y2;
	}

	/**
	 * Project a vector onto another vector
	 */
	private double[] project(double x1, double y1, double x2, double y2){
		double k = dotProduct(x1,y1,x2,y2)/dotProduct(x2,y2,x2,y2);
		double[] array = {k*x2,k*y2};
		return array;
	}

	/**
	 * Calculate a normal vector from a vector
	 */
	private double[] normalVector(double dx, double dy){
		double array[] = {-dy,dx};
		return array;
	}

	/**
	 * Fires when two balls collide
     */
	private void collide(Ball ball1, Ball ball2){
		//Extract various start values
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

		//Compute the collision vector and its normal vector
		double collisionVector[] = {x2-x1,y2- y1};
		double neutralVector[] = normalVector(collisionVector[0], collisionVector[1]);

		//Compute the velocities of the balls, projected onto the collision vector
		double collisionV1[] = project(vx1, vy1, collisionVector[0], collisionVector[1]);
		double collisionV2[] = project(vx2, vy2, collisionVector[0], collisionVector[1]);

		//Check if the balls are moving away from each other. If they are, the collision calculations have already been
		//made, so skip the other calculations. This is to ensure that balls don't get stuck into each other.
		if(!((collisionV1[0]*collisionVector[0]<0 || collisionV1[1]*collisionVector[1]<0)
			&& (collisionV1[0]*collisionV2[0]<0  ||	collisionV1[1]*collisionV2[1]<0))) {

			//Compute the velocities projected onto the neutral vector
			double neutralV1[] = project(vx1, vy1, neutralVector[0], neutralVector[1]);
			double neutralV2[] = project(vx2, vy2, neutralVector[0], neutralVector[1]);

			//Compute the speeds, change the sign of ball 2's speed if they have opposite starting velocities
			double u2;
			double u1 = Math.hypot(collisionV1[0], collisionV1[1]);
			if (collisionVector[0] * collisionV2[0] < 0 | collisionVector[1] *collisionV2[1]<0)
				u2 = -Math.hypot(collisionV2[0], collisionV2[1]);
			else
				u2 = Math.hypot(collisionV2[0], collisionV2[1]);

			//Compute the speeds after the collision
			double v1 = (m1*u1 - m2*u1 + 2*m2*u2)/(m1 + m2);
			double v2 = (2*m1*u1 - m1*u2 + m2*u2)/(m1 + m2);

			//Calculate how much the collision vector should be scaled, in order to apply the new speeds to the
			// original collision vector
			double vecLength = Math.hypot(collisionVector[0], collisionVector[1]);
			double k1 = v1 / vecLength;
			double k2 = v2 / vecLength;

			//Calculate the velocities after collision
			double newVy1 = collisionVector[1] * k1;
			double newVx1 = collisionVector[0] * k1;
			double newVy2 = collisionVector[1] * k2;
			double newVx2 = collisionVector[0] * k2;

			//Set the final velocities, including the neutral velocities
			ball1.setSpeed(newVx1 + neutralV1[0], newVy1 + neutralV1[1]);
			ball2.setSpeed(newVx2 + neutralV2[0], newVy2 + neutralV2[1]);
		}
	}

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
