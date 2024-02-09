package Map.Utils;

import java.util.Objects;

public class Vector {

    public double x;
    public double y;
    public Vector(double x, double y){
        this.x = x;
        this.y = y;
    }
    public Vector(){
        this(0,0);
    }
    public Vector(Vector vector){
        this(vector.x, vector.y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    public Direction getDirection(){
        if (this.x == 0 && this.y == 0)
            return Direction.NONE;
        double degrees = this.getInDegrees();
        if (degrees >= -45 && degrees <= 45)
            return Direction.UP;
        if (degrees > 45 && degrees < 135)
            return Direction.RIGHT;
        if (degrees <= -135 || degrees >= 135)
            return Direction.DOWN;
        return Direction.LEFT;
    }
    public double getInDegrees(){
        return Math.toDegrees(Math.atan2(this.x, this.y));
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return Double.compare(x, vector.x) == 0 && Double.compare(y, vector.y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", this.x, this.y);
    }
}




