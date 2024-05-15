package Elements.Particles;

import Elements.Api.Core.Particle;
import Elements.Api.Flameable;
import Map.Link;
import lombok.NonNull;

import java.awt.*;
import java.util.Set;

/**
 * FireParticle element. It is generated when {@link Elements.Solid.Wood} is on fire.
 * It has a chance to set surrounding elements on fire.
 * @see Particle
 * @see Flameable
 */
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
    public FireParticle(@NonNull Link link){
        this();
    }
    public FireParticle() {
        super( -0.3, 0.3, 1, 4, 5, 15);
        this.setColor(COLORS.stream().skip((int) (COLORS.size() * Math.random())).findFirst().orElseGet(this::getColor));
    }

    /**
     * Sets surrounding elements on fire with a 1% chance.
     * @param link link that the element is on.
     */
    @Override
    public void refresh(@NonNull Link link) {
        link.surroundingLink(1)
                .forEach(l->{
                    if (l.getElement() instanceof Flameable flameable){
                        if (Math.random() > 0.99)
                            flameable.setOnFire();
                    }
                });
        super.refresh(link);
    }
}
