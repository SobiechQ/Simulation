package Elements.Fluid;

import Elements.Api.Core.Fluid;
import Elements.Api.Core.Particle;
import Elements.Api.Flameable;
import Elements.Api.ParticleGenerator;
import Elements.Api.Refreshable;
import Elements.Particles.FireParticle;
import Elements.Particles.SmokeParticle;
import Elements.Solid.Air;
import Elements.Solid.Stone;
import Map.Link;
import lombok.NonNull;

import java.awt.*;
import java.util.Set;

import static Map.Utils.Direction.UP;

public class Lava extends Fluid implements Refreshable {
    private final ParticleGenerator generator;
    private final static Set<Color> COLORS = Set.of(
            new Color(246, 225, 107),
            new Color(215, 93, 18),
            new Color(212, 90, 14),
            new Color(218, 122, 32),
            new Color(209, 72, 11),
            new Color(227, 128, 40)
    );
    public Lava(Link link){
        super(link);
        this.setColor(COLORS.stream().skip((int) (COLORS.size() * Math.random())).findFirst().orElseGet(this::getColor));
        this.generator = new ParticleGenerator() {
            @Override
            public double getParticleIntensity() {
                return 1;
            }

            @Override
            public double getParticleRadius() {
                return 2;
            }

            @Override
            public Particle getParticle() {
                return Math.random() > 0.02 ? new FireParticle() : new SmokeParticle();
            }
        };
    }

    @Override
    protected double getGravity() {
        return 0.01;
    }

    @Override
    protected double getStickness() {
        return 1;
    }

    @Override
    public void refresh(@NonNull Link link) {
        super.refresh(link);
        if (Math.random() > 0.95)
            generator.refresh(link);
        this.generator.refresh(link);
        link.surroundingLink(1)
                .forEach(l->{
                    if (l.getElement() instanceof Flameable flameable)
                        flameable.setOnFire(true);
                    if (l.getElement() instanceof Water) {
                        l.set(new Stone(l));
                        link.set(new Stone(link));
                        l.get(UP).ifPresent(up->{
                            if (up.getElement() instanceof Air)
                                up.set(new SmokeParticle()); //todo steam
                        });
                    }
                });

    }
}

