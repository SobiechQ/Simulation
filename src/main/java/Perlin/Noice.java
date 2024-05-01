package Perlin;

import Map.Utils.Vector;

public class Noice {

    private final Permutation permutation;
    private final int resolution;
    private final double mapingIndex;
    private final double delta;

    public Noice(int resolution, double mapingIndex, double delta) {
        this.delta = delta;
        this.mapingIndex = mapingIndex;
        this.resolution = resolution;
        this.permutation = new Permutation();
    }

    private Noice() {
        this(16, 0.01, 0);
    }

    public double getValue(int x, int y) {
        return this.calculateGrid(
                this.permutation.getPermutation(x / resolution, y / resolution),
                this.permutation.getPermutation(x / resolution + 1, y / resolution),
                this.permutation.getPermutation(x / resolution, y / resolution + 1),
                this.permutation.getPermutation(x / resolution + 1, y / resolution + 1),
                x % resolution,
                y % resolution
        );
    }

    private double calculateGrid(Vector topLeft, Vector topRight, Vector bottomLeft, Vector bottomRight, int localX, int localY) {
        if (localX >= resolution || localY >= resolution)
            throw new IllegalArgumentException("localX and localY must be in range <0; RESOLUTION)");

        final var pointVectorTopLeft = new Vector(localX, -localY);
        final var pointVectorTopRight = new Vector(localX - resolution, -localY);
        final var pointVectorBottomLeft = new Vector(localX, -localY + resolution);
        final var pointVectorBottomRight = new Vector(localX - resolution, -localY + resolution);

        final var dotTopLeft = topLeft.dotProduct(pointVectorTopLeft);
        final var dotTopRight = topRight.dotProduct(pointVectorTopRight);
        final var dotBottomLeft = bottomLeft.dotProduct(pointVectorBottomLeft);
        final var dotBottomRight = bottomRight.dotProduct(pointVectorBottomRight);


        return this.mapResultToRange(this.linearInterpolation(
                this.linearInterpolation(dotBottomLeft, dotTopLeft, this.easeCurve(1 - (double) localY / resolution)),
                this.linearInterpolation(dotBottomRight, dotTopRight, this.easeCurve(1 - (double) localY / resolution)),
                this.easeCurve((double) localX / resolution)

        ) + this.delta);

    }

    private double mapResultToRange(double x) {
        final var result = (Math.atan(this.mapingIndex * x) / Math.PI) + 0.5;
        return result < -1 ? -1 : result > 1 ? 1 : result;
    }
    //todo unmaped getter?

    private double linearInterpolation(double a1, double a2, double t) {
        if (t < 0 || t > 1)
            throw new IllegalArgumentException("t must be in range <0; 1>");
        return a1 + t * (a2 - a1);
    }

    private double easeCurve(double t) {
        if (t < 0 || t > 1)
            throw new IllegalArgumentException("t must be in range <0; 1>");
        return 6 * Math.pow(t, 5) - 15 * Math.pow(t, 4) + 10 * Math.pow(t, 3);
    }
}
