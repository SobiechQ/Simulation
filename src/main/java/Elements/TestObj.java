package Elements;

import Elements.Api.Element;
import Elements.Api.Particleable;
import Elements.Api.Refreshable;
import Elements.Particles.SandParticle;
import Map.Link;

import java.awt.*;
import java.util.Set;

public class TestObj extends Element implements Particleable, Refreshable {

    private final static Set<Color> COLORS = Set.of(
            Color.RED,
            Color.ORANGE,
            Color.WHITE,
            Color.YELLOW
    );
    @Override
    public void generateParticles(Link link) {
        link.stream()
                .filter(l -> l.distance(link) < 5)
                .forEach(l -> {
                    if (l.getElement() instanceof Air){
                        if (Math.random() > 0.95)
                            l.set(new SandParticle(COLORS.stream().skip((int) (COLORS.size() * Math.random())).findFirst().get()));
                    }
                } );
    }


    private int refreshCount = 0;
    @Override
    public void refresh(Link link) {
        refreshCount++;
        if (refreshCount % 4 == 0)
            this.generateParticles(link);
    }
}
