package Map;

import Elements.Solid.Air;
import Elements.Api.Core.Element;
import Map.Utils.Direction;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

public class Link {
    private final int xLocal;
    private final int yLocal;
    private final Chunk chunk;
    private Element element;
    private final double randomOrderSeed;
    private final ReadWriteLock lock = new ReentrantReadWriteLock(true);

    public Link(int xLocal, int yLocal, Chunk chunk) {
        this(xLocal, yLocal, chunk, new Air());
    }

    public Link(int xLocal, int yLocal, Chunk chunk, Element element) {
        this.xLocal = xLocal;
        this.yLocal = yLocal;
        this.chunk = chunk;
        this.element = element;
        this.randomOrderSeed = Math.random();
    }

    public double getRandomOrderSeed() {
        return this.randomOrderSeed;
    }

    public Optional<Link> get(Direction... directions) {
        return this.get(new ArrayDeque<>(List.of(directions)));
    }

    private Optional<Link> get(Queue<Direction> directions) {
        if (directions.isEmpty())
            return Optional.of(this);
        var get = this.get(directions.poll());
        if (get.isEmpty())
            return Optional.empty();
        return get.get().get(directions);
    }

    public Optional<Link> get(Direction direction) {
        final var gridManager = this.chunk.getGridManager();
        return switch (direction) {
            case UP -> gridManager.getLink(gridManager.getXAbsolute(this), gridManager.getYAbsolute(this) - 1);
            case DOWN -> gridManager.getLink(gridManager.getXAbsolute(this), gridManager.getYAbsolute(this) + 1);
            case LEFT -> gridManager.getLink(gridManager.getXAbsolute(this) - 1, gridManager.getYAbsolute(this));
            case RIGHT -> gridManager.getLink(gridManager.getXAbsolute(this) + 1, gridManager.getYAbsolute(this));
            case NONE -> Optional.of(this);
        };
    }

    public Set<Link> surroundingLink(int squareSize) {
        this.lock.readLock().lock();
        try {
            if (squareSize < 0)
                throw new IllegalArgumentException("Square size cant be less then 0");
            final Set<Link> links = new HashSet<>();
            for (int i = -squareSize; i <= squareSize; i++) {
                for (int j = -squareSize; j <= squareSize; j++) {
                    this.chunk.getGridManager()
                            .getLink(this.getXReal() + j, this.getYReal() + i)
                            .ifPresent(links::add);
                }
            }
            return links;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public Set<Link> elementClump() {
        final var clazz = this.element.getClass();
        final Set<Link> links = new HashSet<>();
        links.add(this);
        final Set<Link> toAdd = new HashSet<>();
        do {
            links.addAll(toAdd);
            toAdd.clear();
            links.forEach(l -> l.surroundingLink(1)
                    .stream()
                    .filter(l2 -> l2.isInstanceOf(clazz) && !links.contains(l2))
                    .forEach(toAdd::add)
            );
        } while (!toAdd.isEmpty());
        return links;
    }


    public boolean isInstanceOf(Class<?> clazz, Direction... direction) {
        return this.get(direction)
                .filter(link -> clazz.isInstance(link.getElement()))
                .isPresent();
    }

    public Element getElement() {
        lock.readLock().lock();
        try {
            return this.element;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void clear() {
        lock.writeLock().lock();
        try {
            if (!this.isInstanceOf(Air.class))
                this.setElement(new Air());
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void setElement(Element element) {
        lock.writeLock().lock();
        try {
//            if (element instanceof Air) { //todo test
//                this.element = new Air();
//                return;
//            }
            this.element = element;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * @return true if modified link on given directions
     */
    public boolean setElement(Element element, Direction... directions) {
        var get = this.get(directions);
        get.ifPresent(link -> link.setElement(element));
        return get.isPresent();
    }

    public Link swap(Direction... directions) {
        this.lock.writeLock().lock();
        try {
            final var linkPointer = this.get(directions);
            if (linkPointer.isEmpty())
                return this;
            final var linkPointerWriteLock = linkPointer.get().getLock().writeLock();

            if (linkPointerWriteLock.tryLock()) {
                try {
                    final var moveElement = this.getElement();
                    this.setElement(linkPointer.get().getElement());
                    linkPointer.get().setElement(moveElement);
                    return linkPointer.get();
                } finally {
                    linkPointerWriteLock.unlock();
                }
            }
            System.out.println(linkPointer);
            return this;
        } finally {
            this.lock.writeLock().unlock();
        }

    }

    public double distance(Link link) {
        return Math.sqrt(Math.pow(this.deltaX(link), 2) + Math.pow(this.deltaY(link), 2));
    }

    public double distance(int x, int y) {
        return Math.sqrt(Math.pow(this.getXReal() - x, 2) + Math.pow(this.getYReal() - y, 2));
    }

    @Override
    public String toString() {
        return String.format("Element [%s] at [%s, %s]\nup [%s]\ndown [%s]\nleft [%s]\nright [%s]", this.element, this.xLocal, this.yLocal, this.get(Direction.UP).map(Link::getElement).orElse(null), this.get(Direction.DOWN).map(Link::getElement).orElse(null), this.get(Direction.LEFT).map(Link::getElement).orElse(null), this.get(Direction.RIGHT).map(Link::getElement).orElse(null));
    }

    public double deltaX(Link link) {
        return Math.abs(this.getXReal() - link.getXReal());
    }

    public double deltaY(Link link) {
        return Math.abs(this.getYReal() - link.getYReal());
    }

    public int getXLocal() {
        return xLocal;
    }

    public int getXReal() {
        return this.chunk.getGridManager().getXAbsolute(this);
    }

    public int getYLocal() {
        return yLocal;
    }

    public int getYReal() {
        return this.chunk.getGridManager().getYAbsolute(this);
    }

    public Chunk getChunk() {
        return chunk;
    }
    @Deprecated(forRemoval = true)
    public Stream<Link> stream() {
        return this.chunk.getGridManager().linkStream();
    }
    @Deprecated(forRemoval = true)
    public Stream<Link> stream(double radius) {
        return this.stream().filter(l -> l.distance(this) <= radius);
    }

    public ReadWriteLock getLock() {
        return this.lock;
    }
}
