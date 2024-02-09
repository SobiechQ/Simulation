package Elements;

import Elements.Api.Solid;

import java.awt.*;
import java.util.Set;

public class Stone extends Solid {
    private final Set<Color> colors = Set.of(new Color(104,104,104), new Color(143, 143, 143), new Color(128, 128, 128), new Color(116, 116, 116));
    private Color color;
    public Stone(){
        this.color = (Color) colors.toArray()[(int) (Math.random() * colors.size())]; //todo make statioc
    }
    @Override
    public Color getColor() {
        return this.color;
    }
    @Override
    public void setColor(Color color) {
        this.color = color;
    }
}
