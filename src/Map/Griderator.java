package Map;

import Elements.Air;
import Elements.Api.Element;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

/**
    * Griderator is a class that allows for the traversal of a 2D grid.
    * Always contains current non null value and optional values for the top, bottom, left, and right.
 */
public abstract class Griderator {
    private final int x;
    private final int y;
    public Griderator(int x, int y) {
        if (this.isOutOfBounds(x, y))
            throw new NoSuchElementException("Griderator cant be created outside of bounds.");
        this.x = x;
        this.y = y;
    }
    public abstract Element current(int x, int y);
    public Element current(){
        return this.current(this.x, this.y);
    }
    public abstract boolean isOutOfBounds(int x, int y);
    public abstract Stream<Element> inRadius(int x, int y, int radius);
    public Stream<Element> inRadius(int radius){
        return this.inRadius(this.x, this.y, radius);
    }
    public abstract void set(int x, int y, Element element);
    public void setCurrent(Element element){
        this.set(this.x, this.y, element);
    }
    public void unsetCurrent(){
        this.setCurrent(new Air());
    }
    public Optional<Griderator> getUp(){
        if (this.isOutOfBounds(this.x, this.y - 1))
            return Optional.empty();
        return Optional.of(new Griderator(this.x, this.y - 1) {
            @Override
            public Element current(int x, int y) {
                return Griderator.this.current(x, y);
            }

            @Override
            public boolean isOutOfBounds(int x, int y) {
                return Griderator.this.isOutOfBounds(x, y);
            }

            @Override
            public Stream<Element> inRadius(int x, int y, int radius) {
                return Griderator.this.inRadius(x, y, radius);
            }

            @Override
            public void set(int x, int y, Element element) {
                Griderator.this.set(x, y, element);
            }
        });
    }
    public Object peekUp() {
        var up = this.getUp();
        if (up.isEmpty())
            return new Object();
        return up.get().current();
    }
    public Optional<Griderator> getDown(){
        if (this.isOutOfBounds(this.x, this.y + 1))
            return Optional.empty();
        return Optional.of(new Griderator(this.x, this.y + 1) {
            @Override
            public Element current(int x, int y) {
                return Griderator.this.current(x, y);
            }

            @Override
            public boolean isOutOfBounds(int x, int y) {
                return Griderator.this.isOutOfBounds(x, y);
            }
            @Override
            public Stream<Element> inRadius(int x, int y, int radius) {
                return Griderator.this.inRadius(x, y, radius);
            }

            @Override
            public void set(int x, int y, Element element) {
                Griderator.this.set(x, y, element);
            }
        });
    }
    public Object peekDown() {
        var down = this.getDown();
        if (down.isEmpty())
            return new Object();
        return down.get().current();
    }
    public Optional<Griderator> getLeft(){
        if (this.isOutOfBounds(this.x - 1, this.y))
            return Optional.empty();
        return Optional.of(new Griderator(this.x - 1, this.y) {
            @Override
            public Element current(int x, int y) {
                return Griderator.this.current(x, y);
            }

            @Override
            public boolean isOutOfBounds(int x, int y) {
                return Griderator.this.isOutOfBounds(x, y);
            }
            @Override
            public Stream<Element> inRadius(int x, int y, int radius) {
                return Griderator.this.inRadius(x, y, radius);
            }

            @Override
            public void set(int x, int y, Element element) {
                Griderator.this.set(x, y, element);
            }
        });
    }
    public Object peekLeft() {
        var left = this.getLeft();
        if (left.isEmpty())
            return new Object();
        return left.get().current();
    }
    public Optional<Griderator> getRight(){
        if (this.isOutOfBounds(this.x + 1, this.y))
            return Optional.empty();
        return Optional.of(new Griderator(this.x + 1, this.y) {
            @Override
            public Element current(int x, int y) {
                return Griderator.this.current(x, y);
            }

            @Override
            public boolean isOutOfBounds(int x, int y) {
                return Griderator.this.isOutOfBounds(x, y);
            }
            @Override
            public Stream<Element> inRadius(int x, int y, int radius) {
                return Griderator.this.inRadius(x, y, radius);
            }

            @Override
            public void set(int x, int y, Element element) {
                Griderator.this.set(x, y, element);
            }
        });
    }
    public Object peekRight() {
        var right = this.getRight();
        if (right.isEmpty())
            return new Object();
        return right.get().current();
    }

    protected int getX() {
        return x;
    }

    protected int getY() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("x=%s, y=%s, current=%s, up=%s, down=%s, left=%s, right=%s",
                this.x, this.y,
                this.current().toString(),
                this.getUp().map(griderator -> griderator.current().toString()).orElse("empty"),
                this.getDown().map(griderator -> griderator.current().toString()).orElse("empty"),
                this.getLeft().map(griderator -> griderator.current().toString()).orElse("empty"),
                this.getRight().map(griderator -> griderator.current().toString()).orElse("empty"));
    }
}
