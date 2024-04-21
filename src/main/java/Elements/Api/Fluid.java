package Elements.Api;

import Elements.Air;
import Map.Link;
import Map.Utils.Vector;

import static Map.Utils.Direction.*;

public abstract class Fluid extends Element implements Moveable {
    protected abstract double getGravity();
    protected abstract double getStickness(); 

    private final Vector velocity = new Vector();


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
                if (link.isInstanceOf(Air.class, DOWN)){
                    stepVelocity.y += 1;
                    yield link.swap(DOWN);
                }
                if (link.isInstanceOf(Air.class, LEFT) && link.isInstanceOf(Air.class, RIGHT)){
                    boolean left = stepVelocity.x < 0;
                    if (stepVelocity.x == 0)
                        left = Math.random() >= 0.5;
                    this.velocity.x = (left ? -1 : 1) * this.getStickness();
                    stepVelocity.y = 0;
                    yield link;
                }
                if (link.isInstanceOf(Air.class, LEFT)){
                    this.velocity.x = -this.getStickness();
                    stepVelocity.y=0;
                    yield link;
                }
                if (link.isInstanceOf(Air.class, RIGHT)){
                    this.velocity.x = this.getStickness();
                    stepVelocity.y=0;
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

    @Override
    public void updateGravity(Link link) {
        if (link.isInstanceOf(Air.class, DOWN)) this.velocity.y -= this.getGravity();
        if (link.isInstanceOf(Fluid.class, DOWN)) this.velocity.y -= this.getGravity()/4;
    }

    @Override
    public Vector getVelocity() {
        return this.velocity;
    }
}
