package Perlin;

import Map.Utils.Vector;

import java.util.Arrays;

public class Noice{

    private final static int RESOLUTION = 16;
    public final Vector[][] vectors;
    public Noice(int size){ //todo autoscaleable

        this.vectors = new Vector[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                final var vector = Vector.getRandomVector();
//                if (j%4==0)
//                    vector.setX(-1 + Math.random() * 2);
                vector.multiplyBoth(RESOLUTION);
                this.vectors[i][j] = vector;
            }
    }

    public double getValue(int x, int y){
        try {
            return this.calculateGrid(
                    this.vectors[y / RESOLUTION][x / RESOLUTION],
                    this.vectors[y / RESOLUTION ][x / RESOLUTION + 1],
                    this.vectors[y / RESOLUTION +1][x / RESOLUTION ],
                    this.vectors[y / RESOLUTION + 1][x / RESOLUTION + 1],
                    x % RESOLUTION,
                    y % RESOLUTION
            );
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }
    private double calculateGrid(Vector topLeft, Vector topRight, Vector bottomLeft, Vector bottomRight, int localX, int localY){
        if (localX >= RESOLUTION || localY >= RESOLUTION)
            throw new IllegalArgumentException("localX and localY must be in range <0; RESOLUTION)");

        final var pointVectorTopLeft = new Vector(localX, -localY);
        final var pointVectorTopRight = new Vector(localX - RESOLUTION, -localY);
        final var pointVectorBottomLeft = new Vector(localX, -localY + RESOLUTION);
        final var pointVectorBottomRight = new Vector(localX - RESOLUTION, -localY + RESOLUTION);

        final var dotTopLeft = topLeft.dotProduct(pointVectorTopLeft);
        final var dotTopRight = topRight.dotProduct(pointVectorTopRight);
        final var dotBottomLeft = bottomLeft.dotProduct(pointVectorBottomLeft);
        final var dotBottomRight = bottomRight.dotProduct(pointVectorBottomRight);


        return this.mapResultToRange(this.linearInterpolation(
                this.linearInterpolation(dotBottomLeft, dotTopLeft, this.easeCurve(1- (double) localY / RESOLUTION)),
                this.linearInterpolation(dotBottomRight, dotTopRight, this.easeCurve(1 - (double) localY / RESOLUTION)),
                this.easeCurve((double) localX / RESOLUTION)
        ));
    }
    private double mapResultToRange(double x){
        final var result = (2 * Math.atan(0.05 * x)) / Math.PI;
        return result < -1 ? -1 : result > 1 ? 1 : result;
    }

    private double linearInterpolation(double a1, double a2, double t) {
        if (t < 0 || t > 1)
            throw new IllegalArgumentException("t must be in range <0; 1>");
        return a1 + t * (a2 - a1);
    }
    private double easeCurve(double t){
        if (t < 0 || t > 1)
            throw new IllegalArgumentException("t must be in range <0; 1>");
        return 6 * Math.pow(t, 5) - 15 * Math.pow(t, 4) + 10 * Math.pow(t, 3);
    }

    public static void main(String[] args) {
        final var noice = new Noice(20);
        noice.print();
    }
    public void print(){
        for (int y = 0; y < vectors.length; y++) {
            for (int x = 0; x < vectors[y].length; x++) {
                System.out.printf("%f ", this.getValue(x * RESOLUTION, y * RESOLUTION));
            }
            System.out.println();
        }
        System.out.println("------------");
        for (Vector[] vector : this.vectors) {
            for (Vector vector1 : vector) {
                System.out.printf("%s ", vector1);
            }
            System.out.println("");
        }


    }

}
