package Map.Utils;

import Map.Link;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Vector class. Represents a vector in 2D space.
 * Contains stepVector which is a vector that can be used to calculate unused velocity from previous steps,
 * as described {@link Elements.Api.Moveable#refresh(Link)}  here}.
 */
@EqualsAndHashCode
public class Vector {
    /**
     * Latitude of the vector.
     */
    @Getter
    @Setter
    public double x;
    /**
     * Longitude of the vector.
     */
    @Getter
    @Setter
    public double y;
    @Nullable
    private final Vector stepVector;

    /**
     * Creates new vector with x,y values set to 0.
     */
    public Vector() {
        this(0, 0);
    }

    /**
     * Creates new vector with given x,y values, and stepVector set to 0.
     *
     * @param x latitude of the vector
     * @param y longitude of the vector
     */

    public Vector(double x, double y) {
        this(x, y, new Vector(0, 0, null));
    }

    /**
     * Creates new vector with given angle and length.*
     * @param angle  angle of the vector in degrees
     * @param length length of the vector
     * @param flag   to separate from the other constructor
     */
    public Vector(double angle, double length, boolean flag) {
        this(length * Math.sin(Math.toRadians(angle)), length * Math.cos(Math.toRadians(angle)));
    }

    /**
     * Creates new vector with given x,y values, and stepVector set to given stepVector.
     *
     * @param x          latitude of the vector
     * @param y          longitude of the vector
     * @param stepVector vector that can be used to calculate unused velocity from previous steps, can be null
     */
    private Vector(double x, double y, @Nullable Vector stepVector) {
        this.x = x;
        this.y = y;
        this.stepVector = stepVector;
    }

    /**
     * Generates new random vector with x,y values in range of < -1, 1 >
     *
     * @return Vector with x,y values random. Both values are in range of < -1, 1 >
     */
    public static Vector getRandomVector() {
        return new Vector(-1 + Math.random() * 2, -1 + Math.random() * 2);
    }

    /**
     * Generates new vector with random angle and given fixed length.
     * @param length length of the vector
     * @return Vector with random angle and given fixed length
     */
    public static Vector getRandomVector(double length) {
        return new Vector(Math.random() * 360, length, true);
    }

    /**
     * Calculates dot product of this vector and given vector.
     *
     * @param vector vector to calculate dot product with
     * @return dot product of this vector and given vector
     */
    public double dotProduct(Vector vector) {
        return this.x * vector.x + this.y * vector.y;
    }

    /**
     * Calculates direction in which the vector is pointing.
     *
     * @return Direction in which the vector is pointing
     */
    public Direction getDirection() {
        if (Math.abs(this.x) == 0 && Math.abs(this.y) == 0)
            return Direction.NONE;
        double degrees = this.getAngle();
        if (degrees >= -45 && degrees <= 45)
            return Direction.UP;
        if (degrees > 45 && degrees < 135)
            return Direction.RIGHT;
        if (degrees <= -135 || degrees >= 135)
            return Direction.DOWN;
        return Direction.LEFT;
    }

    /**
     * Calculates angle in which the vector is pointing in degrees.
     *
     * @return Direction in which the vector is pointing in degrees < 0 - 360 ) where: 0 is up, 90 is right, 180 is down, 270 is left
     */
    public double getAngle() {
        return Math.toDegrees(Math.atan2(this.x, this.y));
    }

    /**
     * Changes the direction of the vector by given values
     *
     * @param x latitude to add
     * @param y longitude to add
     */
    public void addVector(double x, double y) {
        this.addX(x);
        this.addY(y);
    }

    /**
     * Checks if the vector is applicable to be used as a step.
     * Only vectors with x or y values greater than 1 are applicable.
     *
     * @return true if the vector is applicable to be used as a step, false otherwise
     */
    public boolean isStepApplicable() {
        return Math.abs(this.x) >= 1 || Math.abs(this.y) >= 1;
    }

    /**
     * Calculates the length of the vector.
     *
     * @return length of the vector
     */
    public double getLength() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    /**
     * Returns the stepVector for unused velocity from previous steps.
     * If invoked for stepVector, returns empty Optional.
     *
     * @return stepVector or empty Optional if invoked for stepVector
     */
    public Optional<Vector> getStepVector() {
        return Optional.ofNullable(this.stepVector);
    }

    /**
     * Clears the vector by setting x,y values to 0.
     * If the vector has stepVector, it also clears the stepVector.
     */
    public void clear() {
        this.x = 0;
        this.y = 0;
        this.getStepVector().ifPresent(Vector::clear);
    }

    /**
     * Adds given x value to the vector.
     *
     * @param x latitude to add
     */
    public void addX(double x) {
        this.x += x;
    }

    /**
     * Adds given y value to the vector.
     *
     * @param y longitude to add
     */
    public void addY(double y) {
        this.y += y;
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", this.x, this.y);
    }

}




