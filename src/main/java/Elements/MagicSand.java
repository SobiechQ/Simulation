package Elements;
import Elements.Api.Loose;
import java.awt.*;

public class MagicSand extends Loose {
    private static float hsvValue = 0;
    public MagicSand(){
        MagicSand.hsvValue += 0.0001F;
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
}
