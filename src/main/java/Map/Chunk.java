package Map;

import Elements.Solid.Air;
import Elements.Api.Refreshable;

import java.util.*;
import java.util.stream.Stream;

public class Chunk implements Runnable {
    public final static int CHUNK_SIZE = 16;
    private final Link[][] grid;
    private final List<Link> linkRandomOrder;
    private final int chunkX;
    private final int chunkY;
    private final GridManager gridManager;


    public Chunk(GridManager gridManager, int chunkX, int chunkY) {
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.gridManager = gridManager;
        this.grid = new Link[CHUNK_SIZE][CHUNK_SIZE];
        for (int i = 0; i < CHUNK_SIZE; i++)
            for (int j = 0; j < CHUNK_SIZE; j++)
                this.grid[i][j] = new Link(j, i, this);
        this.linkRandomOrder = Arrays.stream(this.grid)
                .flatMap(Arrays::stream)
                .sorted(Comparator.comparingDouble(value -> value.getRandomOrderSeed()))
                .toList();
    }

    public GridManager getGridManager() {
        return this.gridManager;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkY() {
        return chunkY;
    }

    public Optional<Link> getLinkLocal(int localX, int localY) {
        if (localY < 0 || localY >= this.grid.length || localX < 0 || localX >= this.grid[localY].length)
            return Optional.empty();
        return Optional.of(grid[localY][localX]);
    }

    public Stream<Link> streamLocal() {
        return this.linkRandomOrder.stream();
    }

    public Set<Chunk> surroundingChunks(int squareSize) {
        if (squareSize < 0)
            throw new IllegalArgumentException("Square size cant be less then 0");
        final Set<Chunk> chunks = new HashSet<>();
        for (int i = -squareSize; i <= squareSize; i++) {
            for (int j = -squareSize; j <= squareSize; j++) {
                this.gridManager
                        .getChunk(this.chunkX + j, this.chunkY + i)
                        .ifPresent(chunks::add);
            }
        }
        return chunks;
    }
    private long startTime = System.nanoTime();

    public boolean isWorking = false;
    @Override
    public void run() {
        this.isWorking = true;
//        if (this.streamLocal().anyMatch(l-> !(l.getElement() instanceof Air))) {
//            try {
//                Thread.sleep((long) (50 + Math.random() * 300));
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
        if (this.chunkX == 0 && this.chunkY == 0) {
            final long endTime = System.nanoTime();
            double difference = (endTime - this.startTime) / 1e6;
            System.out.println(difference);
            this.startTime = System.nanoTime();
//            System.out.println("Elements: " + this.gridManager.linkStream()
//                    .filter(l -> !(l.getElement() instanceof Air))
//                    .count());
        }

        this.streamLocal()
                .forEach(l -> {
                    if (l.getElement() instanceof Refreshable refreshable)
                        refreshable.refresh(l);
                });
        this.isWorking = false;
    }


}
