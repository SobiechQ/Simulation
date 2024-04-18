package Elements.Api;

import Elements.Air;
import Map.Link;
import Map.Utils.Direction;
import Map.Utils.Vector;

import java.awt.*;

import static Map.Utils.Direction.*;
import static Map.Utils.Direction.UP;

public abstract class Particle extends Element implements Moveable{
    private final Vector velocity;
    private final boolean debug = false;
    private double timeToLive;
    private final double maxTimeToLive;

    public Particle(double xMin, double xMax, double yMin, double yMax, double timeToLiveMin, double timeToLiveMax) {
        double xVelocity = xMin + Math.random()*(xMax - xMin);
        double yVelocity = yMin + Math.random()*(yMax - yMin);
        this.maxTimeToLive = (int) (timeToLiveMin + Math.random()*(timeToLiveMax - timeToLiveMin));
        this.velocity = new Vector(xVelocity, yVelocity);
        this.timeToLive = maxTimeToLive;
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

    private double getFade(){
        return this.timeToLive/this.maxTimeToLive;
    }
    @Override
    public Color getColor() {
        return new Color((int) (super.getColor().getRed() * this.getFade()), (int) (super.getColor().getGreen() * this.getFade()), (int) (super.getColor().getBlue() * getFade()));
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
                    this.moveDiagonalUp(init, link, vector, RIGHT);
                }
            }
            case DOWN -> this.moveYAxis(init, link, vector, DOWN);
            case LEFT -> this.moveXAxis(init, link, vector, LEFT);
            case RIGHT -> this.moveXAxis(init, link, vector, RIGHT);
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
