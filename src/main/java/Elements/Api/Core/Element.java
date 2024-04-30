package Elements.Api.Core;

import Map.Link;

import java.awt.*;

public abstract sealed class Element permits Loose, Fluid, Solid, Particle {
    private Color color = Color.MAGENTA;
    public Element (){

    }

    public Element(Link link) {
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }



}
