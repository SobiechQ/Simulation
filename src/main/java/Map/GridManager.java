package Map;

import Elements.Api.Core.Element;
import Elements.Api.Refreshable;

import java.util.*;
import java.util.stream.Stream;

public class GridManager {

    private final Chunk[][] chunks;
    private final Set<Link> randomOrderLinks;

    public GridManager(int xChunkCount, int yChunkCount) {
        this.chunks = new Chunk[yChunkCount][xChunkCount];
        this.randomOrderLinks = new TreeSet<>(Comparator.comparingDouble((Link o) -> o.getRandomOrderSeed()));
        for (int y = 0; y < yChunkCount; y++) {
            for (int x = 0; x < xChunkCount; x++) {
                final var newChunk = new Chunk(this, x, y);;
                this.chunks[y][x] = newChunk;
                newChunk.streamLocal().forEach(this.randomOrderLinks::add);
            }
        }
    }
    public void nextFrame() {
        Set<Element> moved = new HashSet<>();
        this.streamRandom()
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

    public Stream<Link> streamRandom() {
        return this.randomOrderLinks.stream();
    }
    public Stream<Chunk> chunkStream(){
        return Arrays.stream(this.chunks)
                .flatMap(Arrays::stream);
    }
    public int getXReal(Link link){
        return link.getChunk().getChunkX() * Chunk.CHUNK_SIZE + link.getXLocal();
    }
    public int getYReal(Link link){
        return link.getChunk().getChunkY() * Chunk.CHUNK_SIZE + link.getYLocal();
    }


}





