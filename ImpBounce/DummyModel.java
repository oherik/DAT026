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
		ballList.add(new Ball(1,3,2.3,1,1));
		ballList.add(new Ball(3,5,2.3,1,1));
	}

	public void addBall(double x,double y,double vx,double vy,double r){
		ballList.add(new Ball(x,y,vx,vy,r));
	}

	@Override
	public void tick(double deltaT) {
		for(Ball b : ballList) {
			double x = b.getX();
			double y = b.getY();
			double r = b.getR();
			double vx = b.getVx();
			double vy = b.getVy();

			// Change vertical velocity based on gravity
			vy = applyGravity(vy, deltaT);

			//Handle edge collision
			if (x < r || x > areaWidth - r) {
				vx *= -1;
			}
			if (y < r || y > areaHeight - r) {
				vy *= -1;
			}
			x += vx * deltaT;
			y += vy * deltaT;

			// Update values
			b.setSpeed(vx,vy);
			b.setPos(x,y);
		}
	}

	/**
	 * Updates vertical velocity based on gravity, using Euler's method
	 * @param vy	The initial vertical velocity
	 * @param deltaT	The change in time
     * @return	The updated vertical velocity
     */
	private double applyGravity(double vy, double deltaT){
		return vy-g*deltaT;
	}

	@Override
	public List<Ellipse2D> getBalls() {
		List<Ellipse2D> myBalls = new LinkedList<Ellipse2D>();
		for(Ball b : ballList) {
			myBalls.add(new Ellipse2D.Double(b.getX() - b.getR(), b.getY() - b.getR(), 2 * b.getR(), 2 * b.getR()));
		}
		return myBalls;
	}
}
