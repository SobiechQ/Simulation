package Map;

import Elements.Air;
import Elements.Api.Element;

import java.util.Optional;
import java.util.stream.Stream;

public class GridDecorator {
    private final Link[][] grid;
    public GridDecorator(int gridSize) {
        this.grid = new Link[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) { //todo funkcyjnie
            for (int j = 0; j < gridSize; j++) {
                this.grid[i][j] = new Link(j,i, this, new Air());
            }
        }
    }
    public Optional<Element> getElement(int x, int y) { //todo remove na rzecz getLink
        if (this.isOutOfBounds(x, y))
            return Optional.empty();
        return Optional.of(this.grid[y][x].getElement());
    }
    public Optional<Link > getLink(int x, int y) {
        if (this.isOutOfBounds(x, y))
            return Optional.empty();
        return Optional.of(this.grid[y][x]);
    }

    public void set(int x, int y, Element element) {
        if (this.isOutOfBounds(x, y))
            return;
        this.grid[y][x].set(element);
    }
    public boolean isOutOfBounds(int x, int y){ //todo to może być bugiem
        return y < 0 || y >= this.grid.length || x < 0 || x >= this.grid[0].length;
    }
    public void clear(int x, int y){
        this.set(x, y, new Air());
    }
    public Stream<Link> stream(){
        return Stream.of(this.grid).flatMap(Stream::of).sorted((l1, l2) -> (int) (l2.getId() - l1.getId()));
    }


}
