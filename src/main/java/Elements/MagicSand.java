package Elements;

import Elements.Api.NewLoose;
import Map.Link;

import java.awt.*;

public class MagicSand extends NewLoose {
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
        return 0.01;
    }


    @Override
    public void refresh(Link l) {
        super.refresh(l);
    }
}
