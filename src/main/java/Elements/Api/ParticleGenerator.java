package Elements.Api;

import Elements.Air;
import Elements.Api.Core.Particle;
import Map.Chunk;
import Map.Link;

public interface ParticleGenerator extends Refreshable {
    double getParticleIntensity();

    double getParticleRadius();

    Particle getParticle();

    @Override
    default void refresh(Link link) {
        // Check if link is blocked from every direction. If it is, for optimalization refresh wont happen
        final var neighbourCount = link.surroundingLink(1).stream()
                        .filter(l->!l.isInstanceOf(Air.class))
                                .count();
        if (neighbourCount >= 9)
            return;


        link.getChunk()
                .surroundingChunks(1)
                .stream()
                .flatMap(Chunk::streamLocal)
                .filter(l -> l.distance(link) <= this.getParticleRadius())
                .forEach(l -> {
                    if (Math.random() <= this.getParticleIntensity() && l.getElement() instanceof Air)
                        l.set(this.getParticle());
                });

    }
}
