package Elements;
import Elements.Api.Loose;
import Elements.Api.Particleable;
import Elements.Particles.SandParticle;
import Map.Link;

import java.awt.*;

public class MagicSand extends Loose implements Particleable {
    private static float hsvValue = 0;
    public MagicSand(){
        MagicSand.hsvValue += 0.00005F;
        this.setColor(new Color(Color.HSBtoRGB(hsvValue, 1, 1)));
    }

    @Override
    protected double gravity() {
        return 0.5;
    }

    @Override
    protected double stickness() {
        return 0.8;
    }

    @Override
    public void generateParticles(Link link) {
        link.stream()
                .filter(l -> l.distance(link) < 5)
                .forEach(l -> {
                    if (l.getElement() instanceof Air){
                        l.set(new SandParticle(this.getColor()));
                    }
                } );
    }

    @Override
    public void refresh(Link l) {
        super.refresh(l);
    }
}
