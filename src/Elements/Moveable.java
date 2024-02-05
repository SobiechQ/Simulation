package Elements;
import Map.GridDecorator;

public interface Moveable {
    Vector getVector();
    void computeVector(GridDecorator gridDetector);
}
