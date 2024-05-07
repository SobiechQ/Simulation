package Elements.Api;

import Map.Link;

/**
 * Refreshable is an functional interface that allows an element to perform actions with position awareness on every tick.
 * It is used to update the state of the element in the simulation.
 * Its invoked once for every element in the simulation per every frame.
 * Due to multithreading more then one element can be refreshed at the same time.
 * It is guaranteed that single element will not be refreshed by more then one thread at the same time.
 * Order of elements to refresh is not guaranteed, as its based on random seed values in {@link Link link} class.
 */
@FunctionalInterface
public interface Refreshable {
    /**
     * Refreshes the element on every tick.
     * @see Refreshable
     * @param link the link that the element is refreshed on for the current tick. It allowes to perform actions with position awareness.
     */
    void refresh(Link link);

}
