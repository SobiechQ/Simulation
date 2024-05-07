package Elements.Api.Core;

import Elements.Solid.Air;
import Elements.Api.Moveable;
import Map.Link;
import Map.Utils.Vector;

import static Map.Utils.Direction.*;

/**
 * Fluid is the base class for all fluid elements in the simulation.
 * It represents objects that on every refresh can move in the grid.
 * Fluids are elements that tend to flow and fill the space they are in.
 * They are heavier than {@link Air} and {@link Particle}, thus they move through them changing their position.
 * They are lighter than {@link Solid} and {@link Loose} elements, and cant pass through them.
 * Fluids are affected by gravity and stickness that determines speed of their movement.
 *
 * @see Moveable
 * @see Elements.Fluid.Water
 */
public abstract non-sealed class Fluid extends Element implements Moveable {
    private final Vector velocity = new Vector();

    /**
     * Inherited constructor from {@link Element#Element()}
     *
     * @see Element
     */
    public Fluid() {
        super();
    }

    /**
     * Inherited constructor from {@link Element#Element(Link)}
     *
     * @see Element
     */
    public Fluid(Link link) {
        super(link);
    }

    //todo test czy faktycznie 0 no stickness i 1 full stickness

    /**
     * Represents final value that is added to the velocity of the fluid in the y direction on every refresh
     * Allowing every implementation to speed up differently
     *
     * @return final gravity value. It is suggested to always return same parameter.
     */
    protected abstract double getGravity();

    //todo tu jest nieprawda, bo nie musi być [0,1]

    /**
     * Represents final value that determines how fast the fluid flows and fills the space.
     * Returned value has to be in range [0, 1], where 0 means no stickness and 1 means full stickness.
     * This allows to create implementations of fluids that are more or less sticky.
     *
     * @return final stickness value. It is required to always return same parameter.
     */
    protected abstract double getStickness();


    /**
     * Behavior of fluid move is described in {@link Fluid Fluid} class.
     * {@inheritDoc}
     */
    @Override
    public Link move(Link link, Vector stepVelocity) {
        return switch (stepVelocity.getDirection()) {
            case UP -> {
                if (link.isInstanceOf(Air.class, UP)) {
                    stepVelocity.y--;
                    yield link.swap(UP);
                }
                stepVelocity.y = 0;
                this.velocity.y = 0;
                yield link;
            }
            case DOWN -> {
                if (link.isInstanceOf(Air.class, DOWN) || link.isInstanceOf(Particle.class, DOWN)) {
                    stepVelocity.y += 1;
                    yield link.swap(DOWN);
                }
                if (link.isInstanceOf(Air.class, LEFT) && link.isInstanceOf(Air.class, RIGHT)) {
                    boolean left = stepVelocity.x < 0;
                    if (stepVelocity.x == 0)
                        left = Math.random() >= 0.5;
                    this.velocity.x = (left ? -1 : 1) * this.getStickness();
                    stepVelocity.y = 0;
                    yield link;
                }
                if (link.isInstanceOf(Air.class, LEFT)) {
                    this.velocity.x = -this.getStickness();
                    stepVelocity.y = 0;
                    yield link;
                }
                if (link.isInstanceOf(Air.class, RIGHT)) {
                    this.velocity.x = this.getStickness();
                    stepVelocity.y = 0;
                    yield link;
                }
                stepVelocity.y = 0;
                yield link;
            }
            case LEFT -> {
                if (link.isInstanceOf(Air.class, LEFT)) {
                    stepVelocity.x++;
                    yield link.swap(LEFT);
                }
                stepVelocity.x = 0;
                this.velocity.x = 0;
                yield link;
            }
            case RIGHT -> {
                if (link.isInstanceOf(Air.class, RIGHT)) {
                    stepVelocity.x--;
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
     * For every refresh fluid is affected by gravity.
     * If below the fluid is {@link Air} or {@link Particle} it accelerates downwards.
     * {@inheritDoc}
     */
    @Override
    public void updateGravity(Link link) {
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
