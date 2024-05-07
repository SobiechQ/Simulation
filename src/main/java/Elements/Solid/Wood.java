package Elements.Solid;

import Elements.Api.Core.Particle;
import Elements.Api.Core.Solid;
import Elements.Api.Flameable;
import Elements.Api.ParticleGenerator;
import Elements.Loose.Ash;
import Elements.Particles.FireParticle;
import Elements.Particles.SmokeParticle;
import Map.Link;
import Noice.Perlin;
import lombok.NonNull;

import java.awt.*;
import java.util.List;
import java.util.Set;

public class Wood extends Solid implements Flameable {
    private final static Set<Color> ON_FIRE_COLORS = Set.of(
            new Color(245, 149, 71),
            new Color(253, 235, 179),
            new Color(220, 159, 75),
            new Color(255, 127, 92),
            new Color(239, 189, 88),
            new Color(203, 130, 0),
            new Color(250, 102, 57)
    );
    private boolean isOnFire;
    private int timeToLive = (int) (350 + Math.random() * 100);
    private final Color initialColor;
    private final ParticleGenerator particleGenerator = new ParticleGenerator() {
        @Override
        public double getParticleIntensity() {
            return 0.1;
        }

        @Override
        public double getParticleRadius() {
            return 4;
        }

        @Override
        public Particle getParticle() {
            return Math.random() > 0.05 ? new FireParticle() : new SmokeParticle();
        }
    };

    private final static List<Color> COLORS = List.of(
            new Color(108, 87, 54),
            new Color(187, 146, 90),
            new Color(31, 26, 15)
    );

    public final static Perlin PERLIN_1 = new Perlin(8, 4, -2, 4);
    public final static Perlin PERLIN_2 = new Perlin(8, 4, -1, 4);
    public final static Perlin PERLIN_3 = new Perlin(8, 4, -1, 4);
    public Wood(Link link) {
        final var color1 = this.blend(COLORS.get(0), COLORS.get(1), PERLIN_1.getValue(link.getXReal(), link.getYReal()));
        final var color2 = this.blend(COLORS.get(1), COLORS.get(2), PERLIN_2.getValue(link.getXReal(), link.getYReal())); //todo getter by link
        final var color3 = this.blend(color1, color2, PERLIN_3.getValue(link.getXReal(), link.getYReal()));
        this.setColor(color3);
        this.initialColor = color3;

        this.timeToLive -= (int) (250 * PERLIN_2.getValue(link.getXReal(), link.getYReal()));
        this.timeToLive += (int) (50 * PERLIN_1.getValue(link.getXReal(), link.getYReal()));
        this.isOnFire = false;
    }
    private Color blend(Color c1, Color c2, double ratio) {
        final double ir = 1.0 - ratio;
        return new Color(
                (int) (c1.getRed() * ratio + c2.getRed() * ir),
                (int) (c1.getGreen() * ratio + c2.getGreen() * ir),
                (int) (c1.getBlue() * ratio + c2.getBlue() * ir)
        );
    }

    @Override
    public void setOnFire(boolean isOnFire) {
        this.isOnFire = isOnFire;
    }

    @Override
    public boolean isOnFire() {
        return this.isOnFire;
    }

    @Override
    public void extinguish(Link link) {
        if (!this.isOnFire)
            return;
        this.isOnFire = false;
        link.surroundingLink(1)
                .forEach(l->{
                    if(l.getElement() instanceof Flameable flameable)
                        flameable.extinguish(l);
                });
        this.setColor(this.initialColor);
    }

    @Override
    public void refresh(@NonNull Link link) {
        if (!this.isOnFire())
            return;
        if (Math.random() > 0.98)
            this.setColor(ON_FIRE_COLORS.stream().skip((int) (ON_FIRE_COLORS.size() * Math.random())).findFirst().get());
        if (Math.random() > 0.99)
            link.surroundingLink(1).forEach(l->{
                if (Math.random() > Wood.PERLIN_1.getValue(l) && l.getElement() instanceof Flameable flameable)
                    flameable.setOnFire(true);
            });
        this.particleGenerator.refresh(link);
        if (this.timeToLive-- <= 0)
            if (Math.random()>0.9)
                link.set(new Ash(link));
            else
                link.clear();

    }
}
