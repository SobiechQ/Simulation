package Elements;

import Elements.Api.Core.Element;
import Elements.Api.Moveable;
import Elements.Api.Particle;
import Elements.Api.Core.Refreshable;
import Map.Link;

import java.awt.*;
import java.util.List;

public class Wind extends Element implements Refreshable {
    private final static java.util.List<Color> COLORS = List.of(Color.BLUE, Color.WHITE);
    public Wind(){
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

    private final static int EXPLOSION_RADIUS = 80;
    public void explode(Link link){
        final int calculatedExposionRadius = (((
                (int) link.stream()
                        .filter(l -> link.distance(l) < (double) Wind.EXPLOSION_RADIUS /4)
                        .filter(l->l.isInstanceOf(Wind.class))
                        .count() + 20)/20)*EXPLOSION_RADIUS
        );
        link.stream()
                .filter(l -> link.distance(l) < calculatedExposionRadius)
                .forEach(l -> {
                    if (l.getElement() instanceof Moveable moveable){
                        if (!(l.getElement() instanceof Particle)) {
                            double deltaX = link.deltaX(l) / (link.distance(l) * 0.1);
                            double deltaY = link.deltaY(l) / (link.distance(l) * 0.1);
                            if (Double.isInfinite(moveable.getVelocity().getX() + deltaX) || Double.isInfinite(moveable.getVelocity().getY() + deltaY)) {
                                throw new RuntimeException(String.format("Element: [%s] tnt link: [%s]\n movable link: [%s] deltaX: [%s] deltaY: [%s]\ntnt:", l.getElement(), link, l, deltaX, deltaY));
                            }

                            moveable.getVelocity().addVector(deltaX, deltaY);
                        }
                    }
                });
    }
}
