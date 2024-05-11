package Elements.Api.Core;

import Elements.Solid.Air;
import Elements.Api.Moveable;
import Map.Link;
import Map.Utils.Vector;
import lombok.NonNull;
import lombok.SneakyThrows;

import javax.annotation.Nullable;

import java.util.concurrent.Callable;

import static Map.Utils.Direction.*;

/**
 * Loose is the base class for all loose elements in the simulation.
 * It represents objects that on every refresh can move in the grid.
 * Loose elements are elements that tend to fall and form piles.
 * They are heavier than {@link Air}, {@link Particle}, {@link Fluid} thus they move through them changing their position.
 * They are lighter than {@link Solid} elements, and cant pass through them.
 * Loose elements are affected by gravity and stickness that determines speed of their movement.
 *
 * @see Moveable
 * @see Elements.Loose.Sand
 */
public abstract non-sealed class Loose extends Element implements Moveable {
    private final Vector velocity = new Vector();

    /**
     * Inherited constructor from {@link Element#Element()}
     *
     * @see Element
     */
    public Loose() {

    }

    /**
     * Inherited constructor from {@link Element#Element(Link)}
     *
     * @see Element
     */
    public Loose(@NonNull Link link) {
        super(link);
    }

    /**
     * Represents final value that is added to the velocity of the fluid in the y direction on every refresh
     * Allowing every implementation to speed up differently
     *
     * @return final gravity value. It is suggested to always return same parameter.
     */
    protected abstract double getGravity();

    /**
     * Represents final value that determines how fast the fluid flows and fills the space.
     * Returned value has to be in range [0, 1], where 0 means no stickness and 1 means full stickness.
     * This allows to create implementations of fluids that are more or less sticky.
     *
     * @return final stickness value. It is required to always return same parameter.
     */
    protected abstract double getStickness();

    /**
     * Behavior of Loose move is described in {@link Loose Loose} class. {@inheritDoc}
     */
    @Override
    public Link move(@NonNull Link link, @NonNull Vector stepVelocity) {

            return switch (stepVelocity.getDirection()) {
                case UP -> {
                    if (link.isInstanceOf(Air.class, UP)) {
                        stepVelocity.y --;
                        yield link.swap(UP);
                    }
                    stepVelocity.y = 0;
                    this.velocity.y = 0;
                    yield link;
                }
                case DOWN -> {
                    if (link.isInstanceOf(Air.class, DOWN)) {
                        stepVelocity.y += 1;
                        yield link.swap(DOWN);
                    }
                    if (link.isInstanceOf(Fluid.class, DOWN) || link.isInstanceOf(Particle.class, DOWN)) {
                        stepVelocity.y += 1;
                        yield link.swap(DOWN);
                    }
                    if (Math.random() > this.getStickness()) {
                        if (link.isInstanceOf(Air.class, DOWN, LEFT) && link.isInstanceOf(Air.class, DOWN, RIGHT) && link.isInstanceOf(Air.class, LEFT) && link.isInstanceOf(Air.class, RIGHT)) {
                            stepVelocity.y += 1;
                            if (Math.random() > 0.5) yield link.swap(LEFT);
                            yield link.swap(RIGHT);
                        }
                        if (link.isInstanceOf(Air.class, DOWN, LEFT) && link.isInstanceOf(Air.class, LEFT)) {
                            stepVelocity.y += 1;
                            yield link.swap(LEFT);
                        }
                        if (link.isInstanceOf(Air.class, DOWN, RIGHT) && link.isInstanceOf(Air.class, RIGHT)) {
                            stepVelocity.y += 1;
                            yield link.swap(RIGHT);
                        }
                    }

                    stepVelocity.y = 0;
                    this.velocity.y = 0;
                    yield link;
                }
                case LEFT -> {
                    if (link.isInstanceOf(Air.class, LEFT)) {
                        if (!link.isInstanceOf(Air.class, DOWN)) this.velocity.x = 0;
                        stepVelocity.x += 1;
                        yield link.swap(LEFT);
                    }
                    stepVelocity.x = 0;
                    this.velocity.x = 0;
                    yield link;
                }
                case RIGHT -> {
                    if (link.isInstanceOf(Air.class, RIGHT)) {
                        if (!link.isInstanceOf(Air.class, DOWN)) this.velocity.x = 0;
                        stepVelocity.x -= 1;
                        yield link.swap(RIGHT);
                    }
                    stepVelocity.x = 0;
                    this.velocity.x = 0;
                    yield link;
                }
                case NONE -> link;
            };

    }

    /**
     * For every refresh loose is affected by gravity.
     * If below the loose is {@link Air} or {@link Particle} it accelerates downwards.
     * If below the loose is {@link Fluid} it accelerates downwards with 1/4 of the gravity speed.
     * {@inheritDoc}
     */
    @Override
    public void updateGravity(@NonNull Link link) {
        if (link.isInstanceOf(Air.class, DOWN) || link.isInstanceOf(Particle.class, DOWN))
            this.velocity.y -= this.getGravity();
        if (link.isInstanceOf(Fluid.class, DOWN)) this.velocity.y -= this.getGravity() / 4;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getVelocity() {
        return this.velocity;
    }
}
