import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Contains an ellipse and a color
 */
public class ColoredEllipse {
    protected Ellipse2D.Double e;
    protected Color c;
    public ColoredEllipse(Ellipse2D.Double e, Color c){
        this.e=e;
        this.c=c;
    }
}
