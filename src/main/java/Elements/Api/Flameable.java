package Elements.Api;

import Map.Link;

public interface Flameable extends Refreshable {
    void setOnFire(boolean isOnFire);

    boolean isOnFire();
    void extinguish(Link link);

}

