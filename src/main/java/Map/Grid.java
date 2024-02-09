package Map;

import Elements.Air;
import Elements.Api.Element;
import Elements.Api.Refreshable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Grid {
    private final Link[][] grid;

    public Grid(int gridSize) {
        this.grid = new Link[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                this.grid[i][j] = new Link(j, i, this, new Air());
            }
        }
        this.linksSorted = Arrays.stream(grid)
                .flatMap(Arrays::stream)
                .sorted((l1, l2) -> Math.toIntExact(l1.getId() - l2.getId()))
                .collect(Collectors.toList());
        this.linksReversed = Arrays.stream(grid)
                .flatMap(Arrays::stream)
                .sorted((l1, l2) -> Math.toIntExact(l2.getId() - l1.getId()))
                .collect(Collectors.toList());
        this.linksRandomized = Arrays.stream(grid)
                .flatMap(Arrays::stream)
                .sorted((l1, l2) -> Math.toIntExact(l1.getRandomId() - l2.getRandomId()))
                .collect(Collectors.toList());
    }

    public Optional<Link> getLink(int x, int y) {
        if (this.isOutOfBounds(x, y))
            return Optional.empty();
        return Optional.of(this.grid[y][x]);
    }

    public void set(int x, int y, Element element) {
        if (this.isOutOfBounds(x, y))
            return;
        this.grid[y][x].set(element);
    }

    public boolean isOutOfBounds(int x, int y) {
        return y < 0 || y >= this.grid.length || x < 0 || x >= this.grid[0].length;
    }

    public void clear(int x, int y) {
        this.set(x, y, new Air());
    }

    private final List<Link> linksSorted;

    public Stream<Link> stream() {
        return linksSorted.stream();
    }

    private final List<Link> linksReversed;

    public Stream<Link> stream(boolean reversed) {
        if (!reversed)
            return this.stream();
        return this.linksReversed.stream();
    }

    private final List<Link> linksRandomized;

    public Stream<Link> streamRandom() {
        return linksRandomized.stream();
    }


}
