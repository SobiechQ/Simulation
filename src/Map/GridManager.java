package Map;

import Elements.*;
import Elements.Api.Element;
import Elements.Api.Loose;
import Elements.Api.Moveable;
import Elements.Api.Refreshable;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class GridManager {

    private final Grid grid;
    private final int gridSize;

    public GridManager(int gridSize) {
        this.gridSize = gridSize;
        this.grid = new Grid(this.gridSize);
        for (int i=30; i<=40; i++){
            for (int j=40; j<=70; j++){
                this.grid.set(i, j, new Sand());
            }
        }


        for (int i=20; i<=90; i++){
            for (int j=0; j<=10; j++){
                this.grid.set(i, j, new Sand());
            }
        }
        for (int i=10; i<=30; i++){
            for (int j=45; j<=75; j++){
                this.grid.set(i, j, new Stone());
            }
        }
        var sand = new Sand();
//        sand.setDebug();
        this.grid.set(35,30, sand);
        this.grid.set(35,140, new Tnt());
//        this.grid.set(35,139, new Tnt());
//        this.grid.set(35,138, new Tnt());
//        this.grid.set(35,137, new Tnt());
//        this.grid.set(35,136, new Tnt());
//        this.grid.set(40,140, new Tnt());
//        this.grid.set(40,139, new Tnt());
//        this.grid.set(40,138, new Tnt());
//        this.grid.set(40,137, new Tnt());
//        this.grid.set(45,136, new Tnt());
    }
    private int frameCounter = 0;
    public void nextFrame() { //todo co będzie dla losowej kolejności?
        Set<Element> moved = new HashSet<>();
        frameCounter++;
        this.grid.streamRandom()
                .filter(link -> link.getElement() instanceof Refreshable)
                .filter(link -> ((Refreshable)link.getElement()).getPriority() == 1 )
                .forEach(link -> {
                    if (!moved.contains(link.getElement())) {
                        if (link.getElement() instanceof Refreshable refreshable){
                            refreshable.refresh(link);
                            moved.add(link.getElement());
                        }
                    }
                });
        this.grid.streamRandom()
                .filter(link -> link.getElement() instanceof Refreshable)
                .filter(link -> ((Refreshable)link.getElement()).getPriority() == 0 )
                .forEach(link -> {
                    if (!moved.contains(link.getElement())) {
                        if (link.getElement() instanceof Refreshable refreshable){
                            refreshable.refresh(link);
                            moved.add(link.getElement());
                        }
                    }
                });

        if (frameCounter%70==0)
            for (int i=40; i<=70; i++){
                for (int j=0; j<=5; j++){
//                    this.grid.set(i, j, new Sand()); //todo funkcyjnie
                }
            }
        if (frameCounter%60==0)
            for (int i=10; i<=30; i++){
                for (int j=45; j<=75; j++){
//                    this.grid.unsetElement(i,j);
                }
            }
    }
    public Stream<Link> stream() {
        return this.grid.stream();
    }
    public Stream<Link> stream(boolean reversed) {
        return this.grid.stream(reversed);
    }
}





