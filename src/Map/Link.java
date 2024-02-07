package Map;

import Elements.Air;
import Elements.Api.Element;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Stream;

public class Link {
    private long id;
    private static long counter = 0;
    private final int x;
    private final int y;
    private final GridDecorator gridDecorator;
    private Element element;
    public Link(int x, int y, GridDecorator gridDecorator, Element element) {
        this.x = x;
        this.y = y;
        this.gridDecorator = gridDecorator;
        this.element = element;
        this.id = counter++;
    }
//    public boolean isTypeOf(Class<?> type, Direction... direction){
//        return false;
//    }
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
            case UP -> this.gridDecorator.getLink(this.x, this.y - 1);
            case DOWN -> this.gridDecorator.getLink(this.x, this.y + 1);
            case LEFT -> this.gridDecorator.getLink(this.x - 1, this.y);
            case RIGHT -> this.gridDecorator.getLink(this.x + 1, this.y);
            case NONE -> Optional.of(this);
        };
    }

    public Element getElement() {
        return element;
    }


    public void clear() {
        this.set(new Air()); //todo unset
    }

    public void set(Element element) {
        this.element = element;
    }
    public Stream<Link> stream(){
        return this.gridDecorator.stream();
    }
    public double distance(Link link){
        return Math.sqrt(Math.pow(this.x - link.x, 2) + Math.pow(this.y - link.y, 2));
    }

    @Override
    public String toString() {
        return String.format("Element [%s] at [%s, %s]\nup [%s]\ndown [%s]\nleft [%s]\nright [%s]", this.element, this.x, this.y, this.get(Direction.UP).map(Link::getElement).orElse(null), this.get(Direction.DOWN).map(Link::getElement).orElse(null), this.get(Direction.LEFT).map(Link::getElement).orElse(null), this.get(Direction.RIGHT).map(Link::getElement).orElse(null));
    }
    public double deltaX(Link link){
        return this.x - link.x;
    }
    public double deltaY(Link link){
        return this.y - link.y;
    }

    public long getId() {
        return id;
    }
}
