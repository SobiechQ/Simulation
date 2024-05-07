package Elements.Solid;

import Elements.Api.Core.Solid;
import Elements.Fluid.Lava;
import Elements.Fluid.Water;
import Map.Link;
import Perlin.Noice;

import java.awt.*;

public class PerlinTest extends Solid {
    private final static Noice noice = new Noice(16, 1, 1, 4);
    public PerlinTest(Link link) {
        link.stream().forEach(l->{
            if (l.getElement() instanceof Lava || l.getElement() instanceof Water)
                l.clear();
        });
        this.setColor(new Color((int) (255*noice.getValue(link)), 0, 0));
    }
}
