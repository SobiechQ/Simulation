package Elements.Particles;

import Elements.Api.Core.Particle;
import Map.Link;

import java.awt.*;
import java.util.Set;

public class SmokeParticle extends Particle {
    private final static Set<Color> COLORS = Set.of(
            new Color(183, 183, 183, 255),
            new Color(140, 140, 140, 255),
            new Color(189, 186, 182),
            new Color(189, 187, 182),
            new Color(243, 243, 243),
            new Color(148, 148, 148, 179),
            new Color(58, 58, 58, 255)
    );
    public SmokeParticle(Link link){
        this();
    }
    public SmokeParticle() {
        super(-0.2, 0.2, 0.2, 0.4, 200, 500);
        this.setColor(COLORS.stream().skip((int) (COLORS.size() * Math.random())).findFirst().get());
    }
}
