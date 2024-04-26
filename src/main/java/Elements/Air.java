package Elements;

import Elements.Api.Core.Element;

import java.awt.*;
import java.util.Set;

public class Air extends Element {

    private final static Set<Color> COLORS = Set.of(
            new Color(0, 0, 0),
            new Color(3, 3, 3),
            new Color(5, 5, 5),
            new Color(7, 7, 7),
            new Color(9, 9, 9)
    );

    public Air(){
        this.setColor(COLORS.stream().skip((int) (COLORS.size() * Math.random())).findFirst().get());
    }
}
