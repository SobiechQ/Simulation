package Elements.Api;
import Elements.Vector;
import Map.GridDecorator;
import Map.Griderator;

public interface Moveable {
    Vector getVelocity();
    void computeVector(Griderator griderator);
}
