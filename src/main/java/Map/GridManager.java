package Map;

import Elements.Api.Core.Element;
import Elements.Api.Core.Refreshable;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class GridManager {

    private final Grid grid;

    public GridManager(int width, int height) {
        this.grid = new Grid(width, height);
    }
    public void nextFrame() {
        Set<Element> moved = new HashSet<>();
        this.grid.streamRandom()
                .filter(link -> link.getElement() instanceof Refreshable)
                .forEach(link -> {
                    if (!moved.contains(link.getElement())) {
                        if (link.getElement() instanceof Refreshable refreshable){
                            refreshable.refresh(link);
                            moved.add(link.getElement());
                        }
                    }
                });
    }
    public Stream<Link> stream() {
        return this.grid.stream();
    }
    public Stream<Link> stream(boolean reversed) {
        return this.grid.stream(reversed);
    }
    public Grid getGrid(){
        return this.grid;
    }
}





