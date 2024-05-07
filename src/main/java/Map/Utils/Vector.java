package Map.Utils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.Optional;
@EqualsAndHashCode
public class Vector {

    @Getter
    @Setter
    public double x;
    @Getter
    @Setter
    public double y;
    @Nullable
    private final Vector stepVector;

    /**
     * @return Vector with x,y values random. Both values are in range of <-1, 1>
     */
    public static Vector getRandomVector() {
        return new Vector(-1 + Math.random() * 2, -1 + Math.random() * 2);
    }

    public Vector() {
        this(0, 0);
    }

    public Vector(double x, double y) {
        this(x, y, new Vector(0, 0, null));
    }
    public double dotProduct(Vector vector){
        return this.x * vector.x + this.y * vector.y;
    }

    private Vector(double x, double y, @Nullable Vector stepVector) {
        this.x = x;
        this.y = y;
        this.stepVector = stepVector;
    }

    public Direction getDirection() {
        if (Math.abs(this.x) == 0 && Math.abs(this.y) == 0)
            return Direction.NONE;
        double degrees = this.getDegrees();
        if (degrees >= -45 && degrees <= 45)
            return Direction.UP;
        if (degrees > 45 && degrees < 135)
            return Direction.RIGHT;
        if (degrees <= -135 || degrees >= 135)
            return Direction.DOWN;
        return Direction.LEFT;
    }

    public double getDegrees() {
        return Math.toDegrees(Math.atan2(this.x, this.y));
    }

    public void addVector(double x, double y) {
        this.addX(x);
        this.addY(y);
    }

    public boolean step() {
        return Math.abs(this.x) >= 1 || Math.abs(this.y) >= 1;
    }

    public double getLength() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }


    public Optional<Vector> getStepVector() {
        return Optional.ofNullable(this.stepVector);
    }

    public void clear() {
        this.x = 0;
        this.y = 0;
        this.getStepVector().ifPresent(Vector::clear);
    }

    public void addX(double x) {
        this.x += x;
    }

    public void addY(double y) {
        this.y += y;
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", this.x, this.y);
    }

}




