package Elements;

import Elements.Api.Loose;
import Map.Direction;
import Map.Link;

import java.awt.*;
import java.util.Set;

import static Map.Direction.*;

public class Sand extends Loose {
    private final static boolean DEBUG = false;
    private final static Set<Color> COLORS = Set.of(
            new Color(212, 203, 147),
            new Color(210, 200, 144),
            new Color(226, 223, 188),
            new Color(255, 247, 202),
            new Color(252, 250, 235),
            new Color(177, 169, 115),
            new Color(231, 219, 177)
    );
    private final Color color;
    private final Vector velocity;

    public Sand() {
        //random color
        this.color = COLORS.stream().skip((int) (COLORS.size() * Math.random())).findFirst().get();
        this.velocity = new Vector();
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public Vector getVelocity() {
        return this.velocity;
    }

    @Override
    public void computeVector(Link link) {
        this.log(this.velocity.getDirection().toString());
        if (link.get(DOWN).isPresent() && link.get(DOWN).get().getElement() instanceof Air)
            this.velocity.y = this.velocity.y -1;
        this.computeVector(link, link, new Vector(this.velocity));
        this.log("===========================");
    }

    public void computeVector(Link init, Link link, Vector vector) {
        this.log("====step====");
        this.log(String.format("init[%s]\n\nlink [%s]\n\nvector [%s]\nvelocity [%s]\n", init, link, vector, this.velocity));
        this.log("====step====");
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
                this.computeVector(init, link, vector);
            }
            case DOWN -> {
                if (this.isAbleUpOrDown(init, link, vector, DOWN))
                    return;
                if (link.get(DOWN).isPresent()) { //below is something but not air
                    if (Math.random() > 0.8 ){
                        this.velocity.y = 0;
                        vector.y = 0;
                        this.computeVector(init, link, vector);
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
                this.computeVector(init, link, vector);
            }
            case LEFT -> {
                if (this.isAbleLeftOrRight(init, link, vector, LEFT))
                    return;
                vector.x = 0;
                this.velocity.x = 0;
                this.computeVector(init, link, vector);
            }
            case RIGHT -> {
                if (this.isAbleLeftOrRight(init, link, vector, RIGHT))
                    return;
                vector.x = 0;
                this.velocity.x = 0;
                this.computeVector(init, link, vector);
            }
        }

    }

    private boolean isAbleLeftOrRight(Link init, Link link, Vector vector, Direction direction) {
        if (link.get(direction).isPresent() && link.get(direction).get().getElement() instanceof Air) {
            vector.x = vector.x < 0 ? vector.x + 1 : vector.x - 1;
            this.computeVector(init, link.get(direction).get(), vector);
            return true;
        }
        return false;
    }

    private boolean isAbleFallLeftOrRight(Link init, Link link, Vector vector, Direction direction) {
        if (link.get(DOWN, direction).isPresent() && link.get(DOWN, direction).get().getElement() instanceof Air) {
            vector.y = vector.y < 0 ? vector.y + 1 : vector.y - 1;
            this.computeVector(init, link.get(DOWN, direction).get(), vector);
            return true;
        }
        return false;
    }

    private boolean isAbleUpOrDown(Link init, Link link, Vector vector, Direction direction) {
        if (link.get(direction).isPresent() && link.get(direction).get().getElement() instanceof Air) {
            vector.y = vector.y < 0 ? vector.y + 1 : vector.y - 1;
            this.computeVector(init, link.get(direction).get(), vector);
            return true;
        }
        return false;
    }

    public void log(String message){
        if (DEBUG)
            System.out.println(message);
    }
    public void setVelocity(double x, double y){
        this.velocity.x = x;
        this.velocity.y = y;
    }


}
