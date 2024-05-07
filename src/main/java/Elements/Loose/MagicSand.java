package Elements.Loose;

import Elements.Api.Core.Loose;
import Map.Link;

import java.awt.*;

public class MagicSand extends Loose {
    private volatile static float hsvValue = 0;
    public MagicSand(Link link){
        this();
    }
    public MagicSand(){
        MagicSand.hsvValue += 0.00005F;
        this.setColor(new Color(Color.HSBtoRGB(hsvValue, 1, 1)));
    }

    @Override
    protected double getGravity() {
        return 0.2;
    }

    @Override
    protected double getStickness() {
        return 0.01;
    }


    @Override
    public void refresh(Link link) {
        super.refresh(link);
    }
}
