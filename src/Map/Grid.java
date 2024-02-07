package Map;

import Elements.*;
import Elements.Api.Element;
import Elements.Api.Loose;
import Elements.Api.Moveable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Grid { //todo iterable z opcją modyfikacji?

    private final GridDecorator grid;
    private final int gridSize;

    public Grid(int gridSize) {
        this.gridSize = gridSize;
        this.grid = new GridDecorator(this.gridSize);
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
        this.grid.set(50,50, new Sand());
        this.grid.set(35,140, new Tnt());
        this.grid.set(35,139, new Tnt());
        this.grid.set(35,138, new Tnt());
        this.grid.set(35,137, new Tnt());
        this.grid.set(35,136, new Tnt());
        this.grid.set(40,140, new Tnt());
        this.grid.set(40,139, new Tnt());
        this.grid.set(40,138, new Tnt());
        this.grid.set(40,137, new Tnt());
        this.grid.set(45,136, new Tnt());
    }
    private int frameCounter = 0;
    public void nextFrame() {
        Set<Element> moved = new HashSet<>();
        frameCounter++;
        for (int i = this.gridSize - 1; i >= 0; i--)
            for (int j = gridSize; j >= 0; j--) { //funkcyjnie
                Element element = this.grid.getElement(i, j).orElse(new Air());
                if (element instanceof Moveable) {
                    Loose moveable = (Loose) element; //todo przetestuj dla jednego kawałka piachu
                    if (moved.contains(moveable))
                        continue;
                    moveable.computeVector(this.grid.getLink(i, j).get());
                    moved.add(moveable);
                }
                if (element instanceof Tnt tnt){
                    tnt.nextFrame(this.grid.getLink(i, j).get());
                }
            }
        if (frameCounter%70==0)
            for (int i=40; i<=70; i++){
                for (int j=0; j<=5; j++){
                    this.grid.set(i, j, new Sand()); //todo funkcyjnie
                }
            }
        if (frameCounter%60==0)
            for (int i=10; i<=30; i++){
                for (int j=45; j<=75; j++){
//                    this.grid.unsetElement(i,j);
                }
            }
    }
    public Optional<Element> getElement(int x, int y) { //todo remove. make stream
        return this.grid.getElement(x, y);
    }
}





