package Elements.Api;

import Elements.Solid.Air;
import Elements.Api.Core.Particle;
import Map.Chunk;
import Map.Link;
import lombok.NonNull;

/**
 * Allows elements to generate particles around themselves.
 */
public interface ParticleGenerator extends Refreshable {
    /**
     * Returns the intensity of the particle generation.
     * The value should be between 0 and 1.
     * The higher the value, the more particles are generated.
     *
     * @return the intensity of the particle generation.
     */
    double getParticleIntensity();

    /**
     * Returns the radius of the particle generation.
     * The value should be greater than 0.
     * The higher the value, the more particles are generated.
     *
     * @return the radius of the particle generation.
     */
    double getParticleRadius();

    /**
     * Returns the particle to be generated.
     *
     * @return the particle to be generated.
     */
    Particle getParticle();

    /**
     * For every link in radius, there is a ({@link ParticleGenerator#getParticleIntensity() intensity} / 1) * 100% chance that the particle will be generated.
     * The particle is generated only if the link in radius is empty (contains {@link Air}).
     * @param link the link that the element is refreshed on for the current tick.
     */
    @Override
    default void refresh(@NonNull Link link) {
        // Check if link is blocked from every direction. If it is, refresh should not be performed.
        if (this.isBlocked(link)) return;


        link.getChunk().surroundingChunks(1).stream()
                .flatMap(Chunk::streamLocal)
                .filter(l -> l.distance(link) <= this.getParticleRadius())
                .forEach(l -> {
            if (Math.random() <= this.getParticleIntensity() && l.getElement() instanceof Air)
                l.setElement(this.getParticle());
        });
    }

    default boolean isBlocked(@NonNull Link link) {
        return link.surroundingLink(1).stream().filter(l -> !l.isInstanceOf(Air.class)).count() >= 9;

    }
}
