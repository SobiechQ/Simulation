package Elements;

import Elements.Api.*;
import Elements.Api.Core.Particle;
import Elements.Api.Core.Solid;
import Elements.Particles.FireParticle;

public class Generator extends Solid implements ParticleGenerator{

    public Generator() {

    }

    @Override
    public double getParticleIntensity() {
        return 0.5;
    }

    @Override
    public double getParticleRadius() {
        return 3;
    }

    @Override
    public Particle getParticle() {
        return new FireParticle();
    }
}
