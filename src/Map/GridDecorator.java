package Map;

import Elements.Air;
import Elements.Api.Element;

import java.util.Optional;

public class GridDecorator {
    private final Link[][] grid;
    public GridDecorator(int gridSize) {
        this.grid = new Link[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                this.grid[i][j] = new Link(j,i, this, new Air());
            }
        }
    }
    public Optional<Element> getElement(int x, int y) {
        if (this.isOutOfBounds(x, y))
            return Optional.empty();
        return Optional.of(this.grid[y][x].getElement());
    }
    public Optional<Link > getLink(int x, int y) {
        if (this.isOutOfBounds(x, y))
            return Optional.empty();
        return Optional.of(this.grid[y][x]);
    }

    public void setElement(int x, int y, Element element) {
        if (this.isOutOfBounds(x, y))
            return;
        this.grid[y][x].set(element);
    }
    public boolean isOutOfBounds(int x, int y){
        return y < 0 || y >= this.grid.length || x < 0 || x >= this.grid[0].length;
    }
    public void unsetElement(int x, int y){
        this.setElement(x, y, new Air());
    }


}
