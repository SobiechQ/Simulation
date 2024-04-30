package Map;



import java.util.*;
import java.util.concurrent.*;
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;

public class GridManager {

    private final Chunk[][] chunks;
    private final Set<Link> randomOrderLinks;
    private final Set<Chunk> randomOrderChunks;

    private final ScheduledExecutorService chunkThreads;

    public GridManager(int xChunkCount, int yChunkCount) {
        this.chunks = new Chunk[yChunkCount][xChunkCount];
        this.randomOrderLinks = new TreeSet<>(Comparator.comparingDouble((Link o) -> o.getRandomOrderSeed()));
        this.randomOrderChunks = new TreeSet<>(Comparator.comparingDouble((ToDoubleFunction<Chunk>) value -> value.getLinkLocal(0,0).get().getRandomOrderSeed()));
        for (int y = 0; y < yChunkCount; y++) {
            for (int x = 0; x < xChunkCount; x++) {
                final var newChunk = new Chunk(this, x, y);;
                this.chunks[y][x] = newChunk;
                newChunk.streamLocal().forEach(this.randomOrderLinks::add);
                this.randomOrderChunks.add(newChunk);
            }
        }
        this.chunkThreads = new ScheduledThreadPoolExecutor(8);
        this.chunkStream().forEach(c-> this.chunkThreads.scheduleAtFixedRate(c, 0L, 40, TimeUnit.MILLISECONDS));


//        for (int i = 100; i < 130; i++) {
//            for (int j = 0; j < 200; j++) {
//                this.getLink(j, i).ifPresent(l->l.set(new Water()));
//            }
//        }
//        for (int i = 30; i < 80; i++) {
//            for (int j = 0; j < 300; j++) {
//                this.getLink(j, i).ifPresent(l->l.set(new MagicSand()));
//            }
//        }
//        for (int i = 90; i < 110; i++) {
//            for (int j = 0; j < 300; j++) {
//                if (Math.random()>0.95)
//                    this.getLink(j, i).ifPresent(l->l.set(new Tnt()));
//            }
//        }
//        for (int i = 10; i < 30; i++) {
//            for (int j = 0; j < 300; j++) {
//                if (Math.random()>0.95)
//                    this.getLink(j, i).ifPresent(l->l.set(new Generator()));
//            }
//        }
//        this.linkStream()
//                .forEach(l->{
//                    if (Math.random()>0.99)
//                        l.set(new Generator());
//                });

    }

    public synchronized Optional<Link> getLink(int x, int y) {
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
    public Stream<Chunk> chunkStream(){
        return this.randomOrderChunks.stream();
    }
    public int getXReal(Link link){
        return link.getChunk().getChunkX() * Chunk.CHUNK_SIZE + link.getXLocal();
    }
    public int getYReal(Link link){
        return link.getChunk().getChunkY() * Chunk.CHUNK_SIZE + link.getYLocal();
    }


}





