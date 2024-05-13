package Elements.Particles;

import Elements.Api.Core.Element;
import Elements.Api.Core.Particle;
import Elements.Fluid.Water;
import Map.Link;
import lombok.NonNull;

import java.awt.*;
import java.util.Set;

public class Steam extends Particle {
    private final static Set<Color> COLORS = Set.of(
            new Color(230, 237, 253),
            new Color(203, 213, 225),
            new Color(187, 187, 187),
            new Color(213, 242, 250),
            new Color(211, 202, 202)
    );
    private final Color color;
    public Steam() {
        super(-0.2, 0.2, 0.2, 0.5, 200, 500);
        this.color = COLORS.stream().skip((int) (COLORS.size() * Math.random())).findFirst().orElseGet(this::getColor);
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public void updateGravity(@NonNull Link link) {
        this.setTimeToLive(this.getTimeToLive() - 1);
        if (this.getTimeToLive() <= 0) {
            link.clear();
            link.setElement(new Water());
        }
    }
}
