package Elements;

import Elements.Api.Core.Loose;

import java.awt.*;
import java.util.Set;

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
    public Ash(){
        this.setColor(COLORS.stream().skip((int) (COLORS.size() * Math.random())).findFirst().get());

    }
    @Override
    protected double gravity() {
        return 0.01;
    }

    @Override
    protected double stickness() {
        return 0.01;
    }
}
