package Elements.Particles;

import Elements.Air;
import Elements.Api.Moveable;
import Elements.Api.Particle;
import Map.Link;
import Map.Utils.Direction;
import Map.Utils.Vector;

import java.awt.*;

import static Map.Utils.Direction.*;
import static Map.Utils.Direction.RIGHT;

public class SandParticle extends Particle {
    private final Vector velocity = new Vector((Math.random()*2 - 1), 1);
    private final boolean debug = false;
    private int timeToLive = (int) (Math.random()*40 + 10);

    public SandParticle() {

    }
    public SandParticle(Color color) {
        this.setColor(color);
    }


    @Override
    public Vector getVelocity() {
        return this.velocity;
    }

    @Override
    public void move(Link link) {
        if (this.timeToLive-- <= 0) {
            link.clear();
            return;
        }
        this.move(link, link, new Vector(this.velocity));
    }

    public void move(Link init, Link link, Vector vector) {
        if (this.debug) {
            System.out.println(link);
            System.out.println(this.getVelocity());
            System.out.println(vector);
            System.out.println("==========");
        }
        if (Math.abs(vector.getX()) <= 0.5 && Math.abs(vector.getY()) <= 0.5){
            init.clear();
            link.set(this);
            return;
        }
        switch (vector.getDirection()){
            case UP -> {
                if (this.moveYAxis(init, link, vector, UP))
                    return;
                if (link.get(UP).isPresent()) { //above is something but not air
                    if (this.moveDiagonalUp(init, link, vector, LEFT))
                        return;
                    if (this.moveDiagonalUp(init, link, vector, RIGHT))
                        return;
                }
                return;
//                vector.y = 0;
//                this.move(init, link, vector);
            }
            case DOWN -> {
                if (this.moveYAxis(init, link, vector, DOWN))
                    return;

                //below is end of the grid
//                vector.y = 0;
//                this.move(init, link, vector);
                return;
            }
            case LEFT -> {
                if (this.moveXAxis(init, link, vector, LEFT))
                    return;
//                vector.x = 0;
//                this.move(init, link, vector);
                return;
            }
            case RIGHT -> {
                if (this.moveXAxis(init, link, vector, RIGHT))
                    return;
//                vector.x = 0;
//                this.move(init, link, vector);
                return;
            }
        }
    }

    private boolean moveXAxis(Link init, Link link, Vector vector, Direction direction) {
        if (link.isInstanceOf(Air.class, direction)) {
            vector.x = vector.x < 0 ? vector.x + 1 : vector.x - 1;
            this.move(init, link.get(direction).get(), vector);
            return true;
        }
        return false;
    }

    private boolean moveDiagonalUp(Link init, Link link, Vector vector, Direction direction) {
        if (link.isInstanceOf(Air.class, UP, direction)) {
            vector.y = vector.y < 0 ? vector.y + 1 : vector.y - 1;
            this.move(init, link.get(UP, direction).get(), vector);
            return true;
        }
        return false;
    }

    private boolean moveYAxis(Link init, Link link, Vector vector, Direction direction) {
        if (link.isInstanceOf(Air.class, direction)) {
            vector.y = vector.y < 0 ? vector.y + 1 : vector.y - 1;
            this.move(init, link.get(direction).get(), vector);
            return true;
        }
        return false;
    }
}
