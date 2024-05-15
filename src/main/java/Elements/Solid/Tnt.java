package Elements.Solid;

import Elements.Api.Core.Particle;
import Elements.Api.Refreshable;
import Elements.Api.Core.Solid;
import Elements.Api.Moveable;
import Elements.Particles.ExplotionParticle;
import Map.Link;
import lombok.NonNull;

import java.awt.*;
import java.util.List;

/**
 * Tnt element. After predifined time it explodes and sets surrounding movable elements in motion.
 * Other elements closest to the tnt are destroyed.
 * On explosion it creates {@link ExplotionParticle}.
 *
 * @see Solid
 */
public class Tnt extends Solid implements Refreshable {
    private final static int EXPLOSION_RADIUS = 40;
    private final static List<Color> COLORS = List.of(Color.RED, Color.ORANGE);
    private int refreshCount = 0;

    public Tnt(@NonNull Link link) {
        this();
    }

    public Tnt() {
        this.setColor(COLORS.getFirst());
    }

    /**
     * Adds 1 to the refresh count. If the refresh count is 100 it explodes.
     *
     * @param link link that the element is on.
     */
    @Override
    public void refresh(@NonNull Link link) {
        if (this.refreshCount++ == 100) {
            this.refreshCount = 0;
            this.explode(link);
        }
        if (this.refreshCount % 20 == 0) this.switchColor();
    }

    private void switchColor() {
        if (this.getColor().equals(COLORS.getFirst())) {
            this.setColor(COLORS.getLast());
            return;
        }
        if (this.getColor().equals(COLORS.getLast())) this.setColor(COLORS.getFirst());
    }

    private void explode(Link link) {
        final int calculatedExposionRadius = ((((int) link.stream().filter(l -> link.distance(l) < (double) Tnt.EXPLOSION_RADIUS / 4).filter(l -> l.isInstanceOf(Tnt.class)).count() + 20) / 20) * EXPLOSION_RADIUS);

        link.stream().filter(l -> link.distance(l) < (double) calculatedExposionRadius / 4).forEach(l -> {
            l.clear();
            if (Math.random() > 0.7) l.setElement(new ExplotionParticle(l));
        });
        link.stream().filter(l -> link.distance(l) < calculatedExposionRadius).forEach(l -> {
            if (l.getElement() instanceof Moveable moveable) {
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
