package Elements.Api;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public abstract class Element {
    private Color color = Color.MAGENTA;

    public Element() {

    };
    public Color getColor(){
        return this.color;
    }
    public void setColor(Color color){
        this.color = color;
    }
}
