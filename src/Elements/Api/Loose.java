package Elements.Api;

import Elements.Air;
import Map.Link;
import Map.Utils.Direction;
import Map.Utils.Vector;

import java.awt.*;

import static Map.Utils.Direction.*;
import static Map.Utils.Direction.DOWN;

public abstract class Loose extends Element implements Moveable {
    protected abstract double gravity();
    protected abstract double stickness();
    private boolean debug = false;
    private final Vector velocity = new Vector();
    public void setDebug(){
        this.setColor(Color.RED);
        this.debug = true;
    }
    public boolean getDebug(){
        return this.debug;
    }


    @Override
    public void move(Link link) {
        if (link.isInstanceOf(Air.class, DOWN))
            this.velocity.y = this.velocity.y - this.gravity() ;
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
                if (this.isAbleUpOrDown(init, link, vector, UP))
                    return;
                vector.y = 0;
                this.velocity.y = 0;
                this.move(init, link, vector);
            }
            case DOWN -> {
                if (this.isAbleUpOrDown(init, link, vector, DOWN))
                    return;
                if (link.get(DOWN).isPresent()) { //below is something but not air
                    if (Math.random() > this.stickness() ){
                        this.velocity.y = 0;
                        vector.y = 0;
                        this.move(init, link, vector);
                        return;
                    }
                    if (this.isAbleFallLeftOrRight(init, link, vector, LEFT))
                        return;
                    if (this.isAbleFallLeftOrRight(init, link, vector, RIGHT))
                        return;
                }

                //below is end of the grid
                vector.y = 0;
                this.velocity.y = 0;
                this.move(init, link, vector);
            }
            case LEFT -> {
                if (this.isAbleLeftOrRight(init, link, vector, LEFT))
                    return;
                vector.x = 0;
                this.velocity.x = 0;
                this.move(init, link, vector);
            }
            case RIGHT -> {
                if (this.isAbleLeftOrRight(init, link, vector, RIGHT))
                    return;
                vector.x = 0;
                this.velocity.x = 0;
                this.move(init, link, vector);
            }
        }
    }

    private boolean isAbleLeftOrRight(Link init, Link link, Vector vector, Direction direction) {
        if (link.isInstanceOf(Air.class, direction)) {
            vector.x = vector.x < 0 ? vector.x + 1 : vector.x - 1;
            this.move(init, link.get(direction).get(), vector);
            return true;
        }
        return false;
    }

    private boolean isAbleFallLeftOrRight(Link init, Link link, Vector vector, Direction direction) {
        if (link.isInstanceOf(Air.class, DOWN, direction)) {
            vector.y = vector.y < 0 ? vector.y + 1 : vector.y - 1;
            this.move(init, link.get(DOWN, direction).get(), vector);
            return true;
        }
        return false;
    }

    private boolean isAbleUpOrDown(Link init, Link link, Vector vector, Direction direction) {
        if (link.isInstanceOf(Air.class, direction)) {
            vector.y = vector.y < 0 ? vector.y + 1 : vector.y - 1;
            this.move(init, link.get(direction).get(), vector);
            return true;
        }
        return false;
    }

    @Override
    public Vector getVelocity() {
        return velocity;
    }
}
