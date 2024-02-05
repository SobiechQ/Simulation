package Elements;

import Map.GridDecorator;

import java.awt.*;
import java.util.Set;

public class Sand extends Loose{
    private final static Set<Color> COLORS = Set.of(
            new Color(212,203,147),
            new Color(210, 200, 144),
            new Color(226, 223, 188),
            new Color(255, 247, 202),
            new Color(252, 250, 235),
            new Color(177, 169, 115),
            new Color(231, 219, 177)
    );
    private final Color color;
    private final Vector vector;
    public Sand(){
        //random color
        this.color = COLORS.stream().skip((int) (COLORS.size() * Math.random())).findFirst().get();
        this.vector = new Vector();
    }
    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public Vector getVector() {
        return this.vector;
    }

    @Override
    public void computeVector(GridDecorator gridDetector) {
        if(gridDetector.getElement(0, 1).isPresent()) {
            if (gridDetector.getElement(0, 1).get() instanceof Air) {
                this.vector.setX(0);
                this.vector.setY(1);
                gridDetector.setElement(0, 1, this);
                gridDetector.unsetElement(0, 0);
                return;
            }
//            if (Math.random() > 0.30) {
//                this.vector.setY(1);
//                return;
//            }
            if (this.vector.getY() == 1)
                return;
            int direction = (int) Math.round(Math.random() * 2) - 1;
            if (gridDetector.getElement(direction, 1).isPresent())
                if (gridDetector.getElement(direction, 1).get() instanceof Air) {
                    gridDetector.setElement(direction, 1, this);
                    gridDetector.unsetElement(0, 0);
                    return;
                }
        }
    }


}
