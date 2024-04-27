package Elements.Api.Core;

import java.awt.*;

public abstract sealed class Element permits Loose, Fluid, Solid, Particle {
    private Color color = Color.MAGENTA;

    public Element() {
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }



}
