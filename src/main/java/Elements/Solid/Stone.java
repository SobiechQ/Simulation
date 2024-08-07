package Elements.Solid;

import Elements.Api.Core.Solid;
import Map.Link;
import lombok.NonNull;

import java.awt.*;
import java.util.Set;
/**
 * Stone element.
 * @see Solid
 */
public class Stone extends Solid {
    private final static Set<Color> COLORS = Set.of(
            new Color(104,104,104),
            new Color(143, 143, 143),
            new Color(128, 128, 128),
            new Color(116, 116, 116));
    public Stone(){
        this.setColor(COLORS.stream().skip((int) (COLORS.size() * Math.random())).findFirst().orElseGet(this::getColor));
    }
    public Stone(@NonNull Link link){
        this();
    }
}
