package Elements;

import Elements.Api.Loose;
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
        this.velocity = new Vector(0, 0);
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
                if (link.get(UP).isPresent() && link.get(UP).get().getElement() instanceof Air) {
                    vector.y = vector.y < 0 ? vector.y + 1 : vector.y - 1;
                    this.computeVector(init, link.get(UP).get(), vector);
                    return;
                }
                vector.y = 0;
                this.velocity.y = 0;
                this.computeVector(init, link, vector);
            }
            case DOWN -> {
                if (link.get(DOWN).isPresent() && link.get(DOWN).get().getElement() instanceof Air) { //below is air
                    vector.y = vector.y < 0 ? vector.y + 1 : vector.y - 1;
                    this.computeVector(init, link.get(DOWN).get(), vector);
                    return;
                }
                if (link.get(DOWN).isPresent()) { //below is something but not air
                    if (Math.random() > 0.7 ){
                        this.velocity.y = 0;
                        vector.y = 0;
                        this.computeVector(init, link, vector);
                        return;
                    }
                    if (link.get(DOWN, LEFT).isPresent() && link.get(DOWN, LEFT).get().getElement() instanceof Air) {
                        vector.y = vector.y < 0 ? vector.y + 1 : vector.y - 1;
                        this.computeVector(init, link.get(DOWN, LEFT).get(), vector);
                        return;
                    }
                    if (link.get(DOWN, RIGHT).isPresent() && link.get(DOWN, RIGHT).get().getElement() instanceof Air) {
                        vector.y = vector.y < 0 ? vector.y + 1 : vector.y - 1;
                        this.computeVector(init, link.get(DOWN, RIGHT).get(), vector);
                        return;
                    }
                }

                //below is end of the grid
                vector.y = 0;
                this.velocity.y = 0;
                this.computeVector(init, link, vector);
            }
            case LEFT -> {
                if (link.get(LEFT).isPresent() && link.get(LEFT).get().getElement() instanceof Air) {
                    vector.x = vector.x < 0 ? vector.x + 1 : vector.x - 1;
                    this.computeVector(init, link.get(LEFT).get(), vector);
                    return;
                }
                vector.x = 0;
                this.velocity.x = 0;
                this.computeVector(init, link, vector);
            }
            case RIGHT -> {
                if (link.get(RIGHT).isPresent() && link.get(RIGHT).get().getElement() instanceof Air) {
                    vector.x = vector.x < 0 ? vector.x + 1 : vector.x - 1;
                    this.computeVector(init, link.get(RIGHT).get(), vector);
                    return;
                }
                vector.x = 0;
                this.velocity.x = 0;
                this.computeVector(init, link, vector);
            }
        }

    }
    public void log(String message){
        if (DEBUG)
            System.out.println(message);
    }


}
