package Elements.Solid;

import Elements.Api.Core.Element;
import Elements.Api.Core.Solid;
import Map.Link;

import java.awt.*;
import java.util.Set;

public class Air extends Solid {

    private final static Set<Color> COLORS = Set.of(
            new Color(0, 0, 0),
            new Color(3, 3, 3),
            new Color(5, 5, 5),
            new Color(7, 7, 7),
            new Color(9, 9, 9)
    );

    public Air(Link link){
        this();
    }
    public Air(){
        this.setColor(COLORS.stream().skip((int) (COLORS.size() * Math.random())).findFirst().orElseGet(this::getColor));
    }
}
