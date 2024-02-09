package Elements;

import Elements.Api.Element;
import Elements.Api.GeneratesParticles;
import Map.Link;

import java.awt.*;

public class TestObj extends Element implements GeneratesParticles {
    private Color color = Color.BLUE;

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public void generateParticles(Link link) {

    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }
}
