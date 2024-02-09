package Elements.Api;

import java.awt.*;

public abstract class Element {
    private Color color = Color.MAGENTA;
    public Element(){};
    public Color getColor(){
        return this.color;
    }
    public void setColor(Color color){
        this.color = color;
    }


}
