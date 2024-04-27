package Elements.Api;

import Map.Link;

public interface Flameable extends Refreshable {
    void setIsOnFire(boolean isOnFire);

    boolean isOnFire();
    void extinguish(Link link);

}

