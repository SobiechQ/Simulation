package Elements.Api;

import Elements.Air;
import Map.Link;
import Map.Utils.Direction;
import Map.Utils.Vector;

import java.awt.*;

import static Map.Utils.Direction.*;
import static Map.Utils.Direction.DOWN;

public abstract class Fluid extends Element implements Moveable {
    protected abstract double gravity();

    private boolean debug = false;

    private final Vector velocity = new Vector();

    public void setDebug() {
        this.setColor(Color.RED);
        this.debug = true;
    }

    public boolean getDebug() {
        return this.debug;
    }

    @Override
    public void move(Link link) {
        boolean isLeftAir = link.isInstanceOf(Air.class, LEFT);
        boolean isRightAir = link.isInstanceOf(Air.class, RIGHT);
        boolean isBelowAir = link.isInstanceOf(Air.class, DOWN);
        if (!isBelowAir) {
            if (Math.abs(this.velocity.x) != 1 && isLeftAir && isRightAir)
                this.velocity.x = Math.random() > 0.5 ? 1 : -1;
            else if (this.velocity.x != 1 && isLeftAir) {
                this.velocity.x = -1;
            } else if (this.velocity.x != -1 && isRightAir) {
                this.velocity.x = 1;
            }
        }


        this.velocity.y -= this.gravity();
        this.move(link, link, new Vector(this.velocity));
    }

    public void move(Link init, Link link, Vector vector) {
        if (Math.abs(vector.x) < 1 && Math.abs(vector.y) < 1){
            init.clear();
            link.set(this);
            return;
        }

        switch (vector.getDirection()) {
            case UP -> {
                if (link.isInstanceOf(Air.class, UP)) {
                    vector.y--;
                    link = link.get(UP).get();
                } else {
                    vector.y = 0;
                    this.velocity.y = 0;
                }
                this.move(init, link, vector);
            }
            case DOWN -> {
                if (link.isInstanceOf(Air.class, DOWN)) {
                    vector.y++;
                    link = link.get(DOWN).get();
                } else {
                    vector.y = 0;
                    this.velocity.y = 0;
                }
                this.move(init, link, vector);
            }
            case LEFT -> {
                if (link.isInstanceOf(Air.class, LEFT)) {
                    vector.x++;
                    link = link.get(LEFT).get();
                } else {
                    vector.x = 0;
                    this.velocity.x = 0;
                }
                this.move(init, link, vector);
            }
            case RIGHT -> {
                if (link.isInstanceOf(Air.class, RIGHT)) {
                    vector.x--;
                    link = link.get(RIGHT).get();
                } else {
                    vector.x = 0;
                    this.velocity.x = 0;
                }
                this.move(init, link, vector);
            }
        }
    }

    private void linkSwitch(Link init, Link link, Vector vector, Direction direction) {
        switch (direction) {
            case UP -> vector.y--;
            case DOWN -> vector.y++;
            case LEFT -> vector.x--;
            case RIGHT -> vector.x++;
        }
        var copy = link.getElement();
        init.clear();
        link.clear();
        link.set(copy);
    }

    @Override
    public Vector getVelocity() {
        return this.velocity;
    }
}
