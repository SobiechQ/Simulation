package Elements;

import Elements.Api.*;
import Elements.Api.Core.Element;
import Elements.Particles.FireParticle;
import Elements.Particles.SmokeParticle;

public class Generator extends Element implements ParticleGenerator{

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
