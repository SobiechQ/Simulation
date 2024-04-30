package Elements.Particles;

import Elements.Api.Core.Particle;
import Elements.Api.Flameable;
import Map.Link;

import java.awt.*;
import java.util.Set;

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
        this.setColor(COLORS.stream().skip((int) (COLORS.size() * Math.random())).findFirst().get());
    }
    @Override
    public void refresh(Link link) {
        link.surroundingLink(1)
                .forEach(l->{
                    if (l.getElement() instanceof Flameable flameable){
                        if (Math.random() > 0.95)
                            flameable.setIsOnFire(true);
                    }
                });
        super.refresh(link);
    }
}
