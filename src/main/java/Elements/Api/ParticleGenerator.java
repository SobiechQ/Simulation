package Elements.Api;

import Elements.Air;
import Map.Link;

public interface ParticleGenerator extends Refreshable {
    double getParticleIntensity();
    double getParticleRadius();
    Particle getParticle();
    @Override
    default void refresh(Link link){
        link.stream(this.getParticleRadius())
                .forEach(l->{
                    if (Math.random() <= this.getParticleIntensity() && l.getElement() instanceof Air)
                        l.set(this.getParticle());
                });
    }
}
