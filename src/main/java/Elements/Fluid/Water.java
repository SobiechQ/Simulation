package Elements.Fluid;

import Elements.Api.Core.Fluid;
import Elements.Api.Flameable;
import Map.Link;
import lombok.NonNull;

import java.awt.*;
import java.util.Set;

/**
 * Water element. Extinguishes fire particles.
 */
public class Water extends Fluid {
    private final static Set<Color> COLORS = Set.of(
            new Color(59, 111, 249),
            new Color(59, 110, 255),
            new Color(31, 86, 251),
            new Color(38, 91, 255)
    );
    public Water(){
        this.setColor(COLORS.stream().skip((int) (COLORS.size() * Math.random())).findFirst().orElseGet(this::getColor));
    }
    @Override
    protected double getGravity() {
        return 0.2;
    }

    @Override
    protected double getStickness() {
        return 1;
    }

    /**
     * Extinguishes surrounding elements.
     * @param link link that the element is on
     */
    @Override
    public void refresh(@NonNull Link link) {
        super.refresh(link);
        link.surroundingLink(1).forEach(l->{
            if (l.getElement() instanceof Flameable flameable)
                flameable.extinguish(l);
        });

    }
}
