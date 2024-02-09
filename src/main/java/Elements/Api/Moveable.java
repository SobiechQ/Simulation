package Elements.Api;
import Map.Utils.Vector;
import Map.Link;

public interface Moveable extends Refreshable {
    @Override
    default void refresh(Link l){
        this.move(l);
    }
    Vector getVelocity();
    void move(Link link);
}
