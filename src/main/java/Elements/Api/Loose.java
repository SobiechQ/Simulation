package Elements.Api;

import Elements.Air;
import Elements.Api.Core.Element;
import Map.Link;
import Map.Utils.Vector;

import static Map.Utils.Direction.*;

public abstract class Loose extends Element implements Moveable {
    protected abstract double gravity();
    protected abstract double stickness();
    private final Vector velocity = new Vector();
    public Loose(){

    }

    @Override
    public Link move(Link link, Vector stepVelocity) {
        return switch (stepVelocity.getDirection()) {
            case UP -> {
                if (!link.isInstanceOf(Air.class, UP)){
                    stepVelocity.y = 0;
                    this.velocity.y = 0;
                    yield link;
                }
                stepVelocity.y -= 1;
                yield link.swap(UP);
            }
            case DOWN -> {
                if (!link.isInstanceOf(Air.class, DOWN)) {
                    if (link.isInstanceOf(Fluid.class, DOWN) || link.isInstanceOf(Particle.class, DOWN)){
                        stepVelocity.y += 1;
                        yield link.swap(DOWN);
                    }
                    if (Math.random() > this.stickness()) {
                        if (link.isInstanceOf(Air.class, DOWN, LEFT) && link.isInstanceOf(Air.class, DOWN, RIGHT) && link.isInstanceOf(Air.class, LEFT) && link.isInstanceOf(Air.class, RIGHT)) {
                            stepVelocity.y += 1;
                            if (Math.random() > 0.5)
                                yield link.swap(LEFT);
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
                stepVelocity.y += 1;
                yield link.swap(DOWN);
            }
            case LEFT -> {
                if (!link.isInstanceOf(Air.class, LEFT)){
                    stepVelocity.x = 0;
                    this.velocity.x = 0;
                    yield link;
                }
                if (!link.isInstanceOf(Air.class, DOWN)){
                    this.velocity.x = 0;
                }
                stepVelocity.x += 1;
                yield link.swap(LEFT);
            }
            case RIGHT -> {
                if (!link.isInstanceOf(Air.class, RIGHT)){
                    stepVelocity.x = 0;
                    this.velocity.x = 0;
                    yield link;
                }
                if (!link.isInstanceOf(Air.class, DOWN)){
                    this.velocity.x = 0;
                }
                stepVelocity.x -= 1;
                yield link.swap(RIGHT);
            }
            case NONE -> link;
        };
    }

    @Override
    public void updateGravity(Link link) {
        if (link.isInstanceOf(Air.class, DOWN) || link.isInstanceOf(Particle.class, DOWN))
            this.velocity.y -= this.gravity();
        if (link.isInstanceOf(Fluid.class, DOWN))
            this.velocity.y -= this.gravity()/4;
    }

    @Override
    public Vector getVelocity() {
        return this.velocity;
    }
}
