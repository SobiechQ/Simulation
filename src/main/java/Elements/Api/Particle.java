package Elements.Api;

import Elements.Air;
import Map.Link;
import Map.Utils.Vector;

import java.awt.*;

import static Map.Utils.Direction.*;

public abstract class Particle extends Element implements Moveable {
    private final Vector velocity;
    private int timeToLive;
    private final int maxTimeToLive;

    public Particle(double xMin, double xMax, double yMin, double yMax, int timeToLiveMin, int timeToLiveMax) {
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


    //todo fadeable that stores colors so trhey dont have to be created and destroyed every time
    private double getFade(){
        return (double) this.timeToLive / this.maxTimeToLive;
    }
    @Override
    public Color getColor() {
        return new Color(this.getFadedRed(),this.getFadedGreen(), this.getFadedBlue());
    }
    private int getFadedRed() {
        final var red = super.getColor().getRed() * this.getFade();
        return red < 0 ? 0 : (int) (red > 255 ? 255 : red);
    }
    private int getFadedGreen() {
        final var green = super.getColor().getGreen() * this.getFade();
        return green < 0 ? 0 : (int) (green > 255 ? 255 : green);
    }
    private int getFadedBlue() {
        final var blue = super.getColor().getBlue() * this.getFade();
        return blue < 0 ? 0 : (int) (blue > 255 ? 255 : blue);
    }

    @Override
    public Link move(Link link, Vector stepVelocity) {
        if (this.timeToLive <= 0) {
            link.clear();
            stepVelocity.clear();
            this.velocity.clear();
            return link;
        }

        return switch (stepVelocity.getDirection()){
            case UP -> {
                if (link.isInstanceOf(Air.class, UP)){
                    stepVelocity.y -= 1;
                    yield link.swap(UP);
                }
                if (link.isInstanceOf(Air.class, LEFT) && link.isInstanceOf(Air.class, RIGHT)){ //todo!!! może przecież być tylko z jednej opcja poruszania się
                    boolean left = stepVelocity.x < 0;
                    if (stepVelocity.x == 0)
                        left = Math.random() >= 0.5;
                    stepVelocity.y = 0;
                    yield left ? link.swap(LEFT) : link.swap(RIGHT);
                }
                if (link.isInstanceOf(Air.class, LEFT)){
                    stepVelocity.y = 0;
                    yield link.swap(LEFT);
                }
                if (link.isInstanceOf(Air.class, RIGHT)){
                    stepVelocity.y = 0;
                    yield link.swap(RIGHT);
                }
                stepVelocity.y = 0;
                yield link;
            }
            case DOWN -> {
                if (link.isInstanceOf(Air.class, DOWN)){
                    stepVelocity.y += 1;
                    yield link.swap(DOWN);
                }
                stepVelocity.y = 0;
                yield link;
            }
            case LEFT -> {
                if (link.isInstanceOf(Air.class, LEFT)){
                    stepVelocity.x += 1;
                    yield link.swap(LEFT);
                }
                stepVelocity.x = 0;
                yield link;
            }
            case RIGHT -> {
                if (link.isInstanceOf(Air.class, RIGHT)){
                    stepVelocity.x -= 1;
                    yield link.swap(RIGHT);
                }
                stepVelocity.x = 0;
                yield link;
            }
            case NONE -> link;
        };
    }

    @Override
    public void updateGravity(Link link) {
        this.timeToLive--;
    }
}
