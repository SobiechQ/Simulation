package Elements.Solid;

import Elements.Api.Core.Solid;
import Elements.Fluid.Lava;
import Elements.Fluid.Water;
import Map.Link;
import Noice.Perlin;
import Noice.PerlinBuilder;

import java.awt.*;
@Deprecated(forRemoval = true)
public class PerlinTest extends Solid {
    private final static Perlin PERLIN = new PerlinBuilder().build();
    public PerlinTest(Link link) {
//        link.stream().forEach(l -> {
//            if (l.getElement() instanceof Lava || l.getElement() instanceof Water)
//                l.clear();
//
//        });
        this.setColor(new Color((int) (255 * PERLIN.getValue(link)), 0, 0));
    }
}
