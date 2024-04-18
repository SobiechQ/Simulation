package Elements.NEW;
import Elements.Api.Refreshable;
import Map.Utils.Vector;
import Map.Link;
//todo private refreshable class that handles refresh element pool

public interface NewMoveable extends Refreshable {
    @Override
    default void refresh(Link l) {
        var link = l;
        while (getVelocity().step())
            link = this.move(link);
    }
    Vector getVelocity();
    Link move(Link link);
}
