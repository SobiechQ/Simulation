package Elements;

import Elements.Api.Element;

import java.awt.*;

public class Air extends Element {
    private Color color = Color.BLACK;
    public Air(){


    }

    @Override
    public Color getColor() {
        return this.color;
    }
}
