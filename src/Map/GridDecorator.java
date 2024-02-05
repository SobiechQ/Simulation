package Map;

import Elements.Air;
import Elements.Element;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class GridDecorator {
    private final Element[][] grid;
    public GridDecorator(Element[][] grid){
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
        if (x < 0 || x >= this.grid.length || y < 0 || y >= this.grid[0].length)
            return Optional.empty();
        return Optional.of(this.grid[x][y]);
    }

    public void setElement(int x, int y, Element element) {
        if (x < 0 || x >= this.grid.length || y < 0 || y >= this.grid[0].length)
            return;
        this.grid[x][y] = element;
    }
    public void unsetElement(int x, int y){
        this.setElement(x, y, new Air());
    }

    public Stream<Element> getElements() {
        return Arrays.stream(this.grid).flatMap(Arrays::stream);
    }

    public GridDecorator getGridDecoratorCentered(int deltaX, int deltaY) {
        return new GridDecorator(this.grid) {
            @Override
            public String toString(){
                return deltaX + ", " + deltaY;
            }
            @Override
            public Optional<Element> getElement(int x, int y) {
                return super.getElement(x + deltaX, y + deltaY);
            }

            @Override
            public void setElement(int x, int y, Element element) {
                super.setElement(x + deltaX, y + deltaY, element);
            }

            @Override
            public void unsetElement(int x, int y) {
                this.setElement(x, y, new Air());
            }
        };
    }
}
