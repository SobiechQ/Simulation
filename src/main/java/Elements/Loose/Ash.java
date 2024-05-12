package Elements.Loose;

import Elements.Api.Core.Loose;
import Map.Link;

import java.awt.*;
import java.util.Set;

/**
 * Ash element. It is generated when {@link Elements.Solid.Wood} is burned.
 * @see Loose
 */
public class Ash extends Loose {
    private final static Set<Color> COLORS = Set.of(
            new Color(54, 54, 54, 90),
            new Color(86, 86, 86),
            new Color(28, 25, 21),
            new Color(24, 21, 21),
            new Color(91, 91, 91),
            new Color(128, 128, 128),
            new Color(63, 63, 63)
    );
    public Ash(Link link){
        this();
    }
    public Ash(){
        this.setColor(COLORS.stream().skip((int) (COLORS.size() * Math.random())).findFirst().orElseGet(this::getColor));
    }

    @Override
    protected double getGravity() {
        return 0.01;
    }

    @Override
    protected double getStickness() {
        return 0.01;
    }
}
