package Noice;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class PerlinBuilder {
    private int blockWidth = 40;
    private int blockHeight = 40;
    private int octaveCount = 1;
    private double amplitude = 1;
    private double flatness = 1;
    private double delta = 0;

    public PerlinBuilder setBlockWidth(int blockWidth) {
        this.blockWidth = blockWidth;
        return this;
    }

    public PerlinBuilder setBlockHeight(int blockHeight) {
        this.blockHeight = blockHeight;
        return this;
    }

    public PerlinBuilder setOctaveCount(int octaveCount) {
        this.octaveCount = octaveCount;
        return this;
    }

    public PerlinBuilder setAmplitude(double amplitude) {
        this.amplitude = amplitude;
        return this;
    }

    public PerlinBuilder setFlatness(double flatness) {
        this.flatness = flatness;
        return this;
    }

    public PerlinBuilder setDelta(double delta) {
        this.delta = delta;
        return this;
    }

    public Perlin build(){
        return new Perlin(this);
    }
}
