package Map;

import Elements.Solid.Air;
import Elements.Api.Refreshable;
import lombok.Getter;

import java.util.*;
import java.util.stream.Stream;

public class Chunk implements Runnable {
    public final static int CHUNK_SIZE = 16;
    private final Link[][] grid;
    private final List<Link> linkRandomOrder;
    @Getter
    private final int chunkX;
    @Getter
    private final int chunkY;
    @Getter
    private final GridManager gridManager;
    private double lastSystemTimeOnRun = 0;
    @Getter
    private double lastSystemTimeDifference = 0;


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

    @Override
    public void run() {
        this.lastSystemTimeDifference = (System.nanoTime() - this.lastSystemTimeOnRun) / 1e6;
        this.lastSystemTimeOnRun = System.nanoTime();

        this.streamLocal()
                .forEach(l -> {
                    if (l.getElement() instanceof Refreshable refreshable)
                        refreshable.refresh(l);
                });
    }


}
