package Elements.Particles;

import Elements.Api.Particle;

import java.awt.*;
import java.util.Set;

public class FireParticle extends Particle {
    private final static Set<Color> COLORS = Set.of(
            new Color(245, 149, 71),
            new Color(253, 235, 179),
            new Color(220, 159, 75),
            new Color(255, 127, 92),
            new Color(239, 189, 88),
            new Color(203, 130, 0),
            new Color(250, 102, 57)
    );
    public FireParticle() {
        super(-0.3, 0.3, 1, 4, 5, 15);
        this.setColor(COLORS.stream().skip((int) (COLORS.size() * Math.random())).findFirst().get());
    }
}
