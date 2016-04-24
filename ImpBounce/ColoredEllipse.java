import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by erik on 2016-04-24.
 */
public class ColoredEllipse {
    protected Ellipse2D.Double e;
    protected Color c;
    public ColoredEllipse(Ellipse2D.Double e, Color c){
        this.e=e;
        this.c=c;
    }
}
