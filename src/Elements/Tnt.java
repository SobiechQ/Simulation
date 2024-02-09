package Elements;
import Elements.Api.Element;
import Elements.Api.Moveable;
import Elements.Api.Refreshable;
import Elements.Api.Solid;
import Map.Link;

import java.awt.*;

public class Tnt extends Solid implements Refreshable {
    private Color color = Color.RED;
    public Tnt(){

    }

    @Override
    public Color getColor() {
        return this.color;
    }

    private int frames = 0;

    @Override
    public void refresh(Link link) {
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

    @Override
    public int getPriority() {
        return 0;
    }

    private final static int EXPLOSION_RADIUS = 25;
    public void explode(Link link){
        link.stream().filter(l -> link.distance(l) < (double) Tnt.EXPLOSION_RADIUS /8).forEach(l -> {
            if (!(l.getElement() instanceof Tnt))
                l.clear();
        });
        link.stream()
                .filter(l -> link.distance(l) < Tnt.EXPLOSION_RADIUS)
                .forEach(l -> {
            if (l.getElement() instanceof Moveable moveable){
               double deltaX = link.deltaX(l) / (link.distance(l)*0.3);
               double deltaY = link.deltaY(l) / (link.distance(l)*0.3);
               if (Double.isInfinite(moveable.getVelocity().getX() + deltaX) || Double.isInfinite(moveable.getVelocity().getY() + deltaY)){
                   throw new RuntimeException(String.format("Element: [%s] tnt link: [%s]\n movable link: [%s] deltaX: [%s] deltaY: [%s]\ntnt:", l.getElement(), link, l, deltaX, deltaY));
               }
               moveable.setVelocity(moveable.getVelocity().getX() + deltaX, moveable.getVelocity().getY() + deltaY);
                ((Element) moveable).setColor(Color.BLUE);
            }
        });
    }
    @Override
    public void setColor(Color color) {
        this.color = color;
    }
}
