package Elements;

import Elements.Api.Element;
import Elements.Api.Flameable;
import Map.Link;

import java.awt.*;
import java.util.Set;

public class Wood extends Element implements Flameable {
    private boolean setOnFire = false;
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
    private final Color initialColor;
    public Wood(){
        this.initialColor = COLORS.stream().skip((int) (COLORS.size() * Math.random())).findFirst().get();
        this.setColor(this.initialColor);
    }

    @Override
    public void setOnFire() {
        this.setOnFire = true;
    }

    @Override
    public void extinguish() {
        this.setOnFire = false;
    }

    @Override
    public boolean isSetOnFire() {
        return this.setOnFire;
    }

    @Override
    public void refresh(Link l) {

    }
}
