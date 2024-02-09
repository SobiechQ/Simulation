package Elements;
import Elements.Api.*;
import Map.Link;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Tnt extends Solid implements Refreshable {
    private final static List<Color> COLORS = List.of(Color.RED, Color.ORANGE);
    public Tnt(){
        this.setColor(COLORS.getFirst());
    }
    private int refreshCount = 0;
    @Override
    public void refresh(Link link) {
        refreshCount++;
        if (this.refreshCount == 100){
            this.refreshCount = 0;
            this.explode(link);
        }
        if (this.refreshCount % 20 == 0)
            this.switchColor();


    }
    private void switchColor(){
        if(this.getColor().equals(COLORS.getFirst())) {
            this.setColor(COLORS.getLast());
            return;
        }
        if (this.getColor().equals(COLORS.getLast()))
            this.setColor(COLORS.getFirst());
    }

    private final static int EXPLOSION_RADIUS = 40;
    public void explode(Link link){
        link.stream().filter(l -> link.distance(l) < (double) Tnt.EXPLOSION_RADIUS /4).forEach( l -> {
            if (Math.random() > 0.98)
                if (l.getElement() instanceof Particleable particleable)
                    particleable.generateParticles(l);
            l.clear();
        });
        link.stream()
                .filter(l -> link.distance(l) < Tnt.EXPLOSION_RADIUS)
                .forEach(l -> {
            if (l.getElement() instanceof Moveable moveable){
                if (!(l.getElement() instanceof Particle)) {
                    double deltaX = link.deltaX(l) / (link.distance(l) * 0.2);
                    double deltaY = link.deltaY(l) / (link.distance(l) * 0.2);
                    if (Double.isInfinite(moveable.getVelocity().getX() + deltaX) || Double.isInfinite(moveable.getVelocity().getY() + deltaY)) {
                        throw new RuntimeException(String.format("Element: [%s] tnt link: [%s]\n movable link: [%s] deltaX: [%s] deltaY: [%s]\ntnt:", l.getElement(), link, l, deltaX, deltaY));
                    }

                    moveable.getVelocity().addVector(deltaX, deltaY);
                }
            }
        });
    }
}
