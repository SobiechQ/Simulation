package Map;

import Elements.Solid.Air;
import Elements.Api.Core.Element;
import Map.Utils.Direction;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Link is a position aware container for {@link Element elements}. Every link represents a single unmoving point on the map,
 * that can store a single element. It is used to store elements in a {@link GridManager grid-like structure}.
 * It has public access to its {@link Element element} and can be modified by setting a new element or swapping elements with another link.
 * Operations performed on Link are protected by a {@link ReadWriteLock read write lock} to prevent concurrent modification.
 */
public class Link {
    private final int xLocal;
    private final int yLocal;
    @Getter
    private final Chunk chunk;
    private Element element;
    @Getter
    private final double randomOrderSeed;
    @Getter
    private final ReadWriteLock lock = new ReentrantReadWriteLock(true);

    /**
     * For a given chunk creates a link at given local coordinates with default {@link Air} element.
     * @param xLocal x coordinate of the link in the chunk
     * @param yLocal y coordinate of the link in the chunk
     * @param chunk chunk that the link is in
     */
    public Link(int xLocal, int yLocal, Chunk chunk) {
        this(xLocal, yLocal, chunk, new Air());
    }

    /**
     * For a given chunk creates a link at given local coordinates with given element.
     * @param xLocal x coordinate of the link in the chunk
     * @param yLocal y coordinate of the link in the chunk
     * @param chunk chunk that the link is in
     * @param element element that the link will store
     */
    public Link(int xLocal, int yLocal, Chunk chunk, Element element) {
        this.xLocal = xLocal;
        this.yLocal = yLocal;
        this.chunk = chunk;
        this.element = element;
        this.randomOrderSeed = Math.random();
    }

    /**
     * Returns a link in a given directions from the current link. Directions are executed in the order they are given.
     * @param directions directions to follow
     * @return Optional containing link in the given directions or empty if the link would be out of bounds
     */
    public Optional<Link> get(Direction... directions) {
        return this.get(new ArrayDeque<>(List.of(directions)));
    }
    /**
     * Returns a link in a given direction from the current link. Directions are executed in the order they are given.
     * @param directions directions to follow
     * @return Optional containing link in the given directions or empty if the link would be out of bounds
     */
    private Optional<Link> get(Queue<Direction> directions) {
        if (directions.isEmpty())
            return Optional.of(this);
        var pointer = this.get(directions.poll());
        if (pointer.isEmpty())
            return Optional.empty();
        return pointer.get().get(directions);
    }

    /**
     * Returns a link in a given direction from the current link.
     * @param direction direction to follow
     * @return Optional containing link in the given direction or empty if the link would be out of bounds
     */
    public Optional<Link> get(Direction direction) {
        final var gm = this.chunk.getGridManager();
        return switch (direction) {
            case UP -> gm.getLink(gm.getXAbsolute(this), gm.getYAbsolute(this) - 1);
            case DOWN -> gm.getLink(gm.getXAbsolute(this), gm.getYAbsolute(this) + 1);
            case LEFT -> gm.getLink(gm.getXAbsolute(this) - 1, gm.getYAbsolute(this));
            case RIGHT -> gm.getLink(gm.getXAbsolute(this) + 1, gm.getYAbsolute(this));
            case NONE -> Optional.of(this);
        };
    }

    public Set<Link> surroundingLink(int squareSize) { //todo Funkcyjnie niech zwraca stream
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
            this.element = element;
        } finally {
            lock.writeLock().unlock();
        }
    }
    public void setElement(Predicate<Element> predicate, Element element){
        lock.writeLock().lock();
        try {
            if (predicate.test(this.element))
                this.setElement(element);
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
            return this;
        } finally {
            this.lock.writeLock().unlock();
        }
    }
    public Color getColor(){
        return this.element.getColor();
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
        return this.xLocal;
    }

    public int getXReal() {
        return this.chunk.getGridManager().getXAbsolute(this);
    }

    public int getYLocal() {
        return this.yLocal;
    }

    public int getYReal() {
        return this.chunk.getGridManager().getYAbsolute(this);
    }

    @Deprecated(forRemoval = true)
    public Stream<Link> stream() {
        return this.chunk.getGridManager().linkStream();
    }
    @Deprecated(forRemoval = true)
    public Stream<Link> stream(double radius) {
        return this.stream().filter(l -> l.distance(this) <= radius);
    }

}
