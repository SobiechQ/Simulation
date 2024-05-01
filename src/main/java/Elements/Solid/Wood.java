package Elements.Solid;

import Elements.Api.Core.Particle;
import Elements.Api.Core.Solid;
import Elements.Api.Flameable;
import Elements.Api.ParticleGenerator;
import Elements.Loose.Ash;
import Elements.Particles.FireParticle;
import Elements.Particles.SmokeParticle;
import Map.Link;
import Perlin.Noice;

import java.awt.*;
import java.util.Set;

public class Wood extends Solid implements Flameable {
    private boolean isOnFire = false;
    private int timeToLive = (int) (100 + Math.random() * 300);
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
            return Math.random() > 0.1 ? new FireParticle() : new SmokeParticle();
        }
    };
    private final static Set<Color> COLORS = Set.of(
            new Color(70, 57, 38),
            new Color(105, 84, 51),
            new Color(108, 87, 54),
            new Color(155, 123, 76),
            new Color(61, 48, 29),
            new Color(109, 88, 55),
            new Color(66, 53, 34),
            new Color(58, 45, 26)
    );
    private final static Set<Color> ON_FIRE_COLORS = Set.of(
            new Color(245, 149, 71),
            new Color(253, 235, 179),
            new Color(220, 159, 75),
            new Color(255, 127, 92),
            new Color(239, 189, 88),
            new Color(203, 130, 0),
            new Color(250, 102, 57)
    );
    public final static Noice noice = new Noice(50);
    public Wood(Link link) {
        this();
//        System.out.println(noice.getValue(link.getXReal(), link.getYReal()));

        var red = noice.getValue(link.getXReal(), link.getYReal()) * 255;
        red = red < 0 ? 0 : red >= 255 ? 255 : red;

        var green = noice.getValue(link.getXReal(), link.getYReal());


        this.setColor(new Color((int) red, 0, 0));
    }
    public Wood(){
        this.initialColor = COLORS.stream().skip((int) (COLORS.size() * Math.random())).findFirst().get();
        this.setColor(this.initialColor);
        this.isOnFire = false;
    }

    @Override
    public void setIsOnFire(boolean isOnFire) {
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
    public void refresh(Link link) {
        if (!this.isOnFire())
            return;
        if (Math.random() > 0.98)
            this.setColor(ON_FIRE_COLORS.stream().skip((int) (ON_FIRE_COLORS.size() * Math.random())).findFirst().get());
        if (Math.random() > 0.999)
            link.surroundingLink(1).forEach(l->{
                if (l.getElement() instanceof Flameable flameable)
                    flameable.setIsOnFire(true);
            });
        this.particleGenerator.refresh(link);
        if (this.timeToLive-- <= 0)
            if (Math.random()>0.7)
                link.set(new Ash(link));
            else
                link.clear();

    }
}
