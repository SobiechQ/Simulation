package Elements;

import Elements.Api.Fluid;

import java.awt.*;
import java.util.Set;

public class Water extends Fluid {
    private final static Set<Color> COLORS = Set.of(
            new Color(59, 111, 249),
            new Color(59, 110, 255),
            new Color(31, 86, 251),
            new Color(38, 91, 255)
    );
    public Water(){
        this.setColor(COLORS.stream().skip((int) (COLORS.size() * Math.random())).findFirst().get());
    }
    @Override
    protected double gravity() {
        return 0.4;
    }
}
