package Map;

import Elements.Air;
import Elements.Api.Element;
import Map.Utils.Direction;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Stream;

public class Link {
    private final long id;
    private final int randomId;
    private static long counter = 0;
    private final int x;
    private final int y;
    private final Grid grid;
    private Element element;
    public Link(int x, int y, Grid grid, Element element) {
        this.x = x;
        this.y = y;
        this.grid = grid;
        this.element = element;
        this.id = counter++;
        this.randomId = (int) (Math.random()*10000);
    }
    public Optional<Link> get(Direction... directions){
        //optional make
        return this.get(new ArrayDeque<>(List.of(directions)));
    }
    private Optional<Link> get(Queue<Direction> directions){
        if (directions.isEmpty())
            return Optional.of(this);
        //optional make
        var get = this.get(directions.poll());
        if (get.isEmpty())
            return Optional.empty();
        return get.get().get(directions);
    }
    public Optional<Link> get(Direction direction){
        return switch (direction) {
            case UP -> this.grid.getLink(this.x, this.y - 1);
            case DOWN -> this.grid.getLink(this.x, this.y + 1);
            case LEFT -> this.grid.getLink(this.x - 1, this.y);
            case RIGHT -> this.grid.getLink(this.x + 1, this.y);
            case NONE -> Optional.of(this);
        };
    }
    public boolean isInstanceOf(Class<?> clazz, Direction... direction){
        return this.get(direction)
                .filter(link -> clazz.isInstance(link.getElement()))
                .isPresent();
    }

    public Element getElement() {
        return element;
    }

    public void clear() {
        this.set(new Air());
    }

    public void set(Element element) {
        this.element = element;
    }
    public Stream<Link> stream(){
        return this.grid.stream();
    }
    public double distance(Link link){
        return Math.sqrt(Math.pow(this.x - link.x, 2) + Math.pow(this.y - link.y, 2));
    }

    @Override
    public String toString() {
        return String.format("Element [%s] at [%s, %s]\nup [%s]\ndown [%s]\nleft [%s]\nright [%s]", this.element, this.x, this.y, this.get(Direction.UP).map(Link::getElement).orElse(null), this.get(Direction.DOWN).map(Link::getElement).orElse(null), this.get(Direction.LEFT).map(Link::getElement).orElse(null), this.get(Direction.RIGHT).map(Link::getElement).orElse(null));
    }
    public double deltaX(Link link){
        return link.x - this.x;
    }
    public double deltaY(Link link){
        return this.y - link.y;
    }
    public long getId() {
        return id;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public int getRandomId() {
        return randomId;
    }
}
