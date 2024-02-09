package Elements;

import Elements.Api.Loose;
import Map.Utils.Direction;
import Map.Link;
import Map.Utils.Vector;

import java.awt.*;
import java.util.Set;

import static Map.Utils.Direction.*;

public class Sand extends Loose {
    private final static Set<Color> COLORS = Set.of(
            new Color(212, 203, 147),
            new Color(210, 200, 144),
            new Color(226, 223, 188),
            new Color(255, 247, 202),
            new Color(252, 250, 235),
            new Color(177, 169, 115),
            new Color(231, 219, 177)
    );
    private Color color;
//    private final Vector velocity;

    public Sand() {
        //random color
        this.color = COLORS.stream().skip((int) (COLORS.size() * Math.random())).findFirst().get();
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    protected double gravity() {
        return 0.5;
    }

    @Override
    protected double stickness() {
        return 0.8;
    }

    @Override
    public int getPriority() { //todo jako enum
        return 1;
    }
    @Override
    public void setColor(Color color) { //todo przeniesc do api
        if (this.getDebug())
            return;
        this.color = color;
    }
}
