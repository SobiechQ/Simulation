package Elements.Api;

import Elements.Api.Core.Refreshable;

public interface Flameable extends Refreshable {
    void setIsOnFire(boolean isOnFire);
    boolean isOnFire();

}
