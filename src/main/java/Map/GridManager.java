package Map;


import Elements.Fluid.Water;
import Elements.Solid.PerlinTest;
import com.google.gson.Gson;

import java.awt.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;

public class GridManager {

    private final Chunk[][] chunks;
    private final Set<Link> randomOrderLinks;
    private final Set<Chunk> randomOrderChunks;

    private final ScheduledExecutorService chunkThreads;

    public GridManager(int chunksWidth, int chunksHeight) {
        this.chunks = new Chunk[chunksHeight][chunksWidth];
        this.randomOrderLinks = new TreeSet<>(Comparator.comparingDouble((Link o) -> o.getRandomOrderSeed()));
        this.randomOrderChunks = new TreeSet<>(Comparator.comparingDouble((ToDoubleFunction<Chunk>) value -> value.getLinkLocal(0, 0).get().getRandomOrderSeed()));
        for (int y = 0; y < chunksHeight; y++) {
            for (int x = 0; x < chunksWidth; x++) {
                final var newChunk = new Chunk(this, x, y);
                this.chunks[y][x] = newChunk;
                newChunk.streamLocal().forEach(this.randomOrderLinks::add);
                this.randomOrderChunks.add(newChunk);
            }
        }
        this.chunkThreads = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
        this.chunkStream().forEach(c -> this.chunkThreads.scheduleAtFixedRate(c, 0L, 40, TimeUnit.MILLISECONDS));
        randomOrderLinks.forEach(l->l.setElement(new PerlinTest(l)));

    }

    public Optional<Link> getLink(int x, int y) {
        final int chunkX = x / Chunk.CHUNK_SIZE;
        final int chunkY = y / Chunk.CHUNK_SIZE;
        final int localX = x % Chunk.CHUNK_SIZE;
        final int localY = y % Chunk.CHUNK_SIZE;
        if (chunkY < 0 || chunkY >= this.chunks.length || chunkX < 0 || chunkX >= this.chunks[chunkY].length)
            return Optional.empty();
        return this.chunks[chunkY][chunkX].getLinkLocal(localX, localY);
    }

    public Optional<Chunk> getChunk(int chunkX, int chunkY) {
        if (chunkY < 0 || chunkY >= this.chunks.length || chunkX < 0 || chunkX >= this.chunks[chunkY].length)
            return Optional.empty();
        return Optional.of(this.chunks[chunkY][chunkX]);
    }

    public Stream<Link> linkStream() {
        return this.randomOrderLinks.stream();
    }
//    public Stream<Link> linkStream(int x, int y, int radius) {
//        final var chunkRadius = radius / Chunk.CHUNK_SIZE + 1;
//
//    }

    public Stream<Chunk> chunkStream() {
        return this.randomOrderChunks.stream();
    }
    public Stream<Chunk> chunkStream(int x, int y, int radius) {
        final var chunkRadius = radius / Chunk.CHUNK_SIZE + 1;
        return this.randomOrderChunks.stream()
                .filter(c -> Math.abs(c.getChunkX() - x) < chunkRadius && Math.abs(c.getChunkY() - y) < chunkRadius);
    }

    public int getXAbsolute(Link link) {
        return link.getChunk().getChunkX() * Chunk.CHUNK_SIZE + link.getXLocal();
    }

    public int getYAbsolute(Link link) {
        return link.getChunk().getChunkY() * Chunk.CHUNK_SIZE + link.getYLocal();
    }
    public Dimension getDimensionInElements() {
        return new Dimension(this.chunks[0].length * Chunk.CHUNK_SIZE, this.chunks.length * Chunk.CHUNK_SIZE);
    }


}





