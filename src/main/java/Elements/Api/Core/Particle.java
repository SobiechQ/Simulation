package Elements.Api.Core;

import Elements.Solid.Air;
import Elements.Api.Moveable;
import Map.Link;
import Map.Utils.Vector;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;

import java.awt.*;

import static Map.Utils.Direction.*;

/**
 * Particle is the base class for all particles in the simulation.
 * It represents objects that on every refresh can move in the grid.
 * Particles are elements that tend to move up and fill the space.
 * They have predefined time to live, after which they disappear.
 * On creation their initial velocity is randomized, based on the constraints given in the constructor.
 * They are lighter than {@link Solid}, {@link Loose}, {@link Fluid} elements, and cant pass through them.
 *
 * @see Moveable
 * @see Elements.Particles.SmokeParticle
 */
public abstract non-sealed class Particle extends Element implements Moveable {
    private final Vector velocity;
    private final int maxTimeToLive;
    @Getter
    @Setter
    private int timeToLive;

    /**
     * Particle constructor with predefined velocity and time to live.
     * Values are randomized between given parameters.
     * Inherited constructor from {@link Element#Element()}
     * @param xMin minimum up/down velocity in squares per refresh.
     * @param xMax maximum up/down velocity in squares per refresh.
     * @param yMin minimum left/right velocity in squares per refresh.
     * @param yMax maximum left/right velocity in squares per refresh.
     * @param timeToLiveMin minimum time to live in tics.
     * @param timeToLiveMax maximum time to live in tics.
     * @see Element
     */
    public Particle(double xMin, double xMax, double yMin, double yMax, int timeToLiveMin, int timeToLiveMax) {
        super();
        double xVelocity = xMin + Math.random() * (xMax - xMin);
        double yVelocity = yMin + Math.random() * (yMax - yMin);
        this.maxTimeToLive = (int) (timeToLiveMin + Math.random() * (timeToLiveMax - timeToLiveMin));
        this.velocity = new Vector(xVelocity, yVelocity);
        this.timeToLive = maxTimeToLive;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getVelocity() {
        return this.velocity;
    }

    /**
     * Returns the color of the particle changed by the remaining time to live.
     * The color is faded based on the index of remaining time/maximum time to live, thus with every tick the color is closer to black.
     * @return faded color of the particle
     */
    @Override
    public Color getColor() {
        return new Color(this.getFadedRed(), this.getFadedGreen(), this.getFadedBlue());
    }

    private double getFadeIndex() {
        return (double) this.timeToLive / this.maxTimeToLive;
    }

    private int getFadedRed() {
        final var red = super.getColor().getRed() * this.getFadeIndex();
        return red < 0 ? 0 : (int) (red > 255 ? 255 : red);
    }

    private int getFadedGreen() {
        final var green = super.getColor().getGreen() * this.getFadeIndex();
        return green < 0 ? 0 : (int) (green > 255 ? 255 : green);
    }

    private int getFadedBlue() {
        final var blue = super.getColor().getBlue() * this.getFadeIndex();
        return blue < 0 ? 0 : (int) (blue > 255 ? 255 : blue);
    }

    /**
     * Behavior of Particle move is described in {@link Particle Particle} class. {@inheritDoc}
     * @return
     */
    @Override
    public Link move(@NonNull Link link, @NonNull Vector stepVelocity) {
        return switch (stepVelocity.getDirection()) {
            case UP -> {
                if (link.isInstanceOf(Air.class, UP)) {
                    stepVelocity.y -= 1;
                    yield link.swap(UP);
                }
                if (link.isInstanceOf(Air.class, LEFT) && link.isInstanceOf(Air.class, RIGHT)) {
                    boolean left = stepVelocity.x < 0;
                    if (stepVelocity.x == 0)
                        left = Math.random() >= 0.5;
                    stepVelocity.y = 0;
                    yield left ? link.swap(LEFT) : link.swap(RIGHT);
                }
                if (link.isInstanceOf(Air.class, LEFT)) {
                    stepVelocity.y = 0;
                    yield link.swap(LEFT);
                }
                if (link.isInstanceOf(Air.class, RIGHT)) {
                    stepVelocity.y = 0;
                    yield link.swap(RIGHT);
                }
                stepVelocity.y = 0;
                yield link;
            }
            case DOWN -> {
                if (link.isInstanceOf(Air.class, DOWN)) {
                    stepVelocity.y += 1;
                    yield link.swap(DOWN);
                }
                stepVelocity.y = 0;
                yield link;
            }
            case LEFT -> {
                if (link.isInstanceOf(Air.class, LEFT)) {
                    stepVelocity.x += 1;
                    yield link.swap(LEFT);
                }
                stepVelocity.x = 0;
                yield link;
            }
            case RIGHT -> {
                if (link.isInstanceOf(Air.class, RIGHT)) {
                    stepVelocity.x -= 1;
                    yield link.swap(RIGHT);
                }
                stepVelocity.x = 0;
                yield link;
            }
            case NONE -> link;
        };
    }
    /**
     * Particles are not affected by gravity. Their velocity is changed only by interactions with or by other elements.
     * This method is used to handle remaining time to live value. On every refresh it is decreased by one.
     * If the time to live reaches zero, the particle disappears, and its velocity is cleared.
     * {@inheritDoc}
     */
    @Override
    public void updateGravity(@NonNull Link link) {
        if (this.timeToLive-- <= 0) {
            link.clear();
            this.velocity.clear();
        }
    }
}
