package Elements.Api;

import Map.Link;
import Map.Utils.Vector;

public interface Moveable extends Refreshable {
    @Override
    default void refresh(Link l){
        this.move(l);
    }
    Vector getVelocity();
    void move(Link link);
}
