package Elements;

import Elements.Api.Loose;
import Map.Griderator;

import java.awt.*;
import java.util.Set;

public class Sand extends Loose {
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
        this.velocity = new Vector(5, 5);
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
    public void computeVector(Griderator griderator) {
//        System.out.println(this.velocity.getDirection());
        if (griderator.getDown().isPresent() && griderator.getDown().get().current() instanceof Air)
            this.velocity.y = this.velocity.y -1;
        this.computeVector(griderator, griderator, new Vector(this.velocity));
//        System.out.println("==================================");
    }

    public void computeVector(Griderator init, Griderator griderator, Vector vector) {
//        System.out.printf("init [%s]\ngriderator [%s]\nvector [%s]\n", init, griderator, vector);
        if (Math.abs(vector.getX()) <= 0.5 && Math.abs(vector.getY()) <= 0.5){
            init.unsetCurrent();
            griderator.setCurrent(this);
            return;
        }
        switch (vector.getDirection()){
            case UP -> {
                if (griderator.getUp().isPresent() && griderator.getUp().get().current() instanceof Air) {
                    vector.y = vector.y < 0 ? vector.y + 1 : vector.y - 1;
                    this.computeVector(init, griderator.getUp().get(), vector);
                    return;
                }
                vector.y = 0;
                this.velocity.y = 0;
                this.computeVector(init, griderator, vector);
            }
            case DOWN -> {
                if (griderator.getDown().isPresent() && griderator.getDown().get().current() instanceof Air) { //below is air
                    vector.y = vector.y < 0 ? vector.y + 1 : vector.y - 1;
                    this.computeVector(init, griderator.getDown().get(), vector);
                    return;
                }
                if (griderator.getDown().isPresent()) { //below is something but not air
                    if (Math.random() > 0.7 ){
                        this.velocity.y = 0;
                        vector.y = 0;
                        this.computeVector(init, griderator, vector);
                        return;
                    }


                    var downLeft = griderator.getDown().get().getLeft();
                    var downRight = griderator.getDown().get().getRight();
                    if (downLeft.isPresent() && downLeft.get().current() instanceof Air) {
                        vector.y = vector.y < 0 ? vector.y + 1 : vector.y - 1;
                        this.computeVector(init, downLeft.get(), vector);
                        return;
                    }
                    if (downRight.isPresent() && downRight.get().current() instanceof Air) {
                        vector.y = vector.y < 0 ? vector.y + 1 : vector.y - 1;
                        this.computeVector(init, downRight.get(), vector);
                        return;
                    }




                }

                //below is end of the grid
                vector.y = 0;
                this.velocity.y = 0;
                this.computeVector(init, griderator, vector);
            }
            case LEFT -> {
                if (griderator.getLeft().isPresent() && griderator.getLeft().get().current() instanceof Air) {
                    vector.x = vector.x < 0 ? vector.x + 1 : vector.x - 1;
                    this.computeVector(init, griderator.getLeft().get(), vector);
                    return;
                }
                vector.x = 0;
                this.velocity.x = 0;
                this.computeVector(init, griderator, vector);
            }
            case RIGHT -> {
                if (griderator.getRight().isPresent() && griderator.getRight().get().current() instanceof Air) {
                    vector.x = vector.x < 0 ? vector.x + 1 : vector.x - 1;
                    this.computeVector(init, griderator.getRight().get(), vector);
                    return;
                }
                vector.x = 0;
                this.velocity.x = 0;
                this.computeVector(init, griderator, vector);
            }
        }

    }


}
