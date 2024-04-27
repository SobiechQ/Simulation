package Elements;

import Elements.Api.Core.Solid;

import java.awt.*;
import java.util.Set;

public class Stone extends Solid {
    private final static Set<Color> COLORS = Set.of(
            new Color(104,104,104),
            new Color(143, 143, 143),
            new Color(128, 128, 128),
            new Color(116, 116, 116));
    public Stone(){
        this.setColor(COLORS.stream().skip((int) (COLORS.size() * Math.random())).findFirst().get());
    }
}
