package Noice;

import Map.Link;
import Map.Utils.Vector;
import lombok.Data;
import lombok.NonNull;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Perlin {
    private final int blockWidth;
    private final int blockHeight;
    private final HashMap<Tuple2<Integer, Integer>, PerlinSegment> segmentMap = new HashMap<>();
    @Nullable
    private final Perlin octave;
    private final double amplitude;
    private final double flatness;
    private final double delta;
    @Data
    private class PerlinSegment {
        private final Vector topLeft;
        private final Vector topRight;
        private final Vector bottomLeft;
        private final Vector bottomRight;
        private double getValue(int x, int y){
            if (x >= Perlin.this.blockWidth || y >= Perlin.this.blockHeight)
                throw new IllegalArgumentException("X and Y have to be lower then block height or width");
            final var dotTopLeft = this.topLeft.dotProduct(new Vector(x, -y));
            final var dotTopRight = this.topRight.dotProduct(new Vector(x - Perlin.this.blockWidth, -y));
            final var dotBottomLeft = this.bottomLeft.dotProduct(new Vector(x, -y + Perlin.this.blockHeight));
            final var dotBottomRight = this.bottomRight.dotProduct(new Vector(x - Perlin.this.blockWidth, -y + Perlin.this.blockHeight));

            final var xRatio = (double) x / Perlin.this.blockWidth;
            final var yRatio = (double) y / Perlin.this.blockHeight;

            final var topInterpolation = this.linearInterpolation(dotTopLeft, dotTopRight, easeCurve(xRatio));
            final var bottomInterpolation = this.linearInterpolation(dotBottomLeft, dotBottomRight, easeCurve(xRatio));
            final var finalInterpolation = this.linearInterpolation(topInterpolation, bottomInterpolation, easeCurve(yRatio));
            return this.arcTanEase(finalInterpolation);
        }
        private double arcTanEase(double value) {
            final var ease = (Math.atan((value + Perlin.this.delta) * Perlin.this.flatness) / Math.PI) + 0.5;
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
    protected Perlin(PerlinBuilder builder){
        this.blockWidth = Math.max(builder.getBlockWidth(), 1);
        this.blockHeight = Math.max(builder.getBlockHeight(), 1);
        this.amplitude = builder.getAmplitude();
        this.flatness = builder.getFlatness();
        this.delta = builder.getDelta();

        this.octave = builder.getOctaveCount() > 1 ? new Perlin(
                new PerlinBuilder()
                        .setBlockWidth((int) (this.blockWidth * 0.7))
                        .setBlockHeight((int) (this.blockHeight * 0.7))
                        .setOctaveCount(builder.getOctaveCount() - 1)
                        .setAmplitude(this.amplitude / 2)
                        .setFlatness(this.flatness)
                        .setDelta(this.delta)
        ) : null;
    }

    public double getValue(@NonNull Link link) {
        return this.getValue(link.getXReal(), link.getYReal());
    }

    public double getValue(int x, int y) {
        final var tuple = Tuple.tuple(x / this.blockWidth, y / this.blockHeight);

        this.segmentMap.computeIfAbsent(tuple, t -> {
            final var topLeft = Stream.of(
                    Optional.ofNullable(this.segmentMap.get(Tuple.tuple(t.v1-1, t.v2))).map(p->p.topRight),
                    Optional.ofNullable(this.segmentMap.get(Tuple.tuple(t.v1, t.v2-1))).map(p->p.bottomLeft),
                    Optional.ofNullable(this.segmentMap.get(Tuple.tuple(t.v1-1, t.v2-1))).map(p->p.bottomRight)
            )
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findAny()
                    .orElse(Vector.getRandomVector(1));

            final var topRight = Stream.of(
                            Optional.ofNullable(this.segmentMap.get(Tuple.tuple(t.v1, t.v2-1))).map(p->p.bottomRight),
                            Optional.ofNullable(this.segmentMap.get(Tuple.tuple(t.v1+1, t.v2-1))).map(p->p.bottomLeft),
                            Optional.ofNullable(this.segmentMap.get(Tuple.tuple(t.v1+1, t.v2))).map(p->p.topLeft)
                    )
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findAny()
                    .orElse(Vector.getRandomVector(1));

            final var bottomLeft = Stream.of(
                            Optional.ofNullable(this.segmentMap.get(Tuple.tuple(t.v1-1, t.v2))).map(p->p.bottomRight),
                            Optional.ofNullable(this.segmentMap.get(Tuple.tuple(t.v1-1, t.v2+1))).map(p->p.topRight),
                            Optional.ofNullable(this.segmentMap.get(Tuple.tuple(t.v1, t.v2+1))).map(p->p.topLeft)
                    )
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findAny()
                    .orElse(Vector.getRandomVector(1));

            final var bottomRight = Stream.of(
                            Optional.ofNullable(this.segmentMap.get(Tuple.tuple(t.v1, t.v2+1))).map(p->p.topRight),
                            Optional.ofNullable(this.segmentMap.get(Tuple.tuple(t.v1+1, t.v2+1))).map(p->p.topLeft),
                            Optional.ofNullable(this.segmentMap.get(Tuple.tuple(t.v1+1, t.v2))).map(p->p.bottomLeft)
                    )
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findAny()
                    .orElse(Vector.getRandomVector(1));

            return new PerlinSegment(topLeft, topRight, bottomLeft, bottomRight);
        });

        final double thisValue = Optional.ofNullable(this.segmentMap.get(tuple))
                .map(s->s.getValue(x%this.blockWidth, y%this.blockHeight))
                .orElse(0.0);

        if (this.octave == null)
            return thisValue * this.amplitude;

        return (thisValue * this.amplitude + this.octave.getValue(x, y) * this.octave.amplitude) / (this.amplitude + this.octave.amplitude);
    }



}
