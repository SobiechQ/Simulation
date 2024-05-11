package Elements.Api.Core;

import Elements.Api.Core.Element;
import Map.Link;
import lombok.NonNull;

/**
 * Solid is the base class for all non moving elements in the simulation.
 */
public abstract non-sealed class Solid extends Element {
    /**
     * Inherited constructor from {@link Element#Element(Link)}
     * @see Element
     */
    public Solid(@NonNull Link link) {
        super(link);
    }
    /**
     * Inherited constructor from {@link Element#Element()}
     * @see Element
     */
    public Solid(){

    }
}
