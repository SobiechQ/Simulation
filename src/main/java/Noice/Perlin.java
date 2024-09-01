package Noice;

import Map.Link;
import Map.Utils.Vector;
import lombok.NonNull;

import javax.annotation.Nullable;
import java.util.Optional;

public class Perlin {
    private final int blockWidth;
    private final int blockHeight;
    private final Vector topLeft;
    private final Vector topRight;
    private final Vector bottomLeft;
    private final Vector bottomRight;

    @Nullable
    private Perlin bottomPerlin = null;
    @Nullable
    private Perlin rightPerlin = null;

    public Perlin() {
        this(60, 10);
    }

    public Perlin(int blockWidth, int blockHeight) {
        this(blockWidth, blockHeight, Vector.getRandomVector(1), Vector.getRandomVector(1), Vector.getRandomVector(1), Vector.getRandomVector(1));
    }

    private Perlin(int blockWidth, int blockHeight, Vector topLeft, Vector bottomLeft, Vector topRight, Vector bottomRight) {
        this.blockWidth = blockWidth;
        this.blockHeight = blockHeight;
        this.topLeft = topLeft;
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
        this.bottomRight = bottomRight;
    }

    public double getValue(@NonNull Link link) {
        return this.getValue(link.getXReal(), link.getYReal());
    }

    public double getValue(int x, int y) {
        if (x > this.blockWidth || y > this.blockHeight) {
            if (y > this.blockHeight) {



                if (this.bottomPerlin == null)
                    this.bottomPerlin = new Perlin(this.blockWidth, this.blockHeight, this.bottomLeft, Vector.getRandomVector(1), this.bottomRight, Vector.getRandomVector(1));
//                this.bottomPerlin = new Perlin(this.blockWidth, this.blockHeight, this.topLeft, this.bottomLeft, this.topRight, this.bottomRight);
                return this.bottomPerlin.getValue(x, y - this.blockHeight);
            }
            if (this.rightPerlin == null)
                this.rightPerlin = new Perlin(this.blockWidth, this.blockHeight, topRight, bottomRight, Vector.getRandomVector(1), Vector.getRandomVector(1));
            return this.rightPerlin.getValue(x - this.blockWidth, y);

        }


        final var dotTopLeft = this.topLeft.dotProduct(new Vector(x, -y));
        final var dotTopRight = this.topRight.dotProduct(new Vector(x - this.blockWidth, -y));
        final var dotBottomLeft = this.bottomLeft.dotProduct(new Vector(x, -y + this.blockHeight));
        final var dotBottomRight = this.bottomRight.dotProduct(new Vector(x - this.blockWidth, -y + this.blockHeight));

        System.out.println("dotTopLeft: " + dotTopLeft);
        System.out.println("dotTopRight: " + dotTopRight);
        System.out.println("dotBottomLeft: " + dotBottomLeft);
        System.out.println("dotBottomRight: " + dotBottomRight);

        final var xRatio = (double) x / this.blockWidth;
        final var yRatio = (double) y / this.blockHeight;

        final var topInterpolation = this.linearInterpolation(dotTopLeft, dotTopRight, easeCurve(xRatio));
        final var bottomInterpolation = this.linearInterpolation(dotBottomLeft, dotBottomRight, easeCurve(xRatio));
        final var finalInterpolation = this.linearInterpolation(topInterpolation, bottomInterpolation, easeCurve(yRatio));
        return this.arcTanEase(finalInterpolation, 0.2);
    }


    private double arcTanEase(double value, double flatness) {
        final var ease = (Math.atan(value * flatness) / Math.PI) + 0.5;
        return ease < -1 ? -1 : ease > 1 ? 1 : ease;
    }

    private double linearInterpolation(double a1, double a2, double t) {
        if (t < 0 || t > 1) throw new IllegalArgumentException("t must be in range <0; 1>");
        return a1 + t * (a2 - a1);
    }

    private double easeCurve(double t) {
        if (t < 0 || t > 1) throw new IllegalArgumentException("t must be in range <0; 1>");
        return 6 * Math.pow(t, 5) - 15 * Math.pow(t, 4) + 10 * Math.pow(t, 3);
    }
}
