package Elements.Api;

import Map.Link;

/**
 * Flameble elements can be set on fire and extinguished.
 * @see Elements.Solid.Wood
 */
public interface Flameable extends Refreshable {
    /**
     * Sets the element on fire.
     */
    void setOnFire();

    /**
     * Returns true if the element is on fire.
     * @return true if the element is on fire.
     */
    boolean isOnFire();

    /**
     * Extinguishes the element. By making it position aware, flameble elements can extinguish other flameble elements.
     * @param link the link of the element to be extinguished.
     */
    void extinguish(Link link);

}

