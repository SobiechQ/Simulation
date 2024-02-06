package Map;

import Elements.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class Grid { //todo iterable z opcją modyfikacji?

    private final GridDecorator grid;
    private final int gridSize;

    public Grid(int gridSize) {
        this.gridSize = gridSize;
        this.grid = new GridDecorator(this.gridSize);
        for (int i=30; i<=40; i++){
            for (int j=40; j<=70; j++){
                this.grid.setElement(i, j, new Sand());
            }
        }
        for (int i=20; i<=90; i++){
            for (int j=0; j<=5; j++){
                this.grid.setElement(i, j, new Sand());
            }
        }
        for (int i=10; i<=30; i++){
            for (int j=45; j<=75; j++){
                this.grid.setElement(i, j, new Stone());
            }
        }
        this.grid.setElement(50,50, new Sand());
    }
    private int frameCounter = 0;
    public void nextFrame() {
        Set<Element> moved = new HashSet<>();
        frameCounter++;
        for (int i = this.gridSize - 1; i >= 0; i--)
            for (int j = gridSize; j >= 0; j--) {
                Element element = this.grid.getElement(i, j).orElse(new Air());
                if (element instanceof Moveable) {
                    Loose moveable = (Loose) element; //todo przetestuj dla jednego kawałka piachu
                    if (moved.contains(moveable))
                        continue;
                    moveable.computeVector(this.grid.getGriderator(i, j));
                    moved.add(moveable);

                }
            }
        if (frameCounter%70==0)
            for (int i=40; i<=90; i++){
                for (int j=0; j<=2; j++){
//                    this.grid.setElement(i, j, new Sand());
                }
            }
    }
    public Optional<Element> getElement(int x, int y) {
        return this.grid.getElement(x, y);
    }
}





