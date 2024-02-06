package Map;

import Elements.Air;
import Elements.Api.Element;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class GridDecorator {
    private final Element[][] grid;
    public GridDecorator(Element[][] grid) {
        this.grid = grid;
    }
    public GridDecorator(int gridSize) {
        this.grid = new Element[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                this.grid[i][j] = new Air();
            }
        }
    }
    public Optional<Element> getElement(int x, int y) {
        if (this.isOutOfBounds(x, y))
            return Optional.empty();
        return Optional.of(this.grid[x][y]);
    }

    public void setElement(int x, int y, Element element) {
        if (this.isOutOfBounds(x, y))
            return;
        this.grid[x][y] = element;
    }
    public boolean isOutOfBounds(int x, int y){
        return x < 0 || x >= this.grid.length || y < 0 || y >= this.grid[0].length;
    }
    public void unsetElement(int x, int y){
        this.setElement(x, y, new Air());
    }
    public Stream<Element> stream() {
        return Arrays.stream(this.grid).flatMap(Arrays::stream);
    }
    public Griderator getGriderator(int x, int y){
        return new Griderator(x, y) {
            @Override
            public Element current(int x, int y) {
                return GridDecorator.this.getElement(x, y).orElseThrow();
            }

            @Override
            public boolean isOutOfBounds(int x, int y) {
                return GridDecorator.this.isOutOfBounds(x, y);
            }

            @Override
            public Stream<Element> inRadius(int x, int y, int radius) {
                Stream.Builder<Element> builder = Stream.builder();
                for (int i = x - radius; i <= x + radius; i++) {
                    for (int j = y - radius; j <= y + radius; j++) {
                        if (i == x && j == y)
                            continue;
                        GridDecorator.this.getElement(i, j).ifPresent(builder);
                    }
                }
                return builder.build();
            }

            @Override
            public void set(int x, int y, Element element) {
                GridDecorator.this.setElement(x, y, element);
            }
        };
    }


}
