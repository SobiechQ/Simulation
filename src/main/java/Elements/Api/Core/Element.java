package Elements.Api.Core;

import Map.Link;

import java.awt.Color;

/**
 * Element is the base class for all elements in the simulation. It represens all objects visible on the screen.
 * All elements and their subtypes are in its design position agnostic.
 * They are not aware of their position in the grid, nor about their neighbors, chunks or any other elements.
 * They have no behavior, thus only permited subtypes of Element can be instantiated.
 * To perform action with position awareness, use {@link Map.Link} and its methods.
 * To instantiate a static element without any behaviour extend {@link Solid}
 *
 * @see Map.Link
 * @see Solid
 * @see Loose
 * @see Fluid
 * @see Particle
 */

public abstract sealed class Element permits Loose, Fluid, Solid, Particle {
    private final static Color DEFAULT_COLOR = Color.MAGENTA;
    private Color color = Element.DEFAULT_COLOR;

    /**
     * Position agnostic constructor. Used for elements that do not require any position awareness on creation.
     * For elements that require position awareness use {@link Element#Element(Link)}
     */
    public Element() {

    }

    /**
     * Position aware constructor. Used for elements that require position awareness on creation.
     * This is default constructor invoked by reflection when creating elements from the map with mouse click.
     * If this constructor is not present in the element subclass reflection will create element with {@link Element#Element() position agnostic constructor}.
     *
     * @param link the link that the element is created on
     * @see Map.Link
     */
    public Element(Link link) {

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
    public void setColor(Color color) {
        this.color = color;
    }


}
