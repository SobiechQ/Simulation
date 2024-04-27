package Map.Utils;

import java.util.Objects;

public class Vector {

    public double x;
    public double y;
    public double stepX;
    public double stepY;

    public Vector(){
        this(0,0);
    }
    public Vector(Vector vector){
        this(vector.x, vector.y, vector.stepX, vector.stepY);
    }
    public Vector(double x, double y){
        this(x, y, 0, 0);
    }
    public Vector(double x, double y, double stepX, double stepY) {
        this.x = x;
        this.y = y;
        this.stepX = stepX;
        this.stepY = stepY;
    }

    public double getX() {
        return x;
    }
    public void clear(){
        this.x = 0;
        this.y = 0;
        this.stepX = 0;
        this.stepY = 0;
    }

    public void setX(double x) {
        this.x = x;
    }
    public void addX(double x) {
        this.x += x;
    }
    public double getY() {
        return y;
    }
    public void addY(double y) {
        this.y+=y;
    }
    public void addVector(double x, double y){
        this.addX(x);
        this.addY(y);
    }

    public void setY(double y) {
        this.y = y;
    }
    public Direction getDirection(){
        if (Math.abs(this.x) == 0  && Math.abs(this.y) == 0)
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

    public double getLength() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public boolean step() {
        return Math.abs(this.x) >= 1 || Math.abs(this.y) >= 1;
    }

    public double getStepX() {
        return stepX;
    }

    public void setStepX(double stepX) {
        this.stepX = stepX;
    }

    public double getStepY() {
        return stepY;
    }

    public void setStepY(double stepY) {
        this.stepY = stepY;
    }
}




