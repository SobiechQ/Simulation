package Elements;

import Elements.Api.*;
import Elements.Particles.SmokeParticle;
import Map.Link;
import Map.Utils.Vector;

public class Generator extends Element implements ParticleGenerator{

    public Generator() {

    }

    @Override
    public double getParticleIntensity() {
        return 0.01;
    }

    @Override
    public double getParticleRadius() {
        return 10;
    }

    @Override
    public Particle getParticle() {
        return new SmokeParticle();
    }
}
