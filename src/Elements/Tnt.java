package Elements;
import Elements.Api.Loose;
import Elements.Api.Solid;
import Map.GridDecorator;
import Map.Link;

import java.awt.*;

public class Tnt extends Solid {
    private Color color = Color.RED;
    public Tnt(){

    }

    @Override
    public Color getColor() {
        return this.color;
    }

    private int frames = 0;
    public void nextFrame(Link link){
        frames++;
        if (frames % 4 == 0) {
            if (this.color.equals(Color.RED))
                this.color = Color.ORANGE;
            else if (this.color.equals(Color.ORANGE))
                this.color = Color.RED;
        }
        if(frames == 32){
            frames = 0;
            explode(link);
        }
    }

    private final static int EXPLOSION_RADIUS = 40;
    public void explode(Link link){
        link.stream().filter(l -> link.distance(l) < (double) Tnt.EXPLOSION_RADIUS /8).forEach(l -> {
//            if (!(l.getElement() instanceof Tnt))
//                l.clear();
        });
        link.stream().filter(l -> link.distance(l) < Tnt.EXPLOSION_RADIUS).forEach(l -> {
            if (l.getElement() instanceof Sand sand){
               double deltaX = Tnt.EXPLOSION_RADIUS/-link.deltaX(l);
               double deltaY = Tnt.EXPLOSION_RADIUS/link.deltaY(l);
               sand.setVelocity(sand.getVelocity().getX() + deltaX, sand.getVelocity().getY() + deltaY);
            }
        });
    }
}
