package Elements.Particles;

import Elements.Api.Core.Particle;
import Elements.Api.Flameable;
import Map.Link;
import lombok.NonNull;

import java.awt.*;
import java.util.Set;

/**
 * ExplotionParticle element. It is generated when {@link Elements.Solid.Tnt tnt} explodes.
 * It has a chance to set surrounding elements on fire.
 * @see Particle
 * @see Flameable
 */
public class ExplotionParticle extends Particle {
    private final static Set<Color> COLORS = Set.of(
            new Color(68, 43, 26),
            new Color(253, 235, 179),
            new Color(189, 186, 182),
            new Color(189, 187, 182),
            new Color(243, 243, 243),
            new Color(203, 130, 0),
            new Color(245, 26, 68)
    );
    public ExplotionParticle(Link link){
        this();
    }
    public ExplotionParticle(){
        super(-1, 1, 0.7, 2, 10, 40);
        this.setColor(COLORS.stream().skip((int) (COLORS.size() * Math.random())).findFirst().orElseGet(this::getColor));
    }

    /**
     * Sets surrounding elements on fire with a 5% chance.
     * @param link link that the element is on
     */
    @Override
    public void refresh(@NonNull Link link) {
        link.surroundingLink(1)
                .forEach(l->{
                    if (l.getElement() instanceof Flameable flameable){
                        if (Math.random() > 0.95)
                            flameable.setOnFire();
                    }
                });
        super.refresh(link);
    }
}
