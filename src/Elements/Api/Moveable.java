package Elements.Api;
import Elements.Vector;
import Map.GridDecorator;
import Map.Link;

public interface Moveable {
    Vector getVelocity();
    void computeVector(Link link);
}
