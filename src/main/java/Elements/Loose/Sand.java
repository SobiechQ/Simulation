package Elements.Loose;

import Elements.Api.Core.Loose;
import Map.Link;

import java.awt.*;
import java.util.Set;

public class Sand extends Loose {
    private final static Set<Color> COLORS = Set.of(
            new Color(212, 203, 147),
            new Color(210, 200, 144),
            new Color(226, 223, 188),
            new Color(255, 247, 202),
            new Color(252, 250, 235),
            new Color(177, 169, 115),
            new Color(231, 219, 177)
    );

    public Sand(Link link) {
        this();
    }
    public Sand(){
        this.setColor(COLORS.stream().skip((int) (COLORS.size() * Math.random())).findFirst().get());
    }



    @Override
    protected double gravity() {
        return 0.4;
    }

    @Override
    protected double stickness() {
        return 0.01;
    }


    @Override
    public void refresh(Link link) {
        super.refresh(link);
    }
}
