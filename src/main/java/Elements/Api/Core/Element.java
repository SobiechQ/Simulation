package Elements.Api.Core;

import Map.Link;
import lombok.NonNull;
import lombok.val;

import java.awt.Color;

/**
 * Element is the base class for all elements in the simulation. It represens all objects visible on the screen.
 * All elements and their subtypes are in its design position agnostic.
 * They are not aware of their position in the grid, nor about their neighbors, chunks or any other elements.
 * They have no behavior, thus only permited subtypes of Element can be instantiated.
 * To perform action with position awareness, use {@link Map.Link} and its methods.
 * To instantiate an element without any predefined behaviour extend {@link Solid}
 *
 * @see Map.Link
 * @see Solid
 * @see Loose
 * @see Fluid
 * @see Particle
 */

public abstract sealed class Element permits Loose, Fluid, Solid, Particle {
    private final static Color DEFAULT_COLOR = Color.MAGENTA;
    protected Color color;

    /**
     * Position agnostic constructor. Used for elements that do not require any position awareness on creation.
     * This constructor is invoked by reflection only if the element subclass does not have {@link Element#Element(Link) position aware constructor}.
     * At least one public constructor (position agnostic or aware) is required for each element subclass.
     */
    public Element() {
        this.color = DEFAULT_COLOR;
    }

    /**
     * Position aware constructor. Used for elements that require position awareness on creation.
     * This is default constructor invoked by reflection when creating elements from the map with mouse click.
     * If this constructor is not present in the element subclass reflection will create element with {@link Element#Element() position agnostic constructor} instead.
     * At least one public constructor (position agnostic or aware) is required for each element subclass.
     *
     * @param link the link that the element is created on
     * @see Map.Link
     */
    public Element(@NonNull Link link) {
        this.color = DEFAULT_COLOR;
    }

    /**
     * Returns the property color of the element
     *
     * @return the color of the element
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Sets the property color of the element
     *
     * @param color the color to set
     */
    public void setColor(@NonNull Color color) {
        this.color = color;
    }



}
