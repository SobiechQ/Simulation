package Elements.Api;

import Elements.Solid.Air;
import Elements.Api.Core.Particle;
import Map.Chunk;
import Map.Link;
import lombok.NonNull;

//todo intensity jest nie optymalne. Może nie refreshable tylko po prostu generate?
public interface ParticleGenerator extends Refreshable {
    double getParticleIntensity();

    double getParticleRadius();

    Particle getParticle();

    @Override
    default void refresh(@NonNull Link link) {
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
                        l.setElement(this.getParticle());
                });

    }
}
