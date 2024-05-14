package Elements.Loose;

import Elements.Api.Core.Loose;
import Elements.Api.Refreshable;
import Elements.Fluid.Lava;
import Elements.Fluid.Water;
import Elements.Particles.FireParticle;
import Elements.Particles.SmokeParticle;
import Elements.Solid.Wood;
import Map.Link;
import Map.Utils.Direction;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.awt.*;
import java.util.Set;

public class Snow extends Loose implements Refreshable {
    @Getter
    @Setter
    private boolean isInfected = false;
    private final static Set<Color> COLORS = Set.of(
            new Color(246, 246, 246, 255),
            new Color(250, 243, 243, 255),
            new Color(252, 252, 252, 255)
    );
    public Snow() {
        this.setColor(COLORS.stream().skip((int) (COLORS.size() * Math.random())).findFirst().orElseGet(this::getColor));
    }

    @Override
    protected double getGravity() {
        return 0.004;
    }

    @Override
    protected double getStickness() {
        return 0.01;
    }


    @Override
    public void updateGravity(@NonNull Link link) {
        super.getVelocity().y += -0.1 + Math.random() * 0.2;
        super.getVelocity().x += -0.1 + Math.random() * 0.2;
    }

    @Override
    public void refresh(@NonNull Link link) {
        super.refresh(link);
        link.surroundingLink(1).forEach(l -> {
            if (l.getElement() instanceof Wood){
                if (Math.random() > 0.95)
                    l.clear();
                if (Math.random() > 0.98)
                    link.setElement(new Snow());
            }
            if (l.getElement() instanceof Lava || l.getElement() instanceof FireParticle){
                link.clear();
            }
            if (l.getElement() instanceof MagicSand){
                this.isInfected = true;
                this.setColor(new Color(115, 3, 178, 255));
            }
            if (l.getElement() instanceof Snow snow){
                if (!this.isInfected && snow.isInfected) {
                    l.setElement(new SmokeParticle());
                }
            }

        });

    }
}
