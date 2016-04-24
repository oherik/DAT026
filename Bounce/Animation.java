import java.awt.*;

public abstract class Animation
  extends java.applet.Applet
    implements java.lang.Runnable {

    protected Dimension d;  // bitmap size
    protected Image im;  // extra image for drawing
    protected Graphics offscreen; // the offscreen bitmap to draw in
    protected int delay = 100; // in milliseconds
    protected Thread animationThread;

    final public void init() {
	d = getSize();
	im = createImage(d.width, d.height);
	offscreen = im.getGraphics();
	initAnimator();
    }

    //final public void paint(Graphics g) {update(g);}

    final public void update(Graphics g) {
	paintAnimator(offscreen);  // first draw offscreen to reduce flicker
	g.drawImage(im, 0, 0, this);  // then put on screen
    }

    // To be implemented in subclass that does the actual drawing
    protected void initAnimator() {} // init for drawing routines
    abstract protected void paintAnimator(Graphics g); // the actual drawing will be here

    public void setDelay(int d) {delay=d;}

    public void start() {
	animationThread = new Thread(this);
	animationThread.start();
    }

    public void stop() {
	animationThread = null;
    }

    public void run() {
	while (Thread.currentThread() == animationThread) {
	    repaint(); // redraws by somehow calling update
	    try {
		Thread.currentThread().sleep(delay);
	    } catch (InterruptedException e) {}
	}
    }
}
